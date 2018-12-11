package com.jaspreetdhanjan.hud.api.javaspace;

import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.JSONReader;
import com.oracle.javafx.jmx.json.impl.JSONStreamReaderImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * This class represents the JavaSpace configuration file on-disk (under resources/config.json).
 * <p>
 * Note: any new configuration items in the JSON file must be also declared in this class.
 */

public class JavaSpaceConfig {
	private static final String CONFIG_JSON = "javaspace-config";
	private static final String HOSTNAME_JSON = "hostname";
	private static final String CONNECTION_TIME_JSON = "connection-time";

	private JSONDocument values;

	private JavaSpaceConfig(JSONDocument values) {
		this.values = values;
	}

	/**
	 * Loads and instantiates a Java representation of the JSON configuration file.
	 */
	public static JavaSpaceConfig getConfig() {
		try {
			JSONReader reader = new JSONStreamReaderImpl(new BufferedReader(new InputStreamReader(JavaSpaceConfig.class.getResourceAsStream("/config.json"))));
			JSONDocument doc = reader.build();

			for (Map.Entry config : doc.object().entrySet()) {
				if (config.getKey().equals(CONFIG_JSON)) {
					return new JavaSpaceConfig((JSONDocument) config.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getHostname() {
		return values.getString(HOSTNAME_JSON);
	}

	public long getConnectionTime() {
		return (long) values.getNumber(CONNECTION_TIME_JSON);
	}
}