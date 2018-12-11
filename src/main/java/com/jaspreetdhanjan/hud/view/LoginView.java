package com.jaspreetdhanjan.hud.view;

import com.jaspreetdhanjan.hud.controller.Controller;
import com.jaspreetdhanjan.hud.controller.LoginController;

public class LoginView extends View {
	@Override
	protected Controller getController() {
		return new LoginController();
	}

	@Override
	protected String getParentName() {
		return "login-view";
	}
}