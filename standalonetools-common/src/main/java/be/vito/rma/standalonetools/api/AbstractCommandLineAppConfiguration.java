package be.vito.rma.standalonetools.api;

import java.lang.Thread.UncaughtExceptionHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public abstract class AbstractCommandLineAppConfiguration implements CommandLineAppConfiguration {

	@Getter @Setter private String appName;

	@Getter @Setter private boolean allowOnlyOneInstance = false;

	@Getter @Setter private boolean logStartedMessage = true;

	@Getter @Setter private UncaughtExceptionHandler defaultUncaughtExceptionHandler = null;

}
