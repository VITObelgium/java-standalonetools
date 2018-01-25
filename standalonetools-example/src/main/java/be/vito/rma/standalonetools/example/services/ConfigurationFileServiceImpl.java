package be.vito.rma.standalonetools.example.services;

import java.io.File;

import org.springframework.stereotype.Component;

import be.vito.rma.resttools.common.api.ConfigurationFileService;

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

}
