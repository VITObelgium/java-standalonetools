package be.vito.rma.standalonetools.services;

import java.io.File;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.vito.rma.resttools.common.services.ConfigurationService;
import lombok.Getter;
import lombok.Setter;

class MessageData {

	@Getter @Setter private MimeMessage mimeMessage;
	@Getter @Setter private String subject;
	@Getter @Setter private String recipientAddress;

}

/**
 * @author (c) 2014-2016 Stijn.VanLooy@vito.be
 *
 */
@Component
public class Mailer extends Thread {

	public final static String EMAIL_INTERVAL = "email.interval.seconds";
	public final static String EMAIL_FROM = "email.from.address";
	public final static String EMAIL_SMTP = "email.smtp.server";

	private int mailInterval;
	private String fromAddress;

	@Autowired
	@Setter private ConfigurationService config;

	private final Logger logger = LoggerFactory.getLogger(Mailer.class);
	// thread save queue
	private LinkedBlockingQueue<MessageData> queue = new LinkedBlockingQueue<>();

	/**
	 * Do not use this constructor
	 * used by Spring
	 */
	public Mailer () {
		super();
	}

	/**
	 *  use this constructor outside Spring
	 * @param config
	 */
	public Mailer (final ConfigurationService config) {
		super();
		this.config = config;
		init();
	}

	/**
	 * called by Spring after construction, no need to run this outside Spring
	 */
	@PostConstruct
	public void init () {
		System.getProperties().setProperty("mail.smtp.host", config.getString(EMAIL_SMTP));
		mailInterval = config.getInt(EMAIL_INTERVAL) * 1000;
		fromAddress = config.getString(EMAIL_FROM);
		start();
	}

	@Override
	public void run () {
		try {
			while (true) {
				MessageData message = queue.take();
				sendMessage(message);
				/*
				 * make sure there is a mimimum amount of time between
				 * sending emails (lets not overload the email server)
				 */
				logger.debug("sleeping " + (mailInterval / 1000) + " seconds");
				Thread.sleep(mailInterval);
				if (isInterrupted()) break;
			}
		} catch (InterruptedException e) {
			logger.warn("mailing thread interrupted", e);
		}
	}

	private void sendMessage (final MessageData message) {
		try {
			Transport.send(message.getMimeMessage());
			logger.info("Sent message with subject '" + message.getSubject() + "' to "
					+ message.getRecipientAddress());
		} catch (MessagingException e) {
			logger.error("Failed to send message with subject '" + message.getSubject()
				+ "' to " + message.getRecipientAddress(), e);
		}
	}

	private MessageData createMessageData (final String recipientAddress, final String subject, final MimeMessage message) {
		MessageData md = new MessageData();
		md.setMimeMessage(message);
		md.setRecipientAddress(recipientAddress);
		md.setSubject(subject);
		return md;
	}

	/*
	 * Inspired by: http://www.tutorialspoint.com/java/java_sending_email.htm
	 */
	public void sendHtmlMail(final String recipientAddress,
			final String subject, final String htmlContent) {
		sendHtmlMail(recipientAddress, subject, htmlContent, true);
	}
	public void sendHtmlMail(final String recipientAddress,
			final String subject, final String htmlContent, final boolean useQueue) {
		Session session = Session.getDefaultInstance(System.getProperties());
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
			message.setSubject(subject);
			message.setContent(htmlContent, "text/html");
			MessageData md = createMessageData(recipientAddress, subject, message);
			if (useQueue) queue.add(md);
			else sendMessage(md);
		} catch (MessagingException mex) {
			logger.error("Failed to send message with subject '" + subject + "' to " + recipientAddress, mex);
		}
	}

	public void sendTextMail(final String recipientAddress,
			final String subject, final String textContent) {
		sendTextMail(recipientAddress, subject, textContent, true);
	}
	public void sendTextMail(final String recipientAddress,
			final String subject, final String textContent, final boolean useQueue) {
		Session session = Session.getDefaultInstance(System.getProperties());
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
			message.setSubject(subject);
			message.setText(textContent);
			MessageData md = createMessageData(recipientAddress, subject, message);
			if (useQueue) queue.add(md);
			else sendMessage(md);
		} catch (MessagingException mex) {
			logger.error("Failed to send message with subject '" + subject + "' to " + recipientAddress, mex);
		}
	}

	public void sendHtmlMailWithAttachments (final String recipientAddress,
			final String subject, final String htmlContent, final List<String> attachmentNames,
			final List<File> attachmentFiles) {
		sendHtmlMailWithAttachments(recipientAddress, subject, htmlContent, attachmentNames, attachmentFiles, true);
	}
	public void sendHtmlMailWithAttachments (final String recipientAddress,
			final String subject, final String htmlContent, final List<String> attachmentNames,
			final List<File> attachmentFiles, final boolean useQueue) {
		Session session = Session.getDefaultInstance(System.getProperties());
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
			message.setSubject(subject);
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(htmlContent, "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			for (int i = 0; i < attachmentNames.size(); i++) {
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.setFileName(attachmentNames.get(i));
				DataSource dataSource = new FileDataSource(attachmentFiles.get(i));
				messageBodyPart.setDataHandler(new DataHandler(dataSource));
				multipart.addBodyPart(messageBodyPart);
			}
			message.setContent(multipart);
			MessageData md = createMessageData(recipientAddress, subject, message);
			if (useQueue) queue.add(md);
			else sendMessage(md);
		} catch (MessagingException mex) {
			logger.error("Failed to send message with subject '" + subject + "' to " + recipientAddress, mex);
		}
	}

}
