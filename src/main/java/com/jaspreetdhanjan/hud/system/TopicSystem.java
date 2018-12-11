package com.jaspreetdhanjan.hud.system;

import com.jaspreetdhanjan.hud.api.BulletinApi;
import com.jaspreetdhanjan.hud.api.BulletinImpl;
import com.jaspreetdhanjan.hud.model.*;

public class TopicSystem {
	private static TopicSystem instance;

	private final BulletinApi api;

	public TopicSystem(BulletinApi api) {
		this.api = api;
	}

	public static synchronized TopicSystem getInstance() {
		if (instance == null) {
			instance = new TopicSystem(BulletinImpl.getInstance());
		}
		return instance;
	}

	public void addTopic(User author, String title, String desc) {
		// Remember: we can't enable transaction blocking here, the Topic constructor aims atomicity!

		// Make the topic and place it into the JavaSpace
		Topic topic = new Topic(author.username, title, desc);
		api.write(topic);

		// Any author who makes a topic shall receive notifications on any further comments made to that topic.
		UserTopicSubscription subscription = new UserTopicSubscription(author.username, topic.index);
		api.write(subscription);
	}

	public void deleteTopic(Topic topic) {
		// Enable transaction block to make sure we achieve everything the interface states.
		api.beginTransactionBlock();
		{
			// Delete topic
			Topic topicTemplate = new Topic();
			topicTemplate.index = topic.index;

			api.takeIfExists(topicTemplate);

			// Delete comments
			Comment commentTemplate = new Comment();
			commentTemplate.topicIndex = topic.index;

			api.takeAll(commentTemplate, Long.MAX_VALUE);

			// Delete any subscribers to this topic
			UserTopicSubscription subscriptionTemplate = new UserTopicSubscription();
			subscriptionTemplate.topicIndex = topic.index;

			api.takeAll(subscriptionTemplate, Long.MAX_VALUE);

			// Trigger an event for any listeners
			RemovedTopic removedTopic = new RemovedTopic(topic);
			api.write(removedTopic);
		}
		api.endTransactionBlock();
	}
}