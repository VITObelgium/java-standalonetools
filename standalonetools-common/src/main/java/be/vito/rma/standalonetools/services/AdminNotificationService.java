package be.vito.rma.standalonetools.services;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.vito.rma.resttools.common.services.ConfigurationService;
import be.vito.rma.standalonetools.HostnameTools;
import lombok.Setter;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
@Component
public class AdminNotificationService {

	private String emailAdmin;
	@Setter private String appName = null;

	private final Logger logger = LoggerFactory.getLogger(AdminNotificationService.class);

	@Autowired private Mailer mailer;
	@Autowired private ConfigurationService config;

	@PostConstruct
	private void init () {
		emailAdmin = config.getString("email.admin.to.address");
	}

	public void notifyAdmin(final String subject, final String message, final Throwable cause) {
		String fullMessage;
		if (cause != null) {
			StringWriter sw = new StringWriter();
			cause.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			fullMessage = message + "\nStack trace:\n" + exceptionAsString;
			logger.error(fullMessage, cause);
		} else {
			fullMessage = message;
			logger.error(fullMessage);
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

	public void notifyAdmin(final String subject, final String message) {
		notifyAdmin(subject, message, null);
	}

	private String getDefaultSubject () {
		return "admin notification";
	}

	public void notifyAdmin(final String message, final Throwable cause) {
		notifyAdmin(getDefaultSubject(), message, cause);
	}

	public void notifyAdmin(final String message) {
		notifyAdmin(getDefaultSubject(), message, null);
	}

}
