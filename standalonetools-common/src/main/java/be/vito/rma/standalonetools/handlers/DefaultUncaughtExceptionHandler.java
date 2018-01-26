package be.vito.rma.standalonetools.handlers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.vito.rma.standalonetools.HostnameTools;
import be.vito.rma.standalonetools.api.CommandLineApp;
import be.vito.rma.standalonetools.services.AdminNotificationService;

/**
 *
 * An UncaughtExceptionHandler that logs to the app's logger,
 * sends a notification message, and exits upon an unexpected error.
 *
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public class DefaultUncaughtExceptionHandler implements UncaughtExceptionHandler {

	private CommandLineApp app;
	private AdminNotificationService notificationService;
	private Logger logger;

	public DefaultUncaughtExceptionHandler(final CommandLineApp app, final Logger logger) {
		this.app = app;
		if (logger != null)
			this.logger = logger;
		else
			this.logger = LoggerFactory.getLogger(DefaultUncaughtExceptionHandler.class);
		notificationService = app.getApplicationContext().getBean(AdminNotificationService.class);
	}

	@Override
	public void uncaughtException(final Thread t, final Throwable e) {
		logger.error("Unexpected error!", e);
        String subject = app.getAppName() + " @ " + HostnameTools.getHostname() + " got unexpected error and exited!";
        String message = subject;
        // include stacktrace into notification message
		if (e != null) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			message = message + "\nStack trace:\n" + exceptionAsString;
		}
        notificationService.notifyAdmin(subject, message);
        if (app.getApplicationContext() != null)
        	app.getApplicationContext().close();
        System.exit(1);
	}

}
