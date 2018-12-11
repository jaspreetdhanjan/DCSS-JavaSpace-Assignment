package com.jaspreetdhanjan.hud.api.event;

import com.jaspreetdhanjan.hud.api.BulletinException;
import com.jaspreetdhanjan.hud.api.entry.BaseEntry;
import net.jini.core.entry.Entry;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.space.AvailabilityEvent;

/**
 * Our API is built on the JINI event handling mechanism. We simplify the existing API to explicitly find and retrieve
 * the entry object that triggered the notification in the first place.
 */

public abstract class EventListener implements RemoteEventListener {
	public void notify(RemoteEvent event) {
		final AvailabilityEvent availabilityEvent = (AvailabilityEvent) event;

		try {
			Entry entry = availabilityEvent.getEntry();

			if (entry instanceof BaseEntry) {
				BaseEntry causation = (BaseEntry) entry;
				onEvent(causation);
			}
		} catch (Exception e) {
			throw new BulletinException("Unable to process event. Refer to wrapped exception.", e);
		}
	}

	protected abstract void onEvent(BaseEntry causation);
}