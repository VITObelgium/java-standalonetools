package be.vito.rma.standalonetools.spring.api;

import java.util.HashMap;
import java.util.Map;

import be.vito.rma.standalonetools.api.AbstractCommandLineAppConfiguration;
import lombok.Getter;
import lombok.Setter;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public class SpringCommandLineAppConfiguration extends AbstractCommandLineAppConfiguration {

	/**
	 * The resource name of the Spring application context XML configuration (required)
	 */
	@Getter @Setter private String springContextTemplateResourceName;

	/**
	 * Parameter key-value pairs for the Spring application context
	 * use empty map if the Spring application context is not parameterized
	 * default = empty map
	 */
	@Getter @Setter private Map<String, String> springContextParameters = new HashMap<>();


}
