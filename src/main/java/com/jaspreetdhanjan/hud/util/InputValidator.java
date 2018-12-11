package com.jaspreetdhanjan.hud.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class with methods used to validate input on the UI.
 */

public class InputValidator {
	private static final String VALID_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
	private static final Pattern VALID_PASSWORD_REGEX = Pattern.compile(VALID_PASSWORD);

	private InputValidator() {
	}

	public static boolean isAnyEmpty(String... inputs) {
		for (String input : inputs) {
			if (input.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Regex retrieved from: https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
	 * <p>
	 * ^                 # start-of-string
	 * (?=.*[0-9])       # a digit must occur at least once
	 * (?=.*[a-z])       # a lower case letter must occur at least once
	 * (?=.*[A-Z])       # an upper case letter must occur at least once
	 * (?=.*[@#$%^&+=])  # a special character must occur at least once
	 * (?=\S+$)          # no whitespace allowed in the entire string
	 * .{8,}             # anything, at least eight places though
	 * $                 # end-of-string
	 */
	public static boolean isValidPassword(String password) {
		return password.matches(VALID_PASSWORD);
	}

	public static boolean isValidEmail(String email) {
		return email.contains("@") && email.contains(".");
	}
}