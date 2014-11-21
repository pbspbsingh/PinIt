package com.pbs.app.component.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pbs.app.entity.User;

@Component
public class LoginService extends AbstractService {

	@Transactional(readOnly = true)
	public User getUserByEmail(String email) {
		 List<?> list = getCurrentSession()
							.createQuery("from User usr where usr.email=:email")
							.setString("email", email)
							.list();
		return list.size() > 0 ? (User) list.get(0) : null;
	}
	
	@Transactional(readOnly = true)
	public User validateUser(String email, String password) {
		List<?> users = getCurrentSession()
							.createQuery("from User usr where usr.email=:email and usr.password=:password")
							.setString("email", email)
							.setString("password", password)
							.list();
		return users.size() == 1 ? (User) users.get(0) : null;
	}
}
