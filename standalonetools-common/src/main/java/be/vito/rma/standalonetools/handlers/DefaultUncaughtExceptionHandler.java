package be.vito.rma.standalonetools.handlers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.vito.rma.standalonetools.api.CommandLineApp;

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
	private Logger logger;

	public DefaultUncaughtExceptionHandler(final CommandLineApp app, final Logger logger) {
		this.app = app;
		if (logger != null)
			this.logger = logger;
		else
			this.logger = LoggerFactory.getLogger(DefaultUncaughtExceptionHandler.class);
	}

	@Override
	public void uncaughtException(final Thread t, final Throwable e) {
		logger.error("Unexpected error!", e);
		String hostname = null;
		try {
			hostname = java.net.InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e1) {
			logger.warn("Could not determine fully qualified hostname", e1);
		}
        String subject = app.getAppName() + " @ " + hostname + " got unexpected error and exited!";
        String message = subject;
        // include stacktrace into notification message
		if (e != null) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			message = message + "\nStack trace:\n" + exceptionAsString;
		}
        app.notifyAdmin(subject, message);
        if (app.getApplicationContext() != null)
        	app.getApplicationContext().close();
        System.exit(1);
	}

}
