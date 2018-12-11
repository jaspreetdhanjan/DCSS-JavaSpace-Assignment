package com.jaspreetdhanjan.hud.controller;

import com.jaspreetdhanjan.hud.system.NotificationSystem;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class NotificationsController extends Controller {
	@FXML
	private ListView notifList;

	@FXML
	private Button clearButton;

	@FXML
	private Button backButton;

	@Override
	protected void initActionListeners() {
		notifList.setPlaceholder(new Label("No recent notifications."));

		NotificationSystem notificationSystem = NotificationSystem.getInstance();

		updateList(notificationSystem);

		clearButton.setOnAction(action -> {
			notificationSystem.clearNotifications();
			updateList(notificationSystem);
		});

		backButton.setOnAction(action -> lastView());
	}

	private void updateList(NotificationSystem notificationSystem) {
		notifList.setItems(new ObservableListWrapper<>(notificationSystem.getNotifications()));
	}
}
