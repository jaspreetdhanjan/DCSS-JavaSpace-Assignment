package com.jaspreetdhanjan.hud.view;

import com.jaspreetdhanjan.hud.controller.Controller;
import com.jaspreetdhanjan.hud.controller.SingleTopicController;
import com.jaspreetdhanjan.hud.model.Topic;

public class SingleTopicViewRenderer extends View {
	private final Topic topic;

	public SingleTopicViewRenderer(Topic topic) {
		this.topic = topic;
	}

	@Override
	protected Controller getController() {
		return new SingleTopicController(topic);
	}

	@Override
	protected String getParentName() {
		return "single-topic-view";
	}

	public Topic getTopic() {
		return topic;
	}
}
