package com.jaspreetdhanjan.hud.view;

import com.jaspreetdhanjan.hud.ViewHandler;
import com.jaspreetdhanjan.hud.controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * This class defines the View contract for our application. A single view should be responsible for a "screen"
 * within the application.
 * <p>
 * Any implementations of this class are intentionally not intended to contain lots of code. Since we build our UI
 * through the .fxml files, there is no real need to. The main purpose of this class it to let our API know what view
 * is current active and switching views.
 */

public abstract class View {
	private ViewHandler viewHandler;

	/**
	 * This method is invoked solely when the view is about to be shown in the application. It should not be
	 * overridden, instead it makes a call to {@link #onViewOpen()} which the developer should use.
	 */
	public final void init(ViewHandler viewHandler) {
		this.viewHandler = viewHandler;
		onViewOpen();
	}

	/**
	 * This method is invoked solely when the view is about to be ended. It should not be
	 * overridden, instead it makes a call to {@link #onViewClose()} which the developer should use.
	 */
	public final void deinit() {
		viewHandler = null;
		onViewClose();
	}

	/**
	 * @see #init(ViewHandler)
	 */
	protected void onViewOpen() {
	}

	/**
	 * @see #init(ViewHandler)
	 */
	protected void onViewClose() {
	}

	/**
	 * Returns the sole instance of our {@link ViewHandler} class.
	 */
	public ViewHandler getViewHandler() {
		return viewHandler;
	}

	/**
	 * Builds a new scene with all of our required assets.
	 */
	public Scene getScene(int defaultWidth, int defaultHeight) {
		final Controller controller = getController();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/parents/" + getParentName() + ".fxml"));
		loader.setController(controller);

		Parent root;
		try {
			root = loader.load();
		} catch (IOException e) {
			// Could not find our file.
			throw new RuntimeException("You have not created a .fxml file for this View.", e);
		}

		controller.init(this);

		Scene scene = new Scene(root, defaultWidth, defaultHeight);
		postProcess(scene);

		return scene;
	}

	/**
	 * If we want to modify our scene BEFORE it gets rendered, but AFTER it gets loaded from the .fxml then we have
	 * the option to do it here.
	 */
	protected void postProcess(Scene scene) {
	}

	/**
	 * Should return the corresponding controller for this view. This cannot be defined in our .fxml file as we are
	 * using a Maven project.
	 */
	protected abstract Controller getController();

	/**
	 * Should return the name of the corresponding .fxml file. There is no need to include the path or file extension --
	 * just the name of the file.
	 */
	protected abstract String getParentName();
}