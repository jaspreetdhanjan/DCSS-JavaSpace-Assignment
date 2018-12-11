package com.jaspreetdhanjan.hud.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Provides string encryption using PBKDF2 salted hashing.
 * Strings are encrypted in three components: Salt, Iteration and Hash
 * These three components are separated by two colons (:).
 * <p>
 * This source code is not mine. It has been adapted from JavaPasswordSecurity tutorial
 * (https://gist.github.com/jtan189/3804290) credit to GitHub user jtan189
 */

public class Encryption {
	private static final String ENCRYPTION_ALGORITHM = "PBKDF2WithHmacSHA512";

	private static final int BYTES_IN_SALT = 512;
	private static final int BYTES_IN_HASH = 512;
	private static final int ITERATIONS = 5000;

	private static final int SALT_INDEX = 0;
	private static final int ITERATION_INDEX = 1;
	private static final int HASH_INDEX = 2;

	private Encryption() {
	}

	public static String encryptString(String string) throws InvalidKeySpecException, NoSuchAlgorithmException {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[BYTES_IN_SALT];
		random.nextBytes(salt);

		byte[] hash = createHash(string.toCharArray(), salt, ITERATIONS, BYTES_IN_HASH);

		return toHex(salt) + ":" + ITERATIONS + ":" + toHex(hash);
	}

	private static byte[] createHash(char[] chars, byte[] salt, int iterations, int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PBEKeySpec keySpec = new PBEKeySpec(chars, salt, iterations, bytes * 8);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);

		return secretKeyFactory.generateSecret(keySpec).getEncoded();
	}

	public static boolean validateString(String string, String encryptedString) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String[] params = encryptedString.split(":");

		if (params.length != 3) {
			throw new RuntimeException("Invalid string encryption shape");
		}

		int iterations = Integer.parseInt(params[ITERATION_INDEX]);
		byte[] salt = fromHex(params[SALT_INDEX]);
		byte[] hash = fromHex(params[HASH_INDEX]);

		byte[] testHash = createHash(string.toCharArray(), salt, iterations, hash.length);

		return slowEquals(hash, testHash);
	}

	private static boolean slowEquals(byte[] a, byte[] b) {
		int diff = a.length ^ b.length;
		for (int i = 0; i < a.length && i < b.length; i++) {
			diff |= a[i] ^ b[i];
		}
		return diff == 0;
	}

	private static byte[] fromHex(String hex) {
		byte[] binary = new byte[hex.length() / 2];
		for (int i = 0; i < binary.length; i++) {
			binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return binary;
	}

	private static String toHex(byte[] array) {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);

		int paddingLength = (array.length * 2) - hex.length();

		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}
}