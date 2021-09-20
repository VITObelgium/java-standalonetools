package be.vito.rma.standalonetools.tools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author (c) 2020 Stijn.VanLooy@vito.be
 *
 */
public class ArgsTools {

	public static Map<String, String> parseKeyValuePairs (final String [] args, final int startIndex,
			final String commandName, final Supplier<Integer> helpPrinter, final String... validKeysArray) {
		final List<String> validKeys = Arrays.asList(validKeysArray);
		final Map<String, String> out = new HashMap<>();
		int i = startIndex;
		while (i < args.length) {
			final String [] tokens = args[i].split("=", 2);
			if (tokens.length != 2) {
				System.out.println("Invalid argument for the " + commandName + " command: not a key=value pair: " + args[i]);
				helpPrinter.get();
				return null;
			}
			if (!validKeys.contains(tokens[0].toLowerCase())) {
				System.out.println("Invalid argument key for the " + commandName + " command: " + tokens[0]);
				helpPrinter.get();
				return null;
			}
			out.put(tokens[0].toLowerCase(), tokens[1]);
			i++;
		}
		return out;
	}

	public static String getValue (final Map<String, String> keyValueMap, final String key, final String defaultValue) {
		final String value = keyValueMap.get(key);
		if (value == null)
			return defaultValue;
		else
			return value;
	}

}
