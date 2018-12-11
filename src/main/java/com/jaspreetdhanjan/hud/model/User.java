package com.jaspreetdhanjan.hud.model;

import com.jaspreetdhanjan.hud.api.annotation.JavaSpaceModel;
import com.jaspreetdhanjan.hud.api.entry.BaseEntry;

@JavaSpaceModel
public class User extends BaseEntry {
	public String username;
	public String firstName, lastName;
	public String emailAddress;
	public String password;

	public User() {
	}

	public User(String username, String firstName, String lastName, String emailAddress, String password) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.password = password;
	}

	@Override
	public String toString() {
		return "User -> " + username;
	}
}