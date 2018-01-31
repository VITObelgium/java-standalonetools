package be.vito.rma.standalonetools.mailer;

import java.io.File;

import be.vito.rma.configtools.common.api.ConfigurationFileService;
import be.vito.rma.configtools.common.api.ConfigurationService;
import be.vito.rma.configtools.common.services.DefaultConfigurationService;
import be.vito.rma.standalonetools.api.Mailer;
import be.vito.rma.standalonetools.services.DefaultMailer;

/**
 * @author (c) 2016-2018 Stijn.VanLooy@vito.be
 *
 */
public class MailerTest {

	public static void main (final String [] args) throws InterruptedException {

		ConfigurationService config = new DefaultConfigurationService(new ConfigurationFileService() {

			@Override
			public String getDefaultResourceName() {
				return "mailertestConfig";
			}

			@Override
			public File getConfigFile() {
				return null;
			}
		});

		Mailer mailer = new DefaultMailer(config);

		for (int i = 0; i < 3; i++) {
			MailingThread thread = new MailingThread("mailer " + i, mailer);
			thread.start();
		}

		// wait until all mails sent
		Thread.sleep(10 * 5 * 1000);
		// and exit
		System.exit(0);

	}

}

class MailingThread extends Thread {

	private Mailer mailer;
	private String name;
	private int count = 3;
	private String recipientAddress = "someone@vito.be";

	public MailingThread(final String name, final Mailer mailer) {
		super();
		this.name = name;
		this.mailer = mailer;
	}

	@Override
	public void run () {
		for (int i = 0; i < count; i++) {
			String message = "message " + i + " from " + name;
			mailer.sendTextMail(recipientAddress, message, message);
		}
	}

}
