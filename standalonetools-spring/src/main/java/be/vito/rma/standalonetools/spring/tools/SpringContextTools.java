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
	 * 		<property name="url" value="$URL$"/>
	 * will be replaced by
	 * 		<property name="url" value="http://my.service.url"/>
	 * @param app
	 * @param templateResourceName
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public static ConfigurableApplicationContext getAppContext (final CommandLineApp app, final String templateResourceName, final Map<String, String> parameters) {
		try {
			File contextFile = createContextFile(app.getAppName(), templateResourceName, parameters);
			// load application context from context file
			String filename = contextFile.getAbsolutePath();
			/*
			 * In linux, Spring cuts off the first / in the absolute path
			 */
			if (File.separator.equals("/"))
				filename = File.separator + filename;
			return new FileSystemXmlApplicationContext(filename);
		} catch (IOException e) {
			String message = "Failed creating application context";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	private static File createContextFile (final String appName, final String resourceName, final Map<String, String> parameters) throws IOException {
		File out = File.createTempFile(appName + "-", "-ctx.xml");
		out.deleteOnExit();
		InputStream is = resourceName.getClass().getResourceAsStream(resourceName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		PrintWriter writer = new PrintWriter(out);
		String line = null;
		while ((line = reader.readLine()) != null) {
			for (Entry<String, String> entry : parameters.entrySet()) {
				String key = "$" + entry.getKey().toUpperCase() + "$";
				String value = entry.getValue();
				line = line.replace(key, value);
			}
			writer.println(line);
		}
		writer.close();
		reader.close();
		return out;
	}

}
