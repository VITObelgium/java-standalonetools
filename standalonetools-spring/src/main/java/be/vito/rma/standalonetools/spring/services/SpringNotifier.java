package be.vito.rma.standalonetools.spring.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.vito.rma.resttools.common.services.ConfigurationService;
import be.vito.rma.standalonetools.api.Mailer;
import be.vito.rma.standalonetools.api.Notifier;
import be.vito.rma.standalonetools.services.DefaultNotifier;
import lombok.Setter;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
@Component
public class SpringNotifier implements Notifier {

	@Setter private static String appName = null;
	private Notifier notifier;

	@Autowired private ConfigurationService config;
	@Autowired private Mailer mailer;

	@PostConstruct
	public void init() {
		notifier = new DefaultNotifier(appName, config, mailer);
	}

	@Override
	public void notifyAdmin(final String subject, final String message, final Throwable cause) {
		notifier.notifyAdmin(subject, message, cause);
	}

	@Override
	public void notifyAdmin(final String subject, final String message) {
		notifier.notifyAdmin(subject, message);
	}

	@Override
	public void notifyAdmin(final String message, final Throwable cause) {
		notifier.notifyAdmin(message, cause);
	}

	@Override
	public void notifyAdmin(final String message) {
		notifier.notifyAdmin(message);
	}

}
