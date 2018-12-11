package com.jaspreetdhanjan.hud.controller;

import com.jaspreetdhanjan.hud.CacheRegistry;
import com.jaspreetdhanjan.hud.model.User;
import com.jaspreetdhanjan.hud.system.TopicSystem;
import com.jaspreetdhanjan.hud.util.InputValidator;
import com.jaspreetdhanjan.hud.view.TopicView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class NewTopicController extends Controller {
	@FXML
	private TextField titleField;

	@FXML
	private TextArea descField;

	@FXML
	private Button createButton;

	@FXML
	private Button cancelButton;

	@FXML
	private Text errorText;

	@Override
	protected void initActionListeners() {
		createButton.setOnAction(this::onCreate);
		cancelButton.setOnAction(event -> NewTopicController.this.lastView());
	}

	private void onCreate(ActionEvent action) {
		final String title = titleField.getText().trim();
		final String desc = descField.getText().trim();

		if (InputValidator.isAnyEmpty(title, desc)) {
			errorText.setText("The topic title and description fields must not be empty");
			return;
		}

		User user = CacheRegistry.getValue(CacheRegistry.USER);

		TopicSystem.getInstance().addTopic(user, title, desc);

		switchView(new TopicView());
	}
}