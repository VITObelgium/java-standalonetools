package be.vito.rma.standalonetools.spring.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import be.vito.rma.standalonetools.api.CommandLineApp;

/**
 * @author (c) 2016 Stijn.VanLooy@vito.be
 *
 */
public class SpringContextTools {

	private SpringContextTools() {}	// no need to create instances of this utility class

	private final static Logger logger = LoggerFactory.getLogger(SpringContextTools.class);

	/**
	 * Creates a Spring application context based upon a template.
	 * The template is a parameterized Spring XML application context configuration.
	 * It must be available as a resource with the given name.
	 * The parameters map maps keys on values.
	 * To use a parameter, put the key in the template in between $ chars.
	 * This method will replace those occurrences with the corresponding parameter value.
	 * For example:
	 * if a parameter URL = http://my.service.url is given,
	 * 		property name="url" value="$URL$"
	 * will be replaced by
	 * 		property name="url" value="http://my.service.url"
	 * @param app app to create the spring application context for
	 * @param templateResourceName resource name for the spring context template
	 * @param parameters parameters and their values for use in the template
	 * @return the spring application context
	 */
	public static ConfigurableApplicationContext getAppContext (final CommandLineApp app, final String templateResourceName, final Map<String, String> parameters) {
		try {
			final File contextFile = createContextFile(app.getAppName(), templateResourceName, parameters);
			// load application context from context file
			String filename = contextFile.getAbsolutePath();
			/*
			 * In linux, Spring cuts off the first / in the absolute path
			 */
			if (File.separator.equals("/"))
				filename = File.separator + filename;
			return new FileSystemXmlApplicationContext(filename);
		} catch (final IOException e) {
			final String message = "Failed creating application context";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	private static File createContextFile (final String appName, final String resourceName, final Map<String, String> parameters) throws IOException {
		final File out = File.createTempFile(appName + "-", "-ctx.xml");
		out.deleteOnExit();
		final InputStream is = SpringContextTools.class.getResourceAsStream(resourceName);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		final PrintWriter writer = new PrintWriter(out);
		String line = null;
		while ((line = reader.readLine()) != null) {
			for (final Entry<String, String> entry : parameters.entrySet()) {
				final String key = "$" + entry.getKey().toUpperCase() + "$";
				final String value = entry.getValue();
				line = line.replace(key, value);
			}
			writer.println(line);
		}
		writer.close();
		reader.close();
		return out;
	}

}
