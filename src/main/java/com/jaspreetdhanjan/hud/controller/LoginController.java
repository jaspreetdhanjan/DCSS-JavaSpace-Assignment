package com.jaspreetdhanjan.hud.controller;

import com.jaspreetdhanjan.hud.CacheRegistry;
import com.jaspreetdhanjan.hud.model.User;
import com.jaspreetdhanjan.hud.system.OnFailedLoginCallback;
import com.jaspreetdhanjan.hud.system.UserSystem;
import com.jaspreetdhanjan.hud.util.Encryption;
import com.jaspreetdhanjan.hud.util.InputValidator;
import com.jaspreetdhanjan.hud.view.RegisterView;
import com.jaspreetdhanjan.hud.view.TopicView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginController extends Controller {
	private final class Login {
		private static final int MAX_ATTEMPT = 5;

		private final AtomicInteger attemptsMade = new AtomicInteger();

		private void increaseAttempts() {
			if (!hasMaxedAttempts()) {
				attemptsMade.incrementAndGet();
			}
		}

		private int getRemainingAttempts() {
			return MAX_ATTEMPT - attemptsMade.get();
		}

		private boolean hasMaxedAttempts() {
			return attemptsMade.get() >= MAX_ATTEMPT;
		}
	}

	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@FXML
	private Button loginButton;

	@FXML
	private Button registerButton;

	@FXML
	private Text errorText;

	@Override
	protected void initActionListeners() {
		loginButton.setOnAction(event -> handleLogin());
		registerButton.setOnAction(event -> LoginController.this.switchView(new RegisterView()));
	}

	private final Login login = new Login();

	private void handleLogin() {
		String userString = username.getText().trim();
		String passString = password.getText().trim();

		if (InputValidator.isAnyEmpty(userString, passString)) {
			errorText.setText("Username and password fields must not be empty!");
			return;
		}

		try {
			passString = Encryption.encryptString(passString);
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			errorText.setText("A fatal error occurred");
			return;
		}

		UserSystem userSystem = UserSystem.getInstance();

		User user = userSystem.authenticateLoginAttempt(userString, passString, new OnFailedLoginCallback() {
			@Override
			public void onUserNotExists() {
				errorText.setText("User " + userString + " does not exist! Please check your username.");
			}

			@Override
			public void onUserWrongPassword() {
				if (login.hasMaxedAttempts()) {
					errorText.setText("You have reached the maximum number of login attempts. We have reset your password please check your email.");
					return;
				}

				login.increaseAttempts();

				errorText.setText("Password incorrect! Please check your password, you have " + login.getRemainingAttempts() + " remaining attempts.");
			}
		});

		if (user != null) {
			CacheRegistry.store(CacheRegistry.USER, user);
			switchView(new TopicView());
		}
	}
}