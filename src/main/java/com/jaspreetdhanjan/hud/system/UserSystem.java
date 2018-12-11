package com.jaspreetdhanjan.hud.system;

import com.jaspreetdhanjan.hud.api.BulletinApi;
import com.jaspreetdhanjan.hud.api.BulletinImpl;
import com.jaspreetdhanjan.hud.model.User;
import com.jaspreetdhanjan.hud.util.Encryption;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

public class UserSystem {
	private static UserSystem instance;

	private final BulletinApi api;

	UserSystem(BulletinApi api) {
		this.api = api;
	}

	public static synchronized UserSystem getInstance() {
		if (instance == null) {
			instance = new UserSystem(BulletinImpl.getInstance());
		}
		return instance;
	}

	public boolean isUsernameTaken(String username) {
		User retrieved = getUser(username);
		return !Objects.isNull(retrieved);
	}

	public User createUser(String username, String firstName, String lastName, String email, String password) {
		User user = new User(username, firstName, lastName, email, password);
		api.write(user);
		return user;
	}

	public User getUser(String username) {
		User template = new User();
		template.username = username;
		return api.read(template);
	}

	public User updateUser(String username, String firstName, String lastName, String password) {
		User template = new User();
		template.username = username;

		User retrievedUser = api.takeIfExists(template);
		retrievedUser.firstName = firstName;
		retrievedUser.lastName = lastName;
		retrievedUser.password = password;

		api.write(retrievedUser);
		return retrievedUser;
	}

	public User authenticateLoginAttempt(String username, String password, OnFailedLoginCallback callback) {
		User user = getUser(username);

		if (user == null) {
			if (callback != null) {
				callback.onUserNotExists();
			} else {
				System.err.println("User does not exist!");
			}

			return null;
		}

		try {
			if (!Encryption.validateString(user.password, password)) {
				if (callback != null) {
					callback.onUserWrongPassword();
				} else {
					System.err.println("Password is not correct!");
				}

				return null;
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return user;
	}
}