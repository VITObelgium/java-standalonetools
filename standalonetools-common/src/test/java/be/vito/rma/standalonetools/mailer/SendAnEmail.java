package be.vito.rma.standalonetools.mailer;

import java.io.File;

import be.vito.rma.configtools.common.api.ConfigurationFileService;
import be.vito.rma.configtools.common.api.ConfigurationService;
import be.vito.rma.configtools.common.services.DefaultConfigurationService;
import be.vito.rma.standalonetools.api.Mailer;
import be.vito.rma.standalonetools.services.DefaultMailer;

/**
 * @author (c) 2021 Stijn.VanLooy@vito.be
 *
 */
public class SendAnEmail {

	public static void main(final String[] args) throws InterruptedException {

		final int HTML = 0;
		final int TEXT = 1;

		final String recipientAddress = "someone@vito.local";
		final String subject = "mailer test Vážený";
		final String content = "Vážený";
		final int mode = HTML;


		final ConfigurationService config = new DefaultConfigurationService(new ConfigurationFileService() {

			@Override
			public String getDefaultResourceName() {
				return "mailertestConfig";
			}

			@Override
			public File getConfigFile() {
				return null;
			}

			@Override
			public boolean neverUseEnvironmentVariables() {
				return true;
			}
		});

		final Mailer mailer = new DefaultMailer(config);

		if (mode == TEXT)
			mailer.sendTextMail(recipientAddress, subject + " (text mode)", content);
		else
			mailer.sendHtmlMail(recipientAddress, subject + " (html mode)", content);

		// wait a bit (make sure all mails are sent)
		Thread.sleep(10 * 1000);
		// and exit
		System.exit(0);

	}

}
