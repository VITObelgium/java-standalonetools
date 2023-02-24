package be.vito.rma.standalonetools.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

import be.vito.rma.configtools.common.api.ConfigurationService;
import be.vito.rma.standalonetools.api.CommandLineApp;
import be.vito.rma.standalonetools.api.Mailer;
import be.vito.rma.standalonetools.api.Notifier;
import be.vito.rma.standalonetools.spring.api.SpringCommandLineAppRunnable;
import be.vito.rma.standalonetools.spring.api.SpringCommandLineAppConfiguration;
import be.vito.rma.standalonetools.spring.services.SpringNotifier;
import be.vito.rma.standalonetools.spring.tools.SpringContextTools;
import be.vito.rma.standalonetools.tools.AppTools;
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
public class SpringCommandLineApp implements CommandLineApp {

	private final Logger logger = LoggerFactory.getLogger(SpringCommandLineApp.class);

	public SpringCommandLineApp (final SpringCommandLineAppConfiguration appConfig, final SpringCommandLineAppRunnable runnable) {
		// validation
		if (appConfig.getAppName() == null) throw new RuntimeException ("CommandLineAppConfiguration.appName is required");
		if (appConfig.getSpringContextTemplateResourceName() == null)
			throw new RuntimeException("SpringCommandLineAppConfiguration.springContextTemplateResourceName is required");

		this.appName = appConfig.getAppName();

		// load application context
		SpringNotifier.setAppName(getAppName());	// need to set this _before_ loading the Spring application context
		applicationContext = SpringContextTools.getAppContext(this,
				appConfig.getSpringContextTemplateResourceName(), appConfig.getSpringContextParameters());
		config = getApplicationContext().getBean(ConfigurationService.class);
		mailer = getApplicationContext().getBean(Mailer.class);
		notifier = getApplicationContext().getBean(Notifier.class);

		AppTools.initApp(this, appConfig, logger);

		// run the payload code
		runnable.run(this);

	}

	@Getter private ConfigurableApplicationContext applicationContext = null;

	// CommandLineApp interface

	@Getter private final String appName;

	@Override
	public String getAppVersion () {
		return config.getVersion();
	}

	@Getter private final ConfigurationService config;

	@Getter private final Mailer mailer;

	@Getter private final Notifier notifier;

	@Override
	public void close () {
		getApplicationContext().close();
	}

}
