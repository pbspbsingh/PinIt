package com.pbs.app.component.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractService {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private SessionFactory sessionFactory;

	private HibernateTemplate hibernateTemplate;

	@PostConstruct
	private void createTemplate() {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	protected Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	protected HibernateTemplate getTemplate() {
		return this.hibernateTemplate;
	}

	@Transactional
	public Serializable save(Object o) {
		return getTemplate().save(o);
	}

	@Transactional(readOnly = true)
	public <T> T getEntity(Class<T> clzz, Integer id) {
		return getTemplate().get(clzz, id);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <T> List<T> getAll(Class<T> claxx) {
		return getCurrentSession().createQuery("from " + claxx.getSimpleName()).list();
	}
}
