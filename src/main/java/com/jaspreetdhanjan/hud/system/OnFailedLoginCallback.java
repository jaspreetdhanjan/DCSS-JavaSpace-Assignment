package com.jaspreetdhanjan.hud.system;

public interface OnFailedLoginCallback {
	void onUserNotExists();

	void onUserWrongPassword();
}