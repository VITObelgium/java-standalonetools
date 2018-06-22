package be.vito.rma.standalonetools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.vito.rma.configtools.common.api.ConfigurationService;
import be.vito.rma.configtools.common.services.DefaultConfigurationService;
import be.vito.rma.standalonetools.api.CommandLineApp;
import be.vito.rma.standalonetools.api.CommandLineAppRunnable;
import be.vito.rma.standalonetools.api.DefaultCommandLineAppConfiguration;
import be.vito.rma.standalonetools.api.Mailer;
import be.vito.rma.standalonetools.api.Notifier;
import be.vito.rma.standalonetools.services.DefaultMailer;
import be.vito.rma.standalonetools.services.DefaultNotifier;
import be.vito.rma.standalonetools.tools.AppTools;
import lombok.Getter;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public class DefaultCommandLineApp implements CommandLineApp {

	private final Logger logger = LoggerFactory.getLogger(DefaultCommandLineApp.class);

	public DefaultCommandLineApp (final DefaultCommandLineAppConfiguration appConfig, final CommandLineAppRunnable runnable) {
		// validation
		if (appConfig.getAppName() == null) throw new RuntimeException ("CommandLineAppConfiguration.appName is required");
		if (appConfig.getConfigFileService() == null)
			throw new RuntimeException("DefaultCommandLineAppConfiguration.configFileService is required");

		this.appName = appConfig.getAppName();

		// initialize components
		config = new DefaultConfigurationService(appConfig.getConfigFileService());
		if (config.getOptionalString(DefaultMailer.EMAIL_SMTP) != null) {
			mailer = new DefaultMailer(config);
			notifier = new DefaultNotifier(appName, config, mailer);
		}

		AppTools.initApp(this, appConfig, logger);

		// run the payload code
		runnable.run(this);

	}

	// CommandLineApp interface

	@Getter private String appName;

	@Override
	public String getAppVersion() {
		return config.getVersion();
	}

	@Getter ConfigurationService config;

	@Getter Mailer mailer;

	@Getter Notifier notifier;

	@Override
	public void close() {
		// nothing to do here
	}

}
