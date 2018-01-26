package be.vito.rma.standalonetools.api;

import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public interface CommandLineApp {

	public String getAppName();

	public String getAppVersion();

	public ConfigurableApplicationContext getApplicationContext();

}
