package com.jaspreetdhanjan.hud.view;

import com.jaspreetdhanjan.hud.controller.Controller;
import com.jaspreetdhanjan.hud.controller.TopicController;

public class TopicView extends View {
	@Override
	protected Controller getController() {
		return new TopicController();
	}

	@Override
	protected String getParentName() {
		return "topic-view";
	}
}