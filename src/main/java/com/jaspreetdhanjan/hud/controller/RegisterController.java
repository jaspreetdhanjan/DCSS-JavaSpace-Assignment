package com.jaspreetdhanjan.hud.controller;

import com.jaspreetdhanjan.hud.system.UserSystem;
import com.jaspreetdhanjan.hud.util.InputValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class RegisterController extends Controller {
	@FXML
	private TextField usernameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private PasswordField confirmPasswordField;

	@FXML
	private TextField firstNameField;

	@FXML
	private TextField lastNameField;

	@FXML
	private TextField emailField;

	@FXML
	private Button registerButton;

	@FXML
	private Button cancelButton;

	@FXML
	private Text errorText;

	@Override
	protected void initActionListeners() {
		registerButton.setOnAction(action -> handleRegistration());
		cancelButton.setOnAction(action -> RegisterController.this.lastView());
	}

	private void handleRegistration() {
		String username = usernameField.getText().trim();
		String password = passwordField.getText();
		String confirmPassword = confirmPasswordField.getText();
		String firstName = firstNameField.getText().trim();
		String lastName = lastNameField.getText().trim();
		String email = emailField.getText().trim();

		if (InputValidator.isAnyEmpty(username, password, confirmPassword, firstName, lastName, email)) {
			errorText.setText("Please fill all fields");
			return;
		}

		if (!password.equals(confirmPassword)) {
			errorText.setText("Password mismatch: please make sure passwords are the same");
			return;
		}

		if (!InputValidator.isValidPassword(password)) {
			errorText.setText("Password must have at least 8 characters and contain: uppercase, lowercase, numerical and special (@#$%^&+=) characters");
			return;
		}

		if (!InputValidator.isValidEmail(email)) {
			errorText.setText("The email you have given is not valid");
			return;
		}

		UserSystem userSystem = UserSystem.getInstance();

		if (userSystem.isUsernameTaken(username)) {
			errorText.setText("Username already exists! Please try another one");
			return;
		}

		userSystem.createUser(username, firstName, lastName, email, password);
		lastView();
	}
}