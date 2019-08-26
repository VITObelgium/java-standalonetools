package be.vito.rma.standalonetools.api;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public interface CommandLineAppConfiguration {

	/**
	 * The name of the app (required)
	 */
	public String getAppName();

	/**
	 * When set to true, the app will quit immediately if another instance is already running.
	 * When set to false, many instances of the app can run in parallel.
	 * default = false
	 */
	public boolean isAllowOnlyOneInstance();

	/**
	 * When set to true, the app will log a message after being started successfully
	 * When set to false, the app will start quietly
	 * default = true
	 */
	public boolean isLogStartedMessage();

	/**
	 * (optional)
	 * When set, this UncaughtExceptionHandler will be used for uncaught exceptions.
	 * When not set, a default implementation will be used which sends a notification message
	 * using the app's notifier.
	 * @return
	 */
	public UncaughtExceptionHandler getDefaultUncaughtExceptionHandler();

}
