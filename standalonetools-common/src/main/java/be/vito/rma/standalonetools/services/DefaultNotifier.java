package be.vito.rma.standalonetools.services;

import java.io.PrintWriter;
import java.io.StringWriter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.vito.rma.configtools.common.api.ConfigurationService;
import be.vito.rma.standalonetools.tools.HostnameTools;
import be.vito.rma.standalonetools.api.Mailer;
import be.vito.rma.standalonetools.api.Notifier;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public class DefaultNotifier implements Notifier {

	private String emailAdmin;
	private String appName = null;

	private final Logger logger = LoggerFactory.getLogger(DefaultNotifier.class);

	private Mailer mailer;

	public DefaultNotifier (final String appName, final ConfigurationService config, final Mailer mailer) {
		this.appName = appName;
		this.mailer = mailer;
		emailAdmin = config.getString("email.admin.to.address");
	}

	@Override
	public void notifyAdmin(final String subject, final String message, final Throwable cause) {
		String fullMessage;
		if (cause != null) {
			StringWriter sw = new StringWriter();
			cause.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			fullMessage = message + "\nStack trace:\n" + exceptionAsString;
			logger.error(message, cause);
		} else {
			fullMessage = message;
			logger.error(message);
		}
		if (mailer == null) {
			// if notifyAdmin is called before the application context is fully loaded,
			// there is no mailer instance, so we create one here in that case
			logger.error("Failed to notify admin: mailer component not available yet.\n" + subject + "\n" + fullMessage);
		} else {
			// send admin notification mails right away: if the program exits, the Mailer thread is killed before it can send emails
			String prefix = appName == null ? "" : appName + " @ ";
			mailer.sendTextMail(emailAdmin, prefix + HostnameTools.getHostname() + ": " + subject, fullMessage, false);
		}
	}

	@Override
	public void notifyAdmin(final String subject, final String message) {
		notifyAdmin(subject, message, null);
	}

	private String getDefaultSubject () {
		return "admin notification";
	}

	@Override
	public void notifyAdmin(final String message, final Throwable cause) {
		notifyAdmin(getDefaultSubject(), message, cause);
	}

	@Override
	public void notifyAdmin(final String message) {
		notifyAdmin(getDefaultSubject(), message, null);
	}

}
