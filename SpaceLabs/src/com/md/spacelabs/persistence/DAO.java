package com.md.spacelabs.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;
import com.md.spacelabs.repository.model.RepositoryItem;
import com.md.spacelabs.repository.model.RepositoryProject;
import com.md.spacelabs.usermgmt.model.ApplicationUser;
import com.md.spacelabs.usermgmt.model.Invitation;
import com.md.spacelabs.usermgmt.model.UserGroup;
import com.md.spacelabs.usermgmt.model.UserGroupAssignment;

@SuppressWarnings({ "unchecked", "rawtypes" })
public final class DAO<E extends AbstractBaseEntity> extends DAOBase {

	static {
		registerWithName("RepositoryProject", RepositoryProject.class);
		registerWithName("RepositoryItem", RepositoryItem.class);
		registerWithName("User", ApplicationUser.class);
		registerWithName("UserGroup", UserGroup.class);
		registerWithName("UserGroupAssignment", UserGroupAssignment.class);
		registerWithName("Invitation", Invitation.class);
	}

	private static HashMap<String, Class> registeredClasses;

	public static void registerWithName(String name, Class<? extends AbstractBaseEntity> clz) {
		if (registeredClasses == null)
			registeredClasses = new HashMap<String, Class>();

		if (!registeredClasses.containsKey(name)) {
			registeredClasses.put(name, clz);
			ObjectifyService.register(clz);
		}
	}

	public static DAO getByName(String name) {
		if (registeredClasses == null)
			registeredClasses = new HashMap<String, Class>();
		if (registeredClasses.containsKey(name)) {
			Class clz = registeredClasses.get(name);
			return new DAO(clz);

		}
		return null;
	}

	private Objectify ofy;
	private Class clz;

	public DAO(Class<? extends AbstractBaseEntity> clz) {
		super();
		this.clz = clz;
		this.ofy = ofy();
	}

	public E get(Long id) {
		try {
			return (E) ofy.get(clz, id);
		} catch (NotFoundException e) {
			return null;
		}
	}

	public List<E> get(Long... ids) {
		Map<Long, E> fetched = ofy.get(clz, ids);
		Iterator<E> it = fetched.values().iterator();
		ArrayList<E> list = new ArrayList<E>();
		while (it.hasNext()) {
			E obj = it.next();
			list.add(obj);
		}
		return list;
	}

	public Long create(E object) {
		Key<E> key = ofy.put(object);
		return key.getId();
	}

	public void update(E object) {
		// TOdo Check if exists
		ofy.put(object);
	}

	public void delete(E... objects) {
		ofy.delete((Object[]) objects);
	}

	public void deleteIDs(Long... ids) {
		ofy.delete((Object[]) ids);
	}

	public void delete(List<RepositoryItem> repoContentList) {
		ofy.delete(repoContentList);
	}

	public List<E> queryAll() {
		Query<E> queryResult = ofy.query(clz);
		Iterator<E> it = queryResult.iterator();
		ArrayList<E> list = new ArrayList<E>();
		while (it.hasNext()) {
			E obj = it.next();
			list.add(obj);
		}
		return list;
	}

	public List<E> query(String condition, Object value) {
		Query<E> queryResult = ofy.query(clz).filter(condition, value);
		Iterator<E> it = queryResult.iterator();
		ArrayList<E> list = new ArrayList<E>();
		while (it.hasNext()) {
			E obj = it.next();
			list.add(obj);
		}

		return list;

	}

	public List<E> queryAndOrder(String condition, Object value, String orderField) {
		Query<E> queryResult = ofy.query(clz).filter(condition, value).order(orderField);
		Iterator<E> it = queryResult.iterator();
		ArrayList<E> list = new ArrayList<E>();
		while (it.hasNext()) {
			E obj = it.next();
			list.add(obj);
		}

		return list;

	}

}