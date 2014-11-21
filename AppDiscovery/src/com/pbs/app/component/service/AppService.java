package com.pbs.app.component.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pbs.app.entity.App;
import com.pbs.app.entity.AppCategory;
import com.pbs.app.entity.Timeline;

@Component
public class AppService extends AbstractService {

	@Autowired
	private AppCategoryService categoryService;

	@Transactional
	public void saveApps(List<App> dbappList) {
		for (App app : dbappList) {
			if (app.getCategory() == null) {
				logger.warn("No category found for " + app);
				app.setCategory(categoryService.getCategoryByName("Casual"));
			}
			if (findAppByPackage(app.getAppPackage()) == null) {
				logger.warn("App is already there in db, skipping saving.");
				getTemplate().save(app);
			}
		}
	}

	@Transactional(readOnly = true)
	public App findAppByPackage(String packageName) {
		List<?> list = getCurrentSession()
							.createQuery("from App ap where ap.appPackage=:name")
							.setString("name", packageName)
							.list();
		return list.size() > 0 ? (App) list.get(0) : null;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<App> getAllApps() {
		return getCurrentSession().createQuery("from App").list();
	}
 
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Timeline> getTimelinesByUserId(int userId) {
		return getCurrentSession()
				.createQuery("from Timeline tl where tl.user.userId=:userId order by tl.time desc")
				.setInteger("userId", userId)
				.list();
	}
	
	@Transactional(readOnly = true)
	public AppCategory getAppCategory(int appId){
		List<?> cats =  getCurrentSession()
						.createQuery("select ap.category from App ap where ap.appId=:appId")
						.setInteger("appId", appId)
						.list();
		return cats.size() > 0 ? (AppCategory) cats.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<App> getAllAppsInCategory(int appCategoryId) {
		return getCurrentSession()
				.createQuery("from App ap where ap.category.appCategoryId=:catId")
				.setInteger("catId", appCategoryId)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Timeline> getTimelineInHisCategory(int userId) {
		return getCurrentSession()
				.createQuery("from Timeline tl where tl.app.category.appCategoryId in ("
						+ "select fc.appCategory.appCategoryId from FollowedCategory fc where fc.user.userId=:userId"
						+ ") order by tl.time desc")
				.setInteger("userId", userId)
				.list();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Timeline> getAllTimelinesForApp(int appId) {
		return getCurrentSession()
				.createQuery("from Timeline tl where tl.app.appId=:appId order by tl.time desc")
				.setInteger("appId", appId)
				.list();
	}
}
