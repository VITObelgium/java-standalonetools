package be.vito.rma.standalonetools.api;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public class CommandLineAppConfiguration {

	/**
	 * The name of the app (required)
	 */
	@Getter @Setter private String appName;

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

	/**
	 * When set to true, the app will quit immediately if another instance is already running.
	 * When set to false, many instances of the app can run in parallel.
	 * default = false
	 */
	@Getter @Setter private boolean allowOnlyOneInstance = false;

	/**
	 * When set to true, the app will log a message after being started successfully
	 * When set to false, the app will start quietly
	 * default = true
	 */
	@Getter @Setter private boolean logStartedMessage = true;

}
