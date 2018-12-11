package com.jaspreetdhanjan.hud.view;

import com.jaspreetdhanjan.hud.controller.Controller;
import com.jaspreetdhanjan.hud.controller.RegisterController;

public class RegisterView extends View {
	@Override
	protected Controller getController() {
		return new RegisterController();
	}

	@Override
	protected String getParentName() {
		return "register-view";
	}
}