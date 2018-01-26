package be.vito.rma.standalonetools.api;

import be.vito.rma.resttools.common.services.ConfigurationService;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public interface CommandLineApp {

	public String getAppName();

	public String getAppVersion();

	public ConfigurationService getConfig();

	public Mailer getMailer();

	public Notifier getNotifier();

	public void close();

}
