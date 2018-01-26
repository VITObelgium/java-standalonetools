package be.vito.rma.standalonetools.api;

import be.vito.rma.resttools.common.api.ConfigurationFileService;
import lombok.Getter;
import lombok.Setter;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public class DefaultCommandLineAppConfiguration extends AbstractCommandLineAppConfiguration {

	/**
	 * the configuration file service to use (required)
	 */
	@Getter @Setter private ConfigurationFileService configFileService;

}
