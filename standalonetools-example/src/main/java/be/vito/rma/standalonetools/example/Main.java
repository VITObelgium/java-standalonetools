package be.vito.rma.standalonetools.example;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import be.vito.rma.resttools.common.services.ConfigurationService;
import be.vito.rma.standalonetools.api.CommandLineAppConfiguration;
import be.vito.rma.standalonetools.example.services.ConfigurationFileServiceImpl;
import be.vito.rma.standalonetools.example.services.HelloService;
import be.vito.rma.standalonetools.impl.DefaultCommandLineApp;
import be.vito.rma.standalonetools.services.AdminNotificationService;
import be.vito.rma.standalonetools.services.Mailer;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public class Main {

	private final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {

		// temporarily load the configuration service to get the Spring context parameters
		ConfigurationService staticConfig = new ConfigurationService(new ConfigurationFileServiceImpl());

		// create CommandLineApp configuration
		CommandLineAppConfiguration appConfig = new CommandLineAppConfiguration();
		appConfig.setAppName("Example app");
		appConfig.setSpringContextTemplateResourceName("/exampleContextTemplate.xml");
		// you would never put this in a configuration file, but we needed an example
		appConfig.getSpringContextParameters().put("PACKAGE", staticConfig.getString("service.package"));
		appConfig.setAllowOnlyOneInstance(true);
		appConfig.setLogStartedMessage(true);

		// create the CommandLineApp
		new DefaultCommandLineApp(appConfig, (app) -> {
			// we can get the app's name and version
			logger.info("Runnable payload starting for " + app.getAppName() + " version " + app.getAppVersion());

			// we can get the application context and load Spring beans from it
			ApplicationContext appCtx = app.getApplicationContext();

			// ConfigurationService, Mailer, and AdminNotificationService
			// are services provided by the skeleton
			ConfigurationService config = appCtx.getBean(ConfigurationService.class);
			Mailer mailer = appCtx.getBean(Mailer.class);
			AdminNotificationService adminNotificationService = appCtx.getBean(AdminNotificationService.class);

			// HelloService is an example custom service
			HelloService service = appCtx.getBean(HelloService.class);

			// we can send email messages using the Mailer component
			String recipientAddress = config.getString("email.address");
			String subject = "message from " + app.getAppName();
			String textContent = service.getMessage();
			mailer.sendTextMail(recipientAddress, subject, textContent);

			// we can notify admin using the AdminNotificationService component
			subject = app.getAppName() + " stopped!";
			String message = app.getAppName() + " version " + app.getAppVersion() + " stopped @ " + new Date();
			adminNotificationService.notifyAdmin(subject, message);

			// we need to explicitly exit the app (the Mailer component has a separate thread running forever)
			System.exit(0);
		});
	}

}
