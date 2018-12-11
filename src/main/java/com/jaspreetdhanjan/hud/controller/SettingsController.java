package com.jaspreetdhanjan.hud.controller;

import com.jaspreetdhanjan.hud.CacheRegistry;
import com.jaspreetdhanjan.hud.model.User;
import com.jaspreetdhanjan.hud.system.UserSystem;
import com.jaspreetdhanjan.hud.util.InputValidator;
import com.jaspreetdhanjan.hud.view.TopicView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SettingsController extends Controller {
	@FXML
	private PasswordField passwordField;

	@FXML
	private PasswordField confirmPasswordField;

	@FXML
	private TextField firstNameField;

	@FXML
	private TextField lastNameField;

	@FXML
	private Button saveButton;

	@FXML
	private Button cancelButton;

	@FXML
	private Text errorText;

	@Override
	protected void initActionListeners() {
		User user = CacheRegistry.getValue(CacheRegistry.USER);

		passwordField.setText(user.password);
		confirmPasswordField.setText(user.password);
		firstNameField.setText(user.firstName);
		lastNameField.setText(user.lastName);

		saveButton.setOnAction(action -> handleSave(action, user));
		cancelButton.setOnAction(action -> SettingsController.this.lastView());
	}

	private void handleSave(ActionEvent actionEvent, User user) {
		String password = passwordField.getText();
		String confirmPassword = confirmPasswordField.getText();
		String firstName = firstNameField.getText().trim();
		String lastName = lastNameField.getText().trim();

		if (InputValidator.isAnyEmpty(password, confirmPassword, firstName, lastName)) {
			errorText.setText("Please fill all fields");
			return;
		}

		if (!password.equals(confirmPassword)) {
			errorText.setText("Password mismatch: please make sure passwords are the same");
			return;
		}

		User newUser = UserSystem.getInstance().updateUser(user.username, firstName, lastName, password);

		// The following is important: since we have locally cached this user we MUST retrieve it from the JavaSpace
		// again. This is because something or someone might have changed the definition of this user from another
		// machine.
		// Therefore we must ensure that we're taking it away, modifying, then placing back.

		CacheRegistry.store(CacheRegistry.USER, newUser);

		switchView(new TopicView());
	}
}