package be.vito.rma.standalonetools.tools;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;

import be.vito.rma.standalonetools.api.CommandLineApp;
import be.vito.rma.standalonetools.api.CommandLineAppConfiguration;
import be.vito.rma.standalonetools.handlers.DefaultUncaughtExceptionHandler;
import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public class AppTools {

	private AppTools() {}	// no need to create instances of this utility class

	public static void initApp (final CommandLineApp app, final CommandLineAppConfiguration appConfig, final Logger logger) {
		// default uncaught exception handler
		UncaughtExceptionHandler handler = appConfig.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(handler == null ? new DefaultUncaughtExceptionHandler(app) : handler);

		if (appConfig.isAllowOnlyOneInstance()) {
			// Only allow 1 instance to run
			try {
				JUnique.acquireLock(app.getAppName() + "-" + HostnameTools.getHostname());
			} catch (AlreadyLockedException e) {
				logger.debug("Another instance of '" + app.getAppName() + "' is already running, exiting");
				System.exit(1);
			}
		}

		if (appConfig.isLogStartedMessage())
			logger.info(app.getAppName() + " version " + app.getAppVersion() + " started");

	}

}
