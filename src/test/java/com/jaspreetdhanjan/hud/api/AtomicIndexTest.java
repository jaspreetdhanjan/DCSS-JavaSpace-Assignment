package com.jaspreetdhanjan.hud.api;

import com.jaspreetdhanjan.hud.api.entry.BaseEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AtomicIndexTest {
	private final class MockEntry extends BaseEntry {
		public Long id;

		public MockEntry(BulletinApi api) {
			id = AtomicIndex.nextId(api, getClass());
		}
	}

	@Test
	public void testIncrement() {
		BulletinApi api = new MockBulletinApi();

		final long instances = 5;

		MockEntry entry = null;
		for (int i = 0; i < instances; i++) {
			entry = new MockEntry(api);
		}

		// Five instances created, but ID is zero indexed...

		assertEquals("Instantiating this class five times should make our ID counter tick to 5.", entry.id.longValue(), instances - 1);
	}
}