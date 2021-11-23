package be.vito.rma.standalonetools.services;

import java.io.File;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.vito.rma.configtools.common.api.ConfigurationService;
import be.vito.rma.standalonetools.api.Mailer;
import lombok.Getter;
import lombok.Setter;

class MessageData {

	@Getter @Setter private MimeMessage mimeMessage;
	@Getter @Setter private String subject;
	@Getter @Setter private String recipientAddress;

}

/**
 * @author (c) 2014-2021 Stijn.VanLooy@vito.be
 *
 */
public class DefaultMailer extends Thread implements Mailer {

	public final static String EMAIL_INTERVAL = "email.interval.seconds";
	public final static String EMAIL_FROM = "email.from.address";
	public final static String EMAIL_SMTP = "email.smtp.server";

	private final int mailInterval;
	private final String fromAddress;

	private final Logger logger = LoggerFactory.getLogger(DefaultMailer.class);
	// thread save queue
	private final LinkedBlockingQueue<MessageData> queue = new LinkedBlockingQueue<>();

	public DefaultMailer (final ConfigurationService config) {
		super();
		System.getProperties().setProperty("mail.smtp.host", config.getString(EMAIL_SMTP));
		mailInterval = config.getInt(EMAIL_INTERVAL) * 1000;
		fromAddress = config.getString(EMAIL_FROM);
		start();
	}

	@Override
	public void run () {
		try {
			while (true) {
				final MessageData message = queue.take();
				sendMessage(message);
				/*
				 * make sure there is a mimimum amount of time between
				 * sending emails (lets not overload the email server)
				 */
				logger.debug("sleeping " + (mailInterval / 1000) + " seconds");
				Thread.sleep(mailInterval);
				if (isInterrupted()) break;
			}
		} catch (final InterruptedException e) {
			logger.warn("mailing thread interrupted", e);
		}
	}

	private void sendMessage (final MessageData message) {
		try {
			Transport.send(message.getMimeMessage());
			logger.info("Sent message with subject '" + message.getSubject() + "' to "
					+ message.getRecipientAddress());
		} catch (final MessagingException e) {
			logger.error("Failed to send message with subject '" + message.getSubject()
				+ "' to " + message.getRecipientAddress(), e);
		}
	}

	private MessageData createMessageData (final String recipientAddress, final String subject, final MimeMessage message) {
		final MessageData md = new MessageData();
		md.setMimeMessage(message);
		md.setRecipientAddress(recipientAddress);
		md.setSubject(subject);
		return md;
	}

	/*
	 * Inspired by: http://www.tutorialspoint.com/java/java_sending_email.htm
	 */
	@Override
	public void sendHtmlMail(final String recipientAddress,
			final String subject, final String htmlContent) {
		sendHtmlMail(recipientAddress, subject, htmlContent, true);
	}
	@Override
	public void sendHtmlMail(final String recipientAddress,
			final String subject, final String htmlContent, final boolean useQueue) {
		final Session session = Session.getDefaultInstance(System.getProperties());
		try {
			final MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
			message.setSubject(subject, "UTF-8");
			message.setContent(htmlContent, "text/html; charset=UTF-8");
			final MessageData md = createMessageData(recipientAddress, subject, message);
			if (useQueue) queue.add(md);
			else sendMessage(md);
		} catch (final MessagingException mex) {
			logger.error("Failed to send message with subject '" + subject + "' to " + recipientAddress, mex);
		}
	}

	@Override
	public void sendTextMail(final String recipientAddress,
			final String subject, final String textContent) {
		sendTextMail(recipientAddress, subject, textContent, true);
	}
	@Override
	public void sendTextMail(final String recipientAddress,
			final String subject, final String textContent, final boolean useQueue) {
		final Session session = Session.getDefaultInstance(System.getProperties());
		try {
			final MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
			message.setSubject(subject, "UTF-8");
			message.setText(textContent, "UTF-8");
			final MessageData md = createMessageData(recipientAddress, subject, message);
			if (useQueue) queue.add(md);
			else sendMessage(md);
		} catch (final MessagingException mex) {
			logger.error("Failed to send message with subject '" + subject + "' to " + recipientAddress, mex);
		}
	}

	@Override
	public void sendHtmlMailWithAttachments (final String recipientAddress,
			final String subject, final String htmlContent, final List<String> attachmentNames,
			final List<File> attachmentFiles) {
		sendHtmlMailWithAttachments(recipientAddress, subject, htmlContent, attachmentNames, attachmentFiles, true);
	}
	@Override
	public void sendHtmlMailWithAttachments (final String recipientAddress,
			final String subject, final String htmlContent, final List<String> attachmentNames,
			final List<File> attachmentFiles, final boolean useQueue) {
		final Session session = Session.getDefaultInstance(System.getProperties());
		try {
			final MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
			message.setSubject(subject);
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(htmlContent, "text/html");
			final Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			for (int i = 0; i < attachmentNames.size(); i++) {
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.setFileName(attachmentNames.get(i));
				final DataSource dataSource = new FileDataSource(attachmentFiles.get(i));
				messageBodyPart.setDataHandler(new DataHandler(dataSource));
				multipart.addBodyPart(messageBodyPart);
			}
			message.setContent(multipart);
			final MessageData md = createMessageData(recipientAddress, subject, message);
			if (useQueue) queue.add(md);
			else sendMessage(md);
		} catch (final MessagingException mex) {
			logger.error("Failed to send message with subject '" + subject + "' to " + recipientAddress, mex);
		}
	}

}
