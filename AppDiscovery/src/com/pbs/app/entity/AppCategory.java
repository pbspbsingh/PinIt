package com.pbs.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AppCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int appCategoryId;

	@Column(nullable = false, unique = true, length = 75)
	private String category;

	public AppCategory() {
		super();
	}

	public AppCategory(String category) {
		this.category = category;
	}

	public int getAppCategoryId() {
		return appCategoryId;
	}

	public void setAppCategoryId(int appCategoryId) {
		this.appCategoryId = appCategoryId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "AppCategory [category=" + category + "]";
	}
	
}
