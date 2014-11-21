package com.pbs.app.model;

public class TimelineModel {

	private int userId;

	private String userName;

	private int appId;

	private String appTitle;

	private String appPackage;

	private String appIcon;

	private String appCompany;

	private String comment;

	private int likesCount;

	private int pinCounts;

	private int ratings;

	private int numberOfUsers;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppTitle() {
		return appTitle;
	}

	public void setAppTitle(String appTitle) {
		this.appTitle = appTitle;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public String getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppCompany() {
		return appCompany;
	}

	public void setAppCompany(String appCompany) {
		this.appCompany = appCompany;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getLikesCount() {
		return likesCount;
	}

	public void setLikesCount(int likesCount) {
		this.likesCount = likesCount;
	}

	public int getPinCounts() {
		return pinCounts;
	}

	public void setPinCounts(int pinCounts) {
		this.pinCounts = pinCounts;
	}

	public int getRatings() {
		return ratings;
	}

	public void setRatings(int ratings) {
		this.ratings = ratings;
	}

	public int getNumberOfUsers() {
		return numberOfUsers;
	}

	public void setNumberOfUsers(int numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}

	@Override
	public String toString() {
		return "TimelineModel [userId=" + userId + ", userName=" + userName + ", appId=" + appId + ", appTitle=" + appTitle
				+ ", appPackage=" + appPackage + ", appIcon=" + appIcon + ", appCompany=" + appCompany + ", comment=" + comment
				+ ", likesCount=" + likesCount + ", pinCounts=" + pinCounts + "]";
	}

}
