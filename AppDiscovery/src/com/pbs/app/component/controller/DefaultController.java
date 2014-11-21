package com.pbs.app.component.controller;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pbs.app.component.service.AppCategoryService;
import com.pbs.app.component.service.AppService;
import com.pbs.app.entity.App;
import com.pbs.app.model.HelloModel;

@RestController
public class DefaultController {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private AppCategoryService categoryService;

	@Autowired
	private AppService appService;

	private static boolean isDBInitialized = false;

	@RequestMapping(value = "/")
	public HelloModel sayHello() throws IOException {
		logger.info("Got a ping from app.");
		if (!isDBInitialized) {
			logger.info("Database is not yet initilalized, Now starting the process.");
			categoryService.insertCategories();
			isDBInitialized = true;
		}
		return new HelloModel("Prashant", "Hello World!");
	}

	@RequestMapping(value = "registerApp")
	public int registerNewApp(String appTitle, String packageName, String appIcon) {
		logger.info("Got an app registration request.");
		App app = appService.findAppByPackage(packageName);
		if (app != null) {
			logger.info("App is already registered: " + app);
			return app.getAppId();
		}
		app = new App();
		app.setAppCompany("Some Company");
		app.setAppIcon(Base64.decodeBase64(appIcon));
		app.setAppPackage(packageName);
		app.setAppTitle(appTitle);
		app.setCategory(categoryService.getCategoryByName("Casual"));
		app.setIconData(appIcon);
		appService.save(app);
		return app.getAppId();
	}
}
