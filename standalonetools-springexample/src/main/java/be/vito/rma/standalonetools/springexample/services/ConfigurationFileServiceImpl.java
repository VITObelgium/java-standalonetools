package be.vito.rma.standalonetools.springexample.services;

import java.io.File;

import org.springframework.stereotype.Component;

import be.vito.rma.configtools.common.api.ConfigurationFileService;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
@Component
public class ConfigurationFileServiceImpl implements ConfigurationFileService {

	@Override
	public File getConfigFile() {
		return new File ("/etc/marvin/example/configuration.properties");
	}

	@Override
	public String getDefaultResourceName() {
		return "configuration";
	}

	@Override
	public boolean neverUseEnvironmentVariables() {
		return false;
	}

}
