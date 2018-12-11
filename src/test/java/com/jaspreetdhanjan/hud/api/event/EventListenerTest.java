package com.jaspreetdhanjan.hud.api.event;

import com.jaspreetdhanjan.hud.api.MockJavaSpace;
import com.jaspreetdhanjan.hud.api.entry.BaseEntry;
import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.space.AvailabilityEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventListenerTest {
	private final class MockEventListener extends EventListener {
		private BaseEntry causation;

		@Override
		public void onEvent(BaseEntry causation) {
			this.causation = causation;
		}

		public BaseEntry getCausation() {
			return causation;
		}
	}

	private final class MockAvailabilityEvent extends AvailabilityEvent {
		protected MockAvailabilityEvent() {
			super(new MockJavaSpace(), 0, 0, null, false);
		}

		@Override
		public Entry getEntry() throws UnusableEntryException {
			return new MockComment("jaspreet", "Hello this is a comment!", 2541251, false);
		}

		@Override
		public Entry getSnapshot() {
			return null;
		}
	}


	private final class MockComment extends BaseEntry {
		private final String user;
		private final String msg;
		private final Long index;
		private final Boolean isPrivate;

		public MockComment(String user, String msg, long index, boolean isPrivate) {
			this.user = user;
			this.msg = msg;
			this.index = index;
			this.isPrivate = isPrivate;
		}
	}

	@Test
	public void testEventTrigger() {
		MockEventListener listener = new MockEventListener();

		MockAvailabilityEvent event = new MockAvailabilityEvent();
		listener.notify(event);

		MockComment comment = (MockComment) listener.getCausation();

		assertEquals("Our mock comment notification event should return correct values.", comment.user, "jaspreet");
		assertEquals("Our mock comment notification event should return correct values.", comment.isPrivate, false);
		assertEquals("Our mock comment notification event should return correct values.", comment.index.longValue(), 2541251);
	}
}