package be.vito.rma.standalonetools.springexample;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import be.vito.rma.configtools.common.api.ConfigurationService;
import be.vito.rma.configtools.common.services.DefaultConfigurationService;
import be.vito.rma.standalonetools.api.Mailer;
import be.vito.rma.standalonetools.api.Notifier;
import be.vito.rma.standalonetools.spring.SpringCommandLineApp;
import be.vito.rma.standalonetools.spring.api.SpringCommandLineAppConfiguration;
import be.vito.rma.standalonetools.springexample.services.ConfigurationFileServiceImpl;
import be.vito.rma.standalonetools.springexample.services.HelloService;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public class Main {

	private final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {

		// temporarily load the configuration service to get the Spring context parameters
		ConfigurationService staticConfig = new DefaultConfigurationService(new ConfigurationFileServiceImpl());

		// create CommandLineApp configuration
		SpringCommandLineAppConfiguration appConfig = new SpringCommandLineAppConfiguration();
		appConfig.setAppName("Example Spring app");
		appConfig.setSpringContextTemplateResourceName("/exampleContextTemplate.xml");
		// you would never put this in a configuration file, but we needed an example
		appConfig.getSpringContextParameters().put("PACKAGE", staticConfig.getString("service.package"));
		appConfig.setAllowOnlyOneInstance(true);
		appConfig.setLogStartedMessage(true);

		// create the CommandLineApp
		new SpringCommandLineApp(appConfig, (app) -> {
			// we can get the app's name and version
			logger.info("Runnable payload starting for " + app.getAppName() + " version " + app.getAppVersion());

			// ConfigurationService, Mailer, and Notifier
			// are components provided by the skeleton
			ConfigurationService config = app.getConfig();
			Mailer mailer = app.getMailer();
			Notifier notifier = app.getNotifier();

			// we can get the application context and load Spring beans from it
			ApplicationContext appCtx = app.getApplicationContext();
			HelloService service = appCtx.getBean(HelloService.class);

			// we can send email messages using the Mailer component
			String recipientAddress = config.getString("email.address");
			String subject = "message from " + app.getAppName();
			String textContent = service.getMessage();
			mailer.sendTextMail(recipientAddress, subject, textContent);

			// we can notify admin using the Notifier component
			subject = app.getAppName() + " stopped!";
			String message = app.getAppName() + " version " + app.getAppVersion() + " stopped @ " + new Date();
			notifier.notifyAdmin(subject, message);

			// we need to explicitly exit the app (the Mailer component has a separate thread running forever)
			System.exit(0);
		});
	}

}
