package com.jaspreetdhanjan.hud.model;

import com.jaspreetdhanjan.hud.api.annotation.JavaSpaceModel;
import com.jaspreetdhanjan.hud.api.entry.BaseEntry;

@JavaSpaceModel
public class UserTopicSubscription extends BaseEntry {
	public String username;
	public Long topicIndex;

	public UserTopicSubscription(String username, Long topicIndex) {
		this.username = username;
		this.topicIndex = topicIndex;
	}

	public UserTopicSubscription() {
	}

	@Override
	public String toString() {
		return "User -> " + username + " (subscribed to topic ID: " + topicIndex + ")";
	}
}