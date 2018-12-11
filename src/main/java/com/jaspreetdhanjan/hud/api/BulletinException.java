package com.jaspreetdhanjan.hud.api;

/**
 * This Exception class is mainly used to wrap any JavaSpace exceptions and attach a helpful message.
 */

public class BulletinException extends RuntimeException {
	public BulletinException(String msg, Exception wrappedException) {
		super(msg, wrappedException);
	}

	public BulletinException(String msg) {
		super(msg);
	}
}