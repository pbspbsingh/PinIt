package com.pbs.app.component.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pbs.app.component.service.AppCategoryService;
import com.pbs.app.entity.AppCategory;
import com.pbs.app.entity.FollowedCategory;
import com.pbs.app.entity.User;
import com.pbs.app.model.UserCategoryModel;

@RestController
@RequestMapping(value = "/user")
public class AppCategoryController {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private AppCategoryService categoryservice;

	@RequestMapping(value = "/countCategory")
	public int getCategoryCount(int userId) {

		return categoryservice.getUsersCategory(userId).size();
	}

	@RequestMapping(value = "/category")
	public List<UserCategoryModel> getUsersCategory(int userId) {
		final List<UserCategoryModel> list = new ArrayList<>();
		final List<AppCategory> usersCat = categoryservice.getUsersCategory(userId);
		final List<AppCategory> allCategories = categoryservice.getAllCategories();

		for (AppCategory cat : allCategories) {
			UserCategoryModel model = new UserCategoryModel();
			model.setUserId(userId);
			model.setCategory(cat.getCategory());
			model.setSelected(contains(usersCat, cat));

			list.add(model);
		}
		logger.info("Total number of categories: " + allCategories.size());
		logger.info("Total user's categories: " + usersCat.size());
		return list;
	}

	private boolean contains(List<AppCategory> usersCat, AppCategory cat) {
		for (AppCategory myCat : usersCat) {
			if (myCat.getAppCategoryId() == cat.getAppCategoryId())
				return true;
		}
		return false;
	}

	@RequestMapping(value = "/saveCategories")
	public boolean saveCategories(int userId, String categories) {
		String[] cats = categories.split("----");

		categoryservice.removeMapping(userId);
		for (String category : cats) {
			if (category == null || category.trim().isEmpty())
				continue;

			final FollowedCategory followedCategory = new FollowedCategory();
			final User user = categoryservice.getEntity(User.class, userId);
			final AppCategory appCategory = categoryservice.getCategoryByName(category);
			followedCategory.setUser(user);
			followedCategory.setAppCategory(appCategory);
			if (appCategory != null && user != null)
				categoryservice.save(followedCategory);
			else
				logger.warn("No matching user: " + userId + "or category: " + category + " is found.");
		}
		return true;
	}
}
