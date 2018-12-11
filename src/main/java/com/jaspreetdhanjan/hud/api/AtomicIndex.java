package com.jaspreetdhanjan.hud.api;

import com.jaspreetdhanjan.hud.api.annotation.JavaSpaceModel;
import com.jaspreetdhanjan.hud.api.entry.BaseEntry;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A thread-safe implementation of a counter in JavaSpace.
 */

//@JavaSpaceModel(lease = JavaSpaceModel.Leases.DEBUG_LEASE)
@JavaSpaceModel()
public class AtomicIndex extends BaseEntry {
	public String name;
	public AtomicLong counter;

	public AtomicIndex() {
	}

	public AtomicIndex(String name, AtomicLong counter) {
		this.name = name;
		this.counter = counter;
	}

	public long getAndIncrement() {
		return counter.getAndIncrement();
	}

	public static synchronized long nextId(BulletinApi api, Class<?> clazz) {
		final String name = clazz.getName();

		AtomicIndex template = new AtomicIndex();
		template.name = name;

		// If we take this object we must modify it or do nothing at all.
		// Therefore we enable transaction locking on these operations.

		api.beginTransactionBlock();

		AtomicIndex idGen = api.takeIfExists(template);
		if (idGen == null) {
			idGen = new AtomicIndex(name, new AtomicLong());
		}

		// We have our next ID, write back and commit the transaction

		final long nextId = idGen.getAndIncrement();

		api.write(idGen);

		api.endTransactionBlock();

		return nextId;
	}
}