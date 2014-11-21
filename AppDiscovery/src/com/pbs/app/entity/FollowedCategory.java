package com.pbs.app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class FollowedCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int folCatId;

	@OneToOne
	private User user;

	@OneToOne
	private AppCategory appCategory;

	public int getFolCatId() {
		return folCatId;
	}

	public void setFolCatId(int folCatId) {
		this.folCatId = folCatId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AppCategory getAppCategory() {
		return appCategory;
	}

	public void setAppCategory(AppCategory appCategory) {
		this.appCategory = appCategory;
	}

	@Override
	public String toString() {
		return "FollowedCategory [user=" + user + ", appCategory=" + appCategory + "]";
	}

}
