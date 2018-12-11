package com.jaspreetdhanjan.hud.controller;

import com.jaspreetdhanjan.hud.CacheRegistry;
import com.jaspreetdhanjan.hud.api.BulletinApi;
import com.jaspreetdhanjan.hud.api.BulletinImpl;
import com.jaspreetdhanjan.hud.model.Topic;
import com.jaspreetdhanjan.hud.model.User;
import com.jaspreetdhanjan.hud.view.*;
import com.sun.javafx.collections.ObservableSequentialListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TopicController extends Controller {
	private final class TopicFilterer {
		private List<Topic> unfilteredTopics;

		public TopicFilterer(List<Topic> unfilteredTopics) {
			this.unfilteredTopics = unfilteredTopics;
		}

		public ObservableList<Topic> sort(boolean newest, String filter) {
			List<Topic> list = new ArrayList<>(unfilteredTopics);

			if (newest) {
				list.sort((t0, t1) -> -Long.compare(t0.time, t1.time));
			} else {
				list.sort(Comparator.comparingLong(t0 -> t0.time));
			}

			if (filter != null && !filter.isEmpty()) {
				list.removeIf(topic -> !topic.toString().toLowerCase().contains(filter.toLowerCase()));
			}

			return new ObservableSequentialListWrapper<>(list);
		}
	}

	@FXML
	private Text welcomeText;

	@FXML
	private Button newTopicButton;

	@FXML
	private Button topicButton;

	@FXML
	private Button notificationsButton;

	@FXML
	private Button settingsButton;

	@FXML
	private Button logoutButton;

	@FXML
	private ComboBox<String> sortBox;

	@FXML
	private TextField searchBox;

	@FXML
	private ListView<Topic> topicList;

	@Override
	protected void initActionListeners() {
		User user = CacheRegistry.getValue(CacheRegistry.USER);

		welcomeText.setText("Welcome " + user.firstName + " " + user.lastName);

		newTopicButton.setOnAction(action -> TopicController.this.switchView(new NewTopicView()));

		topicButton.setOnAction(action -> TopicController.this.switchView(new TopicView()));

		notificationsButton.setOnAction(action -> TopicController.this.switchView(new NotificationsView()));

		settingsButton.setOnAction(action -> TopicController.this.switchView(new SettingsView()));

		logoutButton.setOnAction(this::doLogout);

		topicList.setPlaceholder(new Label("No topics."));

		// If double clicked on topic, move to the renderer
		topicList.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
				onTopicSelection(topicList.getSelectionModel());
			}
		});

		BulletinApi api = BulletinImpl.getInstance();
		List<Topic> topics = api.readAll(new Topic(), Long.MAX_VALUE);

		configureFiltering(new TopicFilterer(topics));
	}

	private void configureFiltering(TopicFilterer topicFilterer) {
		// Set defaults
		topicList.setItems(topicFilterer.sort(true, null));

		// Set sort box
		sortBox.setItems(FXCollections.observableArrayList("Newest", "Oldest"));
		sortBox.setEditable(false);
		sortBox.getSelectionModel().selectFirst();
		sortBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			boolean ascending = newValue.equals("Newest");
			String filterText = searchBox.getText().trim();

			topicList.setItems(topicFilterer.sort(ascending, filterText));
		});

		// Set filtering
		searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
			boolean ascending = sortBox.getValue().equals("Newest");
			String filterText = newValue.trim();

			topicList.setItems(topicFilterer.sort(ascending, filterText));
		});
	}

	private void onTopicSelection(SelectionModel<Topic> selectionModel) {
		Topic selected = selectionModel.getSelectedItem();
		if (selected != null) {
			switchView(new SingleTopicViewRenderer(selected));
		}
	}
}