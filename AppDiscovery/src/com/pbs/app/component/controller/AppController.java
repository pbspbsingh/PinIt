package com.pbs.app.component.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pbs.app.component.service.AppCategoryService;
import com.pbs.app.component.service.AppService;
import com.pbs.app.entity.App;
import com.pbs.app.entity.AppCategory;
import com.pbs.app.entity.Timeline;
import com.pbs.app.entity.User;
import com.pbs.app.model.TimelineModel;

@RestController
@RequestMapping(value = "/user")
public class AppController {

	@Autowired
	private AppService appService;

	@Autowired
	private AppCategoryService categoryService;

	@RequestMapping(value = "/mytimeline")
	public MyTimeLine readMyTimeline(int userId) {
		final List<TimelineModel> timelines = createList(appService.getTimelinesByUserId(userId));

		int totalLikes = 0, totalPins = 0;
		for (TimelineModel timelineModel : timelines) {
			int appLikes = 0, appPins = 0;

			final List<Timeline> feeds = appService.getAllTimelinesForApp(timelineModel.getAppId());
			for (Timeline timeline : feeds) {
				if (timeline.isLikes())
					appLikes++;
				if (timeline.isPinned())
					appPins++;
			}
			timelineModel.setLikesCount(appLikes);
			timelineModel.setPinCounts(appPins);
			totalLikes += appLikes;
			totalPins += appPins;
		}
		MyTimeLine line = new MyTimeLine();
		line.setTotalLikes(totalLikes);
		line.setTotalPins(totalPins);
		line.setTimelines(timelines);
		return line;
	}

	private List<TimelineModel> createList(List<Timeline> timelines) {

		final Map<String, TimelineModel> data = new LinkedHashMap<>();
		for (Timeline timeline : timelines) {
			if (data.get(timeline.getApp().getAppPackage()) == null) {
				final TimelineModel model = new TimelineModel();
				model.setAppCompany(timeline.getApp().getAppCompany());
				model.setAppIcon(encodeByte(timeline.getApp().getAppIcon()));
				model.setAppId(timeline.getApp().getAppId());
				model.setAppPackage(timeline.getApp().getAppPackage());
				model.setAppTitle(timeline.getApp().getAppTitle());
				model.setComment(timeline.getComments());
				model.setLikesCount(0);
				model.setPinCounts(0);
				model.setRatings(0);
				model.setNumberOfUsers(0);
				model.setUserId(timeline.getUser().getUserId());
				model.setUserName(timeline.getUser().getUserName());

				data.put(timeline.getApp().getAppPackage(), model);
			}

			final TimelineModel model = data.get(timeline.getApp().getAppPackage());
			if (timeline.isLikes())
				model.setLikesCount(model.getLikesCount() + 1);
			if (timeline.isPinned())
				model.setPinCounts(model.getPinCounts() + 1);

			model.setNumberOfUsers(model.getNumberOfUsers() + 1);
			model.setRatings(model.getRatings() + timeline.getRating());
		}
		return new ArrayList<>(data.values());
	}

	@RequestMapping(value = "/suggestions")
	public List<Suggeston> getSuggestedApps(int appId) {
		AppCategory appCategory = appService.getAppCategory(appId);
		if (appCategory == null)
			appCategory = categoryService.getCategoryByName("Casual");

		List<App> allApsInCat = appService.getAllAppsInCategory(appCategory.getAppCategoryId());
		if (allApsInCat.size() < 3)
			allApsInCat = appService.getAllApps();

		Collections.shuffle(allApsInCat);

		final List<Suggeston> suggestions = new ArrayList<>(3);
		for (int i = 0; i < allApsInCat.size() && i < 3; i++) {
			Suggeston model = new Suggeston();
			model.setAppTitle(allApsInCat.get(i).getAppTitle());
			model.setAppPackage(allApsInCat.get(i).getAppPackage());
			model.setAppIcon(encodeByte(allApsInCat.get(i).getAppIcon()));

			suggestions.add(model);
		}
		return suggestions;
	}

	public static String encodeByte(final byte[] appIcon) {
		return Base64Utils.encodeToString(appIcon);
	}

	@RequestMapping(value = "/timelines")
	public List<TimelineModel> readTimelines(int userId) {
		final List<Timeline> timelines = appService.getTimelineInHisCategory(userId);

		return createList(timelines);
	}

	static class Suggeston {
		private String appTitle;
		private String appPackage;
		private String appIcon;

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
	}

	static class MyTimeLine {
		private int totalLikes;
		private int totalPins;
		private List<TimelineModel> timelines;

		public int getTotalLikes() {
			return totalLikes;
		}

		public void setTotalLikes(int totalLikes) {
			this.totalLikes = totalLikes;
		}

		public int getTotalPins() {
			return totalPins;
		}

		public void setTotalPins(int totalPins) {
			this.totalPins = totalPins;
		}

		public List<TimelineModel> getTimelines() {
			return timelines;
		}

		public void setTimelines(List<TimelineModel> timelines) {
			this.timelines = timelines;
		}
	}

	@RequestMapping(value = "comment")
	public boolean saveTimeline(int userId, int appId, String comment, boolean isLiked, int rating) {
		final Timeline timeline = new Timeline();
		App app = appService.getEntity(App.class, appId);
		if (app == null)
			return false;
		timeline.setApp(app);

		User user = appService.getEntity(User.class, userId);
		if (user == null)
			return false;
		timeline.setUser(user);

		timeline.setComments(comment);
		timeline.setLikes(isLiked);
		timeline.setPinned(true);
		timeline.setRating(rating);
		appService.save(timeline);
		return true;
	}
}
