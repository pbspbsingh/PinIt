package com.pbs.app.component.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pbs.app.entity.AppCategory;

@Component
public class AppCategoryService extends AbstractService {
	
	@Transactional
	public void insertCategories() throws IOException{
		try(InputStream is = getClass().getResourceAsStream("/categories");
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader reader = new BufferedReader(isr)){
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (getCategoryByName(line) == null) {
					logger.info("Inserting category: " + line + " into db.");
					getTemplate().save(new AppCategory(line));
				} else
					logger.info("Category already exists in db");
			}
		}
	}
	
	@Transactional(readOnly = true)
	public AppCategory getCategoryByName(String name) {
		List<?> list = getCurrentSession()
						.createQuery("from AppCategory ac where ac.category=:name")
						.setParameter("name", name)
						.list();
		return list.size() > 0 ? (AppCategory) list.get(0) : null;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<AppCategory> getUsersCategory(int userId) {
		return getCurrentSession()
				.createQuery("select fc.appCategory from FollowedCategory fc where fc.user.userId=:userId")
				.setInteger("userId", userId)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<AppCategory> getAllCategories() {
		return getCurrentSession()
				.createQuery("from AppCategory")
				.list();
	}

	@Transactional
	public void removeMapping(int userId) {
		getCurrentSession()
			.createQuery("delete from FollowedCategory fc where fc.user.userId=:userId")
			.setInteger("userId", userId)
			.executeUpdate();
		
	} 
}
