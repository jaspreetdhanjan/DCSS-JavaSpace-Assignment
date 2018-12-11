package com.jaspreetdhanjan.hud.controller;

import com.jaspreetdhanjan.hud.CacheRegistry;
import com.jaspreetdhanjan.hud.system.NotificationSystem;
import com.jaspreetdhanjan.hud.view.LoginView;
import com.jaspreetdhanjan.hud.view.View;
import javafx.event.ActionEvent;

/**
 * This class defines the Controller contract for our application. This class MUST be defined in our View's .fxml
 * file in order for this to work. Only one controller should be assigned per view.
 */

public abstract class Controller {
	private View view;

	public final void init(View view) {
		this.view = view;
		initActionListeners();
	}

	protected abstract void initActionListeners();

	protected void doLogout(ActionEvent actionEvent) {
		CacheRegistry.clear();
		switchView(new LoginView());
	}

	protected final View getView() {
		return view;
	}

	protected final void switchView(View newView) {
		view.getViewHandler().setView(newView);
	}

	protected final void lastView() {
		view.getViewHandler().setPreviousView();
	}
}