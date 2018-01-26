package be.vito.rma.standalonetools.springexample.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.vito.rma.resttools.common.services.ConfigurationService;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
@Component
public class HelloService {

	@Autowired private ConfigurationService config;

	public String getMessage() {
		return config.getString("message");
	}

}
