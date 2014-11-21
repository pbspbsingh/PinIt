package com.pbs.app.init;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	public Initializer() {
		if (System.getProperty("spring.profiles.active") == null)
			System.setProperty("spring.profiles.active", "prod");
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { WebAppInitializer.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected String getServletName() {
		return "Spring-MVC";
	}

	/*@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		servletContext.addFilter("corsFilter", new SimpleCORSFilter());
	}*/
}
