package be.vito.rma.standalonetools.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

import be.vito.rma.resttools.common.services.ConfigurationService;
import be.vito.rma.standalonetools.api.CommandLineApp;
import be.vito.rma.standalonetools.api.CommandLineAppConfiguration;
import be.vito.rma.standalonetools.api.CommandLineAppRunnable;
import be.vito.rma.standalonetools.handlers.DefaultUncaughtExceptionHandler;
import be.vito.rma.standalonetools.services.AdminNotificationService;
import be.vito.rma.standalonetools.HostnameTools;
import be.vito.rma.standalonetools.SpringContextTools;
import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import lombok.Getter;

/**
 *
 * Usage:
 * . first use the constructor to create a DefaultCommandLineApp instance
 * . then configure the app using the setters (optional)
 * . then execute the runnable payload in the app with the start(CommandLineAppRunnable runnable) method
 *
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public class DefaultCommandLineApp implements CommandLineApp {

	private final Logger logger = LoggerFactory.getLogger(DefaultCommandLineApp.class);
	private ConfigurationService config;

	/**
	 *
	 * @param appName
	 * @param springContextTemplateResourceName the resource name of the Spring application context XML configuration
	 * @param springContextParameters parameter key-value pairs, use empty list if the Spring application context is not parameterized
	 */
	public DefaultCommandLineApp (final CommandLineAppConfiguration appConfig, final CommandLineAppRunnable runnable) {
		// validation
		if (appConfig.getAppName() == null) throw new RuntimeException ("CommandLineAppConfiguration.appName is required");
		if (appConfig.getSpringContextTemplateResourceName() == null)
			throw new RuntimeException("CommandLineAppConfiguration.springContextTemplateResourceName is required");

		this.appName = appConfig.getAppName();

		// load application context
		applicationContext = SpringContextTools.getAppContext(this,
				appConfig.getSpringContextTemplateResourceName(), appConfig.getSpringContextParameters());
		config = getApplicationContext().getBean(ConfigurationService.class);
		getApplicationContext().getBean(AdminNotificationService.class).setAppName(getAppName());

		// default uncaught exception handler
		Thread.setDefaultUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(this, logger));

		if (appConfig.isAllowOnlyOneInstance()) {
			// Only allow 1 instance to run
			try {
				JUnique.acquireLock(getAppName() + "-" + HostnameTools.getHostname());
			} catch (AlreadyLockedException e) {
				logger.debug("Another instance of '" + getAppName() + "' is already running, exiting");
				System.exit(1);
			}
		}

		if (appConfig.isLogStartedMessage())
			logger.info(getAppName() + " version " + getAppVersion() + " started");

		// run the payload code
		runnable.run(this);

	}

	// CommandLineApp interface

	@Getter private String appName;

	@Override
	public String getAppVersion () {
		return config.getVersion();
	}

	@Getter private ConfigurableApplicationContext applicationContext = null;

}
