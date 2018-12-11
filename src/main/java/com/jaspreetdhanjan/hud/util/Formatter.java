package com.jaspreetdhanjan.hud.util;

import com.jaspreetdhanjan.hud.model.Comment;
import com.jaspreetdhanjan.hud.model.Topic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {
	private static final DateFormat SHORT = new SimpleDateFormat("dd/MM/yy 'at' HH:mm");
	private static final DateFormat LONG = new SimpleDateFormat("EEEE, d MMMM yyyy 'at' HH:mm:ss");

	private Formatter() {
	}

	public static String formatDateTime(long time) {
		return SHORT.format(new Date(time));
	}

	public static String longFormatDateTime(long time) {
		return LONG.format(new Date(time));
	}

	public static String toString(Topic topic) {
		return topic.title + " (posted on " + formatDateTime(topic.time) + " by " + topic.username + ")";
	}

	public static String toString(Comment comment) {
		String str = comment.username + ": " + comment.content + " (posted on " + formatDateTime(comment.time) + ")";
		if (comment.isPrivate) {
			str += " [PRIVATE]";
		}
		return str;
	}
}