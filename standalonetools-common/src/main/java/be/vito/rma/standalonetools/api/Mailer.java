package be.vito.rma.standalonetools.api;

import java.io.File;
import java.util.List;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public interface Mailer {

	/**
	 * Send HTML formatted email using the message queue.
	 * @param recipientAddress
	 * @param subject
	 * @param htmlContent
	 */
	public void sendHtmlMail(final String recipientAddress,
			final String subject, final String htmlContent);

	/**
	 * Send HTML formatted email
	 * Optionally queue the message before sending.
	 * Sending all messages immediately (instead of queuing them first),
	 * possibly results in flooding the email server.
	 * Therefore it is recommended to always use message queuing.
	 * @param recipientAddress send email to this address
	 * @param subject use this subject
	 * @param htmlContent HTML formatted content
	 * @param useQueue if set to true, queue message; if set to false, send message immediately
	 */
	public void sendHtmlMail(final String recipientAddress,
			final String subject, final String htmlContent, final boolean useQueue);

	/**
	 * Send plain text email using the message queue.
	 * @param recipientAddress
	 * @param subject
	 * @param textContent
	 */
	public void sendTextMail(final String recipientAddress,
			final String subject, final String textContent);

	/**
	 * Send plain text email
	 * Optionally queue the message before sending.
	 * Sending all messages immediately (instead of queuing them first),
	 * possibly results in flooding the email server.
	 * Therefore it is recommended to always use message queuing.
	 * @param recipientAddress send email to this address
	 * @param subject use this subject
	 * @param textContent plain text content
	 * @param useQueue if set to true, queue message; if set to false, send message immediately
	 */
	public void sendTextMail(final String recipientAddress,
			final String subject, final String textContent, final boolean useQueue);

	/**
	 * Send HTML formatted email with file attachments using the message queue.
	 * @param recipientAddress
	 * @param subject
	 * @param htmlContent
	 * @param attachmentNames
	 * @param attachmentFiles
	 */
	public void sendHtmlMailWithAttachments (final String recipientAddress,
			final String subject, final String htmlContent, final List<String> attachmentNames,
			final List<File> attachmentFiles);

	/**
	 * Send HTML formatted email with file attachments
	 * Optionally queue the message before sending.
	 * Sending all messages immediately (instead of queuing them first),
	 * possibly results in flooding the email server.
	 * Therefore it is recommended to always use message queuing.
	 * @param recipientAddress send email to this address
	 * @param subject use this subject
	 * @param htmlContent HTML formatted content
	 * @param attachmentNames names of the attachments
	 * @param attachmentFiles files to attach (in the same order as the names list)
	 * @param useQueue if set to true, queue message; if set to false, send message immediately
	 */
	public void sendHtmlMailWithAttachments (final String recipientAddress,
			final String subject, final String htmlContent, final List<String> attachmentNames,
			final List<File> attachmentFiles, final boolean useQueue);

}
