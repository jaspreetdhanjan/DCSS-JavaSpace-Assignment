package com.jaspreetdhanjan.hud.model;

import com.jaspreetdhanjan.hud.api.annotation.JavaSpaceModel;
import com.jaspreetdhanjan.hud.api.entry.BaseEntry;

@JavaSpaceModel(lease = JavaSpaceModel.Leases.SHORT_LEASE)
public class RemovedTopic extends BaseEntry {
	public Long index;
	public String title;
	public String username;

	public RemovedTopic(Topic topic) {
		this.index = topic.index;
		this.title = topic.title;
		this.username = topic.username;
	}

	public RemovedTopic() {
	}

	@Override
	public String toString() {
		return "Removed comment -> " + title + " by " + username;
	}
}