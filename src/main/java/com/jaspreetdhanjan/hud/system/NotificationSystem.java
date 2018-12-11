package com.jaspreetdhanjan.hud.system;

import com.jaspreetdhanjan.hud.api.BulletinApi;
import com.jaspreetdhanjan.hud.api.BulletinImpl;
import com.jaspreetdhanjan.hud.api.entry.BaseEntry;
import com.jaspreetdhanjan.hud.api.event.EventListener;
import com.jaspreetdhanjan.hud.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NotificationSystem {
	private final class CommentTracker extends EventListener {
		private final Topic topic;

		public CommentTracker(Topic topic) {
			this.topic = topic;
		}

		@Override
		public void onEvent(BaseEntry causation) {
			Comment comment = (Comment) causation;

			List<String> notificationStrings = notifications.get(comment.topicIndex);
			if (notificationStrings == null) {
				notificationStrings = new ArrayList<>(1);
				notifications.put(comment.topicIndex, notificationStrings);
			}

			notificationStrings.add("A new comment was added by " + comment.username + " about " + topic.title);
		}
	}

	private final class TopicTracker extends EventListener {
		@Override
		public void onEvent(BaseEntry causation) {
			RemovedTopic removedTopic = (RemovedTopic) causation;

			List<String> notificationStrings = notifications.get(removedTopic.index);
			if (notificationStrings == null) {
				notificationStrings = new ArrayList<>(1);
				notifications.put(removedTopic.index, notificationStrings);
			}

			notificationStrings.add("The topic about " + removedTopic.title + " was removed by " + removedTopic.username);
		}
	}

	private static NotificationSystem instance;

	private final BulletinApi api;
	private Map<Long, List<String>> notifications = new HashMap<>();
	private Map<Long, EventListener[]> trackers = new HashMap<>();

	public NotificationSystem(BulletinApi api) {
		this.api = api;
	}

	public static synchronized NotificationSystem getInstance() {
		if (instance == null) {
			instance = new NotificationSystem(BulletinImpl.getInstance());
		}
		return instance;
	}

	public boolean isUserSubscribed(User user, Topic topic) {
		UserTopicSubscription subscriptionTemplate = new UserTopicSubscription(user.username, topic.index);
		UserTopicSubscription subscription = api.read(subscriptionTemplate);
		return subscription != null;
	}

	public void setSubscription(User user, Topic topic) {
		// Any user can subscribe to any topic

		UserTopicSubscription subscriptionTemplate = new UserTopicSubscription(user.username, topic.index);
		api.write(subscriptionTemplate);

		Comment commentTemplate = new Comment();
		commentTemplate.topicIndex = topic.index;
	}

	public void removeSubscription(User user, Topic topic) {
		UserTopicSubscription subscriptionTemplate = new UserTopicSubscription(user.username, topic.index);
		api.takeIfExists(subscriptionTemplate);
	}

	public void startLocalTracking(Topic topic) {
		Comment commentTemplate = new Comment();
		commentTemplate.topicIndex = topic.index;

		RemovedTopic removedTopicTemplate = new RemovedTopic();
		removedTopicTemplate.index = topic.index;

		// Two distinct trackers per topic. Locally cache the listeners so that we can stop tracking if requested.

		EventListener[] listeners = {new CommentTracker(topic), new TopicTracker()};

		trackers.put(topic.index, listeners);

		// Track the events through JavaSpace.

		BaseEntry[] templates = {commentTemplate, removedTopicTemplate};

		for (int i = 0; i < 2; i++) {
			api.addEventListener(templates[i], listeners[i]);
		}
	}

	public void stopLocalTracking(Topic topic) {
		// Remove the locally cached trackers, then remove from JavaSpace

		EventListener[] listeners = trackers.remove(topic.index);

		for (EventListener listener : listeners) {
			api.removeEventListener(listener);
		}

		clearNotifications(topic.index);

		// How will this work for offline tracking?
	}

	public List<String> getNotifications() {
		return notifications.values().stream().flatMap(List::stream).collect(Collectors.toList());
	}

	public void clearNotifications() {
		notifications.clear();
	}

	public void clearNotifications(long topicIndex) {
		notifications.get(topicIndex).clear();
	}
}