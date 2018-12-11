package com.jaspreetdhanjan.hud.model;

import com.jaspreetdhanjan.hud.api.AtomicIndex;
import com.jaspreetdhanjan.hud.api.BulletinImpl;
import com.jaspreetdhanjan.hud.api.annotation.JavaSpaceModel;
import com.jaspreetdhanjan.hud.api.entry.BaseEntry;
import com.jaspreetdhanjan.hud.util.Formatter;

@JavaSpaceModel
public class Comment extends BaseEntry {
	public Long index;
	public String username;
	public String content;
	public Long topicIndex;
	public Long time;
	public Boolean isPrivate;

	public Comment() {
	}

	public Comment(String username, String content, long topicIndex, boolean isPrivate) {
		this.index = AtomicIndex.nextId(BulletinImpl.getInstance(), getClass());
		this.username = username;
		this.content = content;
		this.topicIndex = topicIndex;
		time = System.currentTimeMillis();
		this.isPrivate = isPrivate;
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