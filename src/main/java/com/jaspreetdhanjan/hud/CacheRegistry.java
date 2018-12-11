package com.jaspreetdhanjan.hud;

import java.util.HashMap;
import java.util.Map;

public class CacheRegistry {
	public static final String USER = "USER";

	private static final Map<String, Object> registry = new HashMap<>();

	public static void store(String key, Object value) {
		if (key == null || value == null) {
			return;
		}

		registry.put(key, value);
	}

	public static <T> T getValue(String key) {
		return (T) registry.get(key);
	}

	public static void clear() {
		registry.clear();
	}
}