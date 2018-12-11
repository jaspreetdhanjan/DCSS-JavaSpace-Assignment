package com.jaspreetdhanjan.hud;

import com.jaspreetdhanjan.hud.view.View;

/**
 * This method defines the contract used to handle our views. We use this to avoid the plethora of other classes
 * interfering with our {@link ApplicationEntryPoint}.
 */

public interface ViewHandler {

	/**
	 * This method abstracts our JavaFX views into a nicer way to switch between screens. The {@link View} class contains
	 * all of the information and processes required to do this cleanly. This should be the ONLY way we do this.
	 */
	void setView(View newView);

	/**
	 * Will change the view to the prior view (if it exists, otherwise will not do anything).
	 */
	void setPreviousView();
}