package com.jaspreetdhanjan.hud.api;

import com.jaspreetdhanjan.hud.api.entry.BaseEntry;
import com.jaspreetdhanjan.hud.api.event.EventListener;

import java.util.List;

/**
 * This interface defines our main contract for our JavaSpace access. To avoid {@link ClassCastException} or
 * {@link java.rmi.MarshalException} we shall use our own implementation of the {@link net.jini.core.entry.Entry}
 * referred to as {@link BaseEntry}.
 * <p>
 * This class aims to abstract out the {@link net.jini.space.JavaSpace} API in a more "clean" way. All method calls made
 * to the JavaSpace are proxied through this implementation.
 * <p>
 * We do this to easily hook in our transaction and notification APIs to the method calls. The unit testing phase of
 * this project lifecycle is also made easier as we can just "mock" JavaSpace to directly test this class.
 */

public interface BulletinApi {
	long MAX_TIMEOUT = 500;

	/**
	 * See JavaSpace documentation.
	 */
	void writeAll(List<? extends BaseEntry> entries);

	/**
	 * See JavaSpace documentation.
	 */
	void write(BaseEntry entry);

	/**
	 * See JavaSpace documentation.
	 */
	<T extends BaseEntry> List<T> takeAll(T template, long maxEntries, long timeout);

	/**
	 * See JavaSpace documentation.
	 */
	default <T extends BaseEntry> List<T> takeAll(T template, long maxEntries) {
		return takeAll(template, maxEntries, MAX_TIMEOUT);
	}

	/**
	 * See JavaSpace documentation.
	 */
	<T extends BaseEntry> T take(T template, long timeout);

	/**
	 * See JavaSpace documentation.
	 */
	default <T extends BaseEntry> T take(T template) {
		return take(template, MAX_TIMEOUT);
	}

	/**
	 * See JavaSpace documentation.
	 */
	<T extends BaseEntry> T takeIfExists(T template, long timeout);

	/**
	 * See JavaSpace documentation.
	 */
	default <T extends BaseEntry> T takeIfExists(T template) {
		return takeIfExists(template, MAX_TIMEOUT);
	}

	/**
	 * Reads all instances that match the given template until the maxEntries limit is reached or the given
	 * timeout is hit.
	 */
	<T extends BaseEntry> List<T> readAll(T template, long maxEntries, long timeout);

	/**
	 * Reads all instances that match the given template until the maxEntries limit is reached or the timeout is hit
	 * {@link BulletinApi#MAX_TIMEOUT}.
	 */
	default <T extends BaseEntry> List<T> readAll(T template, long maxEntries) {
		return readAll(template, maxEntries, MAX_TIMEOUT);
	}

	/**
	 * See JavaSpace documentation.
	 */
	<T extends BaseEntry> T read(T template, long timeout);

	/**
	 * See JavaSpace documentation.
	 */
	default <T extends BaseEntry> T read(T template) {
		return read(template, MAX_TIMEOUT);
	}

	/**
	 * See JavaSpace documentation.
	 */
	<T extends BaseEntry> T readIfExists(T template, long timeout);

	/**
	 * See JavaSpace documentation.
	 */
	default <T extends BaseEntry> T readIfExists(T template) {
		return readIfExists(template, MAX_TIMEOUT);
	}

	/**
	 * Removes any elements that were added to the JavaSpace from this session, effectively meaning the session never
	 * happened. This will not clear everything out of the JavaSpace, unless this session was the first JavaSpace
	 */
	void cancelLocalLeases();

	/**
	 * Any BulletinApi methods called after this method has been called will use a transaction.
	 */
	void beginTransactionBlock();

	/**
	 * Any prior BulletinApi methods called will be committed in a single transaction. It will also reset the
	 * transaction state -- ready for the next block.
	 */
	void endTransactionBlock();

	/**
	 * See JavaSpace documentation.
	 */
	<T extends BaseEntry> void addEventListener(T template, EventListener listener);

	/**
	 * Removes a custom template listener.
	 */
	void removeEventListener(EventListener listener);
}