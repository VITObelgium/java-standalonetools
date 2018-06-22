package be.vito.rma.standalonetools.spring.services;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.vito.rma.configtools.common.api.ConfigurationService;
import be.vito.rma.standalonetools.api.Mailer;
import be.vito.rma.standalonetools.services.DefaultMailer;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
@Component
public class SpringMailer implements Mailer {

	@Autowired private ConfigurationService config;

	private DefaultMailer mailer;

	@PostConstruct
	public void init() {
		if (config.getOptionalString(DefaultMailer.EMAIL_SMTP) != null) {
			mailer = new DefaultMailer(config);
		}
	}

	@Override
	public void sendHtmlMail(final String recipientAddress, final String subject, final String htmlContent) {
		mailer.sendHtmlMail(recipientAddress, subject, htmlContent);
	}

	@Override
	public void sendHtmlMail(final String recipientAddress, final String subject, final String htmlContent, final boolean useQueue) {
		mailer.sendHtmlMail(recipientAddress, subject, htmlContent, useQueue);
	}

	@Override
	public void sendTextMail(final String recipientAddress, final String subject, final String textContent) {
		mailer.sendTextMail(recipientAddress, subject, textContent);
	}

	@Override
	public void sendTextMail(final String recipientAddress, final String subject, final String textContent, final boolean useQueue) {
		mailer.sendTextMail(recipientAddress, subject, textContent, useQueue);
	}

	@Override
	public void sendHtmlMailWithAttachments(final String recipientAddress, final String subject, final String htmlContent,
			final List<String> attachmentNames, final List<File> attachmentFiles) {
		mailer.sendHtmlMailWithAttachments(recipientAddress, subject, htmlContent, attachmentNames, attachmentFiles);
	}

	@Override
	public void sendHtmlMailWithAttachments(final String recipientAddress, final String subject, final String htmlContent,
			final List<String> attachmentNames, final List<File> attachmentFiles, final boolean useQueue) {
		mailer.sendHtmlMailWithAttachments(recipientAddress, subject, htmlContent, attachmentNames, attachmentFiles, useQueue);
	}

}
