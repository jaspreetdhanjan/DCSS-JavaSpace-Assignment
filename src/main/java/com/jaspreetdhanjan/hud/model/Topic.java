package com.jaspreetdhanjan.hud.model;

import com.jaspreetdhanjan.hud.api.AtomicIndex;
import com.jaspreetdhanjan.hud.api.BulletinImpl;
import com.jaspreetdhanjan.hud.api.annotation.JavaSpaceModel;
import com.jaspreetdhanjan.hud.api.entry.BaseEntry;
import com.jaspreetdhanjan.hud.util.Formatter;

@JavaSpaceModel
public class Topic extends BaseEntry {
	public Long index;
	public String username;
	public String title;
	public String description;
	public Long time;

	public Topic() {
	}

	public Topic(String username, String title, String description) {
		this.index = AtomicIndex.nextId(BulletinImpl.getInstance(), getClass());
		this.username = username;
		this.title = title;
		this.description = description;
		this.time = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		if (index == null) {
			// This object is a template
			return super.toString();
		}
		return Formatter.toString(this);
	}
}