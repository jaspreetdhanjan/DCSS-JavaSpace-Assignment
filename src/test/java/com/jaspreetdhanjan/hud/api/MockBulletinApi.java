package com.jaspreetdhanjan.hud.api;

import com.jaspreetdhanjan.hud.api.entry.BaseEntry;
import com.jaspreetdhanjan.hud.api.event.EventListener;

import java.io.PrintStream;
import java.util.List;

public class MockBulletinApi implements BulletinApi {
	private BaseEntry testObject;

	@Override
	public void writeAll(List<? extends BaseEntry> entries) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(BaseEntry entry) {
		testObject = entry;
	}

	@Override
	public <T extends BaseEntry> List<T> takeAll(T template, long maxEntries, long timeout) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends BaseEntry> T take(T template, long timeout) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends BaseEntry> T takeIfExists(T template, long timeout) {
		BaseEntry local = testObject;
		testObject = null;
		return (T) local;
	}

	@Override
	public <T extends BaseEntry> List<T> readAll(T template, long maxEntries, long timeout) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends BaseEntry> T read(T template, long timeout) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends BaseEntry> T readIfExists(T template, long timeout) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void cancelLocalLeases() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void beginTransactionBlock() {
	}

	@Override
	public void endTransactionBlock() {
	}

	@Override
	public <T extends BaseEntry> void addEventListener(T template, EventListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeEventListener(EventListener listener) {
		throw new UnsupportedOperationException();
	}
}