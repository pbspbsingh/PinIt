package com.pbs.app.component.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pbs.app.component.service.LoginService;
import com.pbs.app.entity.User;

@RestController
public class LoginController {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private LoginService loginService;

	@RequestMapping(value = "/signup")
	public SignupStatus signUp(User newUser) {
		User user = loginService.getUserByEmail(newUser.getEmail());
		if (user != null) {
			logger.error("User alreay exists " + user);
			return new SignupStatus(false, "Email is already used, please use another email.");
		}

		int savedUser = (int) loginService.save(newUser);
		logger.info(newUser + " User has been saved: " + savedUser);
		return new SignupStatus(true, String.valueOf(savedUser));
	}

	public static class SignupStatus {
		private final boolean registered;
		private final String msg;

		public SignupStatus(boolean registered, String msg) {
			super();
			this.registered = registered;
			this.msg = msg;
		}

		public boolean isRegistered() {
			return registered;
		}

		public String getMsg() {
			return msg;
		}
	}

	@RequestMapping(value = "/signin")
	public SignInStatus signIn(String email, String password) {
		final User user = loginService.validateUser(email, password);
		if (user != null) {
			logger.info("User detail matched, loggin in now");
			return new SignInStatus(true, user);
		} else {
			logger.info("No user found with above credentials");
			return new SignInStatus(false, null);
		}
	}

	public static class SignInStatus {
		private final boolean valid;
		private final User user;

		public SignInStatus(boolean valid, User user) {
			super();
			this.valid = valid;
			this.user = user;
		}

		public boolean isValid() {
			return valid;
		}

		public User getUser() {
			return user;
		}

	}
}
