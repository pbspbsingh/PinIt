package com.pbs.app.amazonpinit.adapter;

public class NavListItemModel {

	private final String title;
	private final int icon;
	private final boolean hasCounter;
	private final String counter;

	public NavListItemModel(String title, int icon, boolean hasCounter, String counter) {
		this.title = title;
		this.icon = icon;
		this.hasCounter = hasCounter;
		this.counter = counter;
	}

	public NavListItemModel(String title, int icon) {
		this(title, icon, false, "");
	}

	public String getTitle() {
		return title;
	}

	public int getIcon() {
		return icon;
	}

	public boolean isHasCounter() {
		return hasCounter;
	}

	public String getCounter() {
		return counter;
	}

	@Override
	public String toString() {
		return "NavListItemModel [title=" + title + ", icon=" + icon + ", hasCounter=" + hasCounter + ", counter=" + counter + "]";
	}

}
