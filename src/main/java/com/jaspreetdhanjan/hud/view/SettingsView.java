package com.jaspreetdhanjan.hud.view;

import com.jaspreetdhanjan.hud.controller.Controller;
import com.jaspreetdhanjan.hud.controller.SettingsController;

public class SettingsView extends View {
	@Override
	protected Controller getController() {
		return new SettingsController();
	}

	@Override
	protected String getParentName() {
		return "settings-view";
	}
}