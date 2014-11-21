package com.pbs.app.component.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pbs.app.component.service.AppCategoryService;
import com.pbs.app.component.service.LoginService;
import com.pbs.app.entity.App;
import com.pbs.app.entity.AppCategory;
import com.pbs.app.entity.Timeline;
import com.pbs.app.entity.User;

@Controller
@RequestMapping(value = "/create")
public class CreateController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private AppCategoryService categoryService;

	@RequestMapping(value = { "/", "" })
	public String firstPage(Model model) {
		return "index";
	}

	@RequestMapping(value = { "user" }, method = RequestMethod.GET)
	public String showUsers(final Model model) {
		final List<User> users = loginService.getAll(User.class);
		model.addAttribute("users", users);
		return "user";
	}

	@RequestMapping(value = { "user" }, method = RequestMethod.POST)
	public String saveUser(String username, String email, String password) {
		User user = new User();
		user.setEmail(email);
		user.setUserName(username);
		user.setPassword(password);

		loginService.save(user);

		return "redirect:user";
	}

	@RequestMapping(value = { "app" }, method = RequestMethod.GET)
	public String showApps(final Model model) {
		final List<App> apps = loginService.getAll(App.class);
		for (App app : apps)
			app.setIconData(AppController.encodeByte(app.getAppIcon()));

		model.addAttribute("apps", apps);
		model.addAttribute("categories", loginService.getAll(AppCategory.class));
		return "app";
	}

	@RequestMapping(value = { "app" }, method = RequestMethod.POST)
	public String saveApp(String icon, String pkg, String title, String company, int category,
			RedirectAttributes redirectAttributes) {
		try {
			App app = new App();
			app.setAppCompany(company);
			app.setAppIcon(readBytes(icon));
			app.setAppPackage(pkg);
			app.setAppTitle(title);
			app.setCategory(categoryService.getEntity(AppCategory.class, category));
			
			categoryService.save(app);
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Error saving app: " + e.getMessage());
		}
		return "redirect:app";
	}

	private byte[] readBytes(String icon) throws IOException {
		final URL url = new URL(icon);

		try (InputStream is = url.openStream()) {
			return IOUtils.toByteArray(is);
		}
	}
	
	@RequestMapping(value = { "feed" }, method = RequestMethod.GET)
	public String showTimelines(final Model model) {
		final List<Timeline> allTimelines = categoryService.getAll(Timeline.class);
		for (Timeline timeline : allTimelines)
			if (timeline.getApp() != null)
				timeline.getApp().setIconData(AppController.encodeByte(timeline.getApp().getAppIcon()));
		model.addAttribute("feeds", allTimelines);
		model.addAttribute("allUsers", loginService.getAll(User.class));
		model.addAttribute("allApps", loginService.getAll(App.class));
		return "feed";
	}

	@RequestMapping(value = { "feed" }, method = RequestMethod.POST)
	public String saveTimeline(int app, int user, String comment, boolean like, boolean pin, int rating) {
		final Timeline timeline = new Timeline();
		timeline.setApp(loginService.getEntity(App.class, app));
		timeline.setComments(comment);
		timeline.setLikes(like);
		timeline.setPinned(pin);
		timeline.setRating(rating);
		timeline.setUser(loginService.getEntity(User.class, user));
		
		loginService.save(timeline);
		
		return "redirect:feed";
	}
}
