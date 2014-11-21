package com.pbs.app.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Timeline {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int timelineId;

	@Column(nullable = false)
	private Date time = new Date();

	@OneToOne(fetch = FetchType.EAGER)
	private App app;

	@OneToOne(fetch = FetchType.EAGER)
	private User user;

	@Column(length = 512, nullable = false)
	private String comments;

	@Column(nullable = false)
	private boolean likes = false;

	@Column(nullable = false)
	private boolean pinned = false;

	@Column(nullable = false)
	private int rating = 0;

	public int getTimelineId() {
		return timelineId;
	}

	public void setTimelineId(int timelineId) {
		this.timelineId = timelineId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isLikes() {
		return likes;
	}

	public void setLikes(boolean likes) {
		this.likes = likes;
	}

	public boolean isPinned() {
		return pinned;
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "Timeline [time=" + time + ", app=" + app + ", user=" + user + ", comments=" + comments + ", likes=" + likes + ", pinned="
				+ pinned + ", rating=" + rating + "]";
	}

}
