package be.vito.rma.standalonetools.handlers;

import java.lang.Thread.UncaughtExceptionHandler;
import be.vito.rma.standalonetools.tools.HostnameTools;
import be.vito.rma.standalonetools.api.CommandLineApp;
import be.vito.rma.standalonetools.api.Notifier;

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
	private Notifier notifier;

	public DefaultUncaughtExceptionHandler(final CommandLineApp app) {
		this.app = app;
		notifier = app.getNotifier();
	}

	@Override
	public void uncaughtException(final Thread t, final Throwable e) {
		String subject = "Unexpected error: crashed!";
		String message = app.getAppName() + " @ " + HostnameTools.getHostname() + " got unexpected error and crashed!";
		notifier.notifyAdmin(subject, message, e);
		app.close();
        System.exit(1);
	}

}
