package com.jaspreetdhanjan.hud.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InputValidatorTest {
	@Test
	public void testFunctions() {
		String[] inputs = {"Hello", "This", "is", "9999", "", "Test"};
		assertTrue("Should return true as index 4 is empty", InputValidator.isAnyEmpty(inputs));
		String[] inputs2 = {"Hello", "This", "is", "9999", "Test"};
		assertFalse("Should return true as index 4 is empty", InputValidator.isAnyEmpty(inputs2));

		String email = "u1551123@unimail.hud.ac.uk";
		String email2 = "crapsadshgfbf";
		assertTrue("Should return true as email is valid", InputValidator.isValidEmail(email));
		assertFalse("Should return true as email is valid", InputValidator.isValidEmail(email2));

		String goodPassword = "1aB!jasprFh24@eet";
		String badPassword = "bad";
		assertTrue("Should return true as good password", InputValidator.isValidPassword(goodPassword));
		assertFalse("Should return true as good password", InputValidator.isValidPassword(badPassword));
	}
}
