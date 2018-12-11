package com.jaspreetdhanjan.hud.view;

import com.jaspreetdhanjan.hud.controller.Controller;
import com.jaspreetdhanjan.hud.controller.NewTopicController;

public class NewTopicView extends View {
	@Override
	protected Controller getController() {
		return new NewTopicController();
	}

	@Override
	protected String getParentName() {
		return "new-topic-view";
	}
}