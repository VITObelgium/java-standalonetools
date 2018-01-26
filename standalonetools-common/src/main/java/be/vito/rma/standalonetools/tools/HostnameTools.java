package be.vito.rma.standalonetools.tools;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public class HostnameTools {

	private HostnameTools() {}	// no need to create instances of this utility class

	private final static Logger logger = LoggerFactory.getLogger(HostnameTools.class);

	public static String getHostname() {
		try {
			return java.net.InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
			logger.warn("Could not determine fully qualified hostname", e);
			return "localhost";
		}
	}

}
