package be.vito.rma.standalonetools.example;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.vito.rma.configtools.common.api.ConfigurationFileService;
import be.vito.rma.configtools.common.api.ConfigurationService;
import be.vito.rma.standalonetools.DefaultCommandLineApp;
import be.vito.rma.standalonetools.api.DefaultCommandLineAppConfiguration;
import be.vito.rma.standalonetools.api.Mailer;
import be.vito.rma.standalonetools.api.Notifier;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public class Main {

	private final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {

		// create CommandLineApp configuration
		DefaultCommandLineAppConfiguration appConfig = new DefaultCommandLineAppConfiguration();
		appConfig.setAppName("Example app");
		appConfig.setConfigFileService(new ConfigurationFileService() {
			@Override
			public File getConfigFile() {
				return new File ("/etc/marvin/example/configuration.properties");
			}

			@Override
			public String getDefaultResourceName() {
				return "configuration";
			}
		});
		appConfig.setAllowOnlyOneInstance(true);
		appConfig.setLogStartedMessage(true);

		// create the CommandLineApp
		new DefaultCommandLineApp(appConfig, (app) -> {
			// we can get the app's name and version
			logger.info("Runnable payload starting for " + app.getAppName() + " version " + app.getAppVersion());

			// ConfigurationService, Mailer, and Notifier
			// are components provided by the skeleton
			ConfigurationService config = app.getConfig();
			Mailer mailer = app.getMailer();
			Notifier notifier = app.getNotifier();

			// we can send email messages using the Mailer component
			String recipientAddress = config.getString("email.address");
			String subject = "message from " + app.getAppName();
			String textContent = config.getString("message");
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
