package com.pbs.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class App {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int appId;

	@Column(nullable = false, length = 128, unique = true)
	private String appPackage;

	@Column(nullable = false, length = 128)
	private String appTitle;

	@Column(nullable = false, length = 128)
	private String appCompany;

	@OneToOne(fetch = FetchType.EAGER)
	private AppCategory category;

	@Column
	@Lob
	private byte[] appIcon;
	
	@Transient
	private String iconData;

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public String getAppTitle() {
		return appTitle;
	}

	public void setAppTitle(String appTitle) {
		this.appTitle = appTitle;
	}

	public String getAppCompany() {
		return appCompany;
	}

	public void setAppCompany(String appCompany) {
		this.appCompany = appCompany;
	}

	public AppCategory getCategory() {
		return category;
	}

	public void setCategory(AppCategory category) {
		this.category = category;
	}

	public byte[] getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(byte[] appIcon) {
		this.appIcon = appIcon;
	}


	public String getIconData() {
		return iconData;
	}

	public void setIconData(String iconData) {
		this.iconData = iconData;
	}

	@Override
	public String toString() {
		return "App [appPackage=" + appPackage + ", appTitle=" + appTitle + ", appCompany=" + appCompany + ", category=" + category + "]";
	}

}
