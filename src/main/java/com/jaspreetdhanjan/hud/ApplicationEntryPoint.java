package com.jaspreetdhanjan.hud;

import com.jaspreetdhanjan.hud.view.LoginView;
import com.jaspreetdhanjan.hud.view.View;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Defines our entry point into this Bulletin Board application.
 * <p>
 * We use JavaFX to render our assets. These JavaFX components are abstracted into our own types to improve consistency
 * between multiple views.
 * <p>
 * This class utilises the {@link Application} contract. Therefore it can be used to stop the application at any point.
 */

public class ApplicationEntryPoint extends Application implements ViewHandler {
	private static final String TITLE = "Bulletin Board";
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private View view;
	private View lastView;
	private Stage stage;

	public static void main(String... args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle(TITLE);
		primaryStage.setResizable(false);

		stage = primaryStage;

		setView(new LoginView());

		// Important: set the scene before showing the primary stage.
		primaryStage.show();
	}

	@Override
	public void setView(View newView) {
		if (view != null) {
			view.deinit();
		}
		lastView = view;
		view = newView;

		view.init(this);

		stage.setScene(view.getScene(WIDTH, HEIGHT));
	}

	@Override
	public void setPreviousView() {
		setView(lastView);
	}
}