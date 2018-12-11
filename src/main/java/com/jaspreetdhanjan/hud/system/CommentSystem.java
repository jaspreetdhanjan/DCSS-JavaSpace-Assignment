package com.jaspreetdhanjan.hud.system;

import com.jaspreetdhanjan.hud.api.BulletinApi;
import com.jaspreetdhanjan.hud.api.BulletinImpl;
import com.jaspreetdhanjan.hud.model.Comment;

import java.util.List;

public class CommentSystem {
	private static CommentSystem instance;

	private final BulletinApi api;

	public CommentSystem(BulletinApi api) {
		this.api = api;
	}

	public static synchronized CommentSystem getInstance() {
		if (instance == null) {
			instance = new CommentSystem(BulletinImpl.getInstance());
		}
		return instance;
	}

	public void addComment(String username, String content, long topicIndex, boolean isPrivate) {
		Comment comment = new Comment(username, content, topicIndex, isPrivate);
		api.write(comment);
	}

	public List<Comment> getComments(long topicIndex) {
		Comment template = new Comment();
		template.topicIndex = topicIndex;

		return api.readAll(template, Long.MAX_VALUE);
	}
}
