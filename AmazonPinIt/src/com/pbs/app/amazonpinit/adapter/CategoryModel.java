package com.pbs.app.amazonpinit.adapter;

public class CategoryModel {

	private String category;

	private boolean isSelected;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public String toString() {
		return "CategoryModel [category=" + category + ", isSelected=" + isSelected + "]";
	}

}
