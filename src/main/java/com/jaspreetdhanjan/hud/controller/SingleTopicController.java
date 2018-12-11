package com.jaspreetdhanjan.hud.controller;

import com.jaspreetdhanjan.hud.CacheRegistry;
import com.jaspreetdhanjan.hud.model.Comment;
import com.jaspreetdhanjan.hud.model.Topic;
import com.jaspreetdhanjan.hud.model.User;
import com.jaspreetdhanjan.hud.system.CommentSystem;
import com.jaspreetdhanjan.hud.system.NotificationSystem;
import com.jaspreetdhanjan.hud.system.TopicSystem;
import com.jaspreetdhanjan.hud.util.Formatter;
import com.sun.javafx.collections.ObservableSequentialListWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Objects;

public class SingleTopicController extends Controller {
	private final Topic topic;

	@FXML
	private Text welcomeText;

	@FXML
	private ToggleButton subscribeButton;

	@FXML
	private Button deleteButton;

	@FXML
	private Button backButton;

	@FXML
	private Text topicNameText;

	@FXML
	private Text topicDescText;

	@FXML
	private ListView<Comment> commentList;

	@FXML
	private TextArea commentBox;

	@FXML
	private Button addCommentButton;

	@FXML
	private ToggleButton privacyButton;

	public SingleTopicController(Topic topic) {
		Objects.requireNonNull(topic);
		this.topic = topic;
	}

	@Override
	protected void initActionListeners() {
		final User user = CacheRegistry.getValue(CacheRegistry.USER);

		final CommentSystem commentSystem = CommentSystem.getInstance();
		final TopicSystem topicSystem = TopicSystem.getInstance();
		final NotificationSystem notificationSystem = NotificationSystem.getInstance();

		// Menu bar

		welcomeText.setText("Welcome " + user.firstName + " " + user.lastName);
		commentList.setPlaceholder(new Label("No comments."));

		boolean isSubscribed = notificationSystem.isUserSubscribed(user, topic);
		if (isSubscribed) {
			notificationSystem.startLocalTracking(topic);
		}

		subscribeButton.setSelected(isSubscribed);
		subscribeButton.setOnAction(action -> toggleSubscribe(user, notificationSystem));

		if (isTopicOwner(user)) {
			deleteButton.setOnAction(action -> onTopicDelete(topicSystem));
		} else {
			deleteButton.setDisable(true);
		}

		backButton.setOnAction(action -> lastView());

		// Page content

		topicNameText.setText(topic.title);
		topicDescText.setText(getTopicDescription());

		updateCommentList(commentSystem, user);

		addCommentButton.setOnAction(action -> addComment(commentSystem, user));

		privacyButton.setOnAction(action -> onPrivacyToggle());
	}


	private void toggleSubscribe(User user, NotificationSystem notificationSystem) {
		boolean isSubscribed = notificationSystem.isUserSubscribed(user, topic);

		if (subscribeButton.isSelected() && !isSubscribed) {
			notificationSystem.setSubscription(user, topic);
			notificationSystem.startLocalTracking(topic);
		} else if (!subscribeButton.isSelected() && isSubscribed) {
			notificationSystem.removeSubscription(user, topic);
			notificationSystem.stopLocalTracking(topic);
		}
	}

	private void onPrivacyToggle() {
		if (privacyButton.isSelected()) {
			privacyButton.setText("Privacy: ON");

		} else {
			privacyButton.setText("Privacy: OFF");
		}
	}

	private void updateCommentList(CommentSystem commentSystem, User user) {
		commentList.setItems(getItems(commentSystem, user));
	}

	private ObservableList<Comment> getItems(CommentSystem commentSystem, User user) {
		List<Comment> comments = commentSystem.getComments(topic.index);
		comments.removeIf(comment -> isCommentVisible(comment, user));

		return new ObservableSequentialListWrapper<>(comments);
	}

	private boolean isCommentVisible(Comment comment, User user) {
		return comment.isPrivate && !(isTopicOwner(user) || comment.username.equals(user.username));
	}

	private String getTopicDescription() {
		return topic.description + "\n\nPosted by: " + topic.username + " on " + Formatter.longFormatDateTime(topic.time);
	}

	private boolean isTopicOwner(User user) {
		return topic.username.equals(user.username);
	}

	private void onTopicDelete(TopicSystem topicSystem) {
		Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
		confirmDelete.setContentText("Are you sure you would like to delete?");
		confirmDelete.showAndWait();

		if (confirmDelete.getResult() == ButtonType.OK) {
			topicSystem.deleteTopic(topic);
			lastView();
		}
	}

	private void addComment(CommentSystem commentSystem, User user) {
		final String comment = commentBox.getText();
		if (comment == null || comment.isEmpty()) {
			return;
		}

		boolean isPrivate = privacyButton.isSelected();
		commentSystem.addComment(user.username, comment, topic.index, isPrivate);

		updateCommentList(commentSystem, user);
		commentBox.setText("");
	}
}