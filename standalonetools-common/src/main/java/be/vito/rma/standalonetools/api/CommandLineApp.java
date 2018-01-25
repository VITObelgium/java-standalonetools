package be.vito.rma.standalonetools.api;

import org.springframework.context.ConfigurableApplicationContext;

import be.vito.rma.resttools.common.services.ConfigurationService;
import be.vito.rma.standalonetools.services.Mailer;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public interface CommandLineApp {

	public String getAppName();

	public String getAppVersion();

	public ConfigurationService getConfig();

	public Mailer getMailer();

	public ConfigurableApplicationContext getApplicationContext();

	public void notifyAdmin (String subject, String message, Throwable cause);

	public void notifyAdmin (String subject, String message);

	public void notifyAdmin (String message, Throwable cause);

	public void notifyAdmin (String message);

}
