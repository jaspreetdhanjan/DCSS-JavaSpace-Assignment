package com.jaspreetdhanjan.hud.view;

import com.jaspreetdhanjan.hud.controller.Controller;
import com.jaspreetdhanjan.hud.controller.NotificationsController;

public class NotificationsView extends View {
	@Override
	protected Controller getController() {
		return new NotificationsController();
	}

	@Override
	protected String getParentName() {
		return "notifications-view";
	}
}
