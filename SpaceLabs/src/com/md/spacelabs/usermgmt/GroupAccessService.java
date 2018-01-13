package com.md.spacelabs.usermgmt;

import java.util.ArrayList;
import java.util.List;

import com.md.spacelabs.persistence.DAO;
import com.md.spacelabs.usermgmt.model.UserGroup;
import com.md.spacelabs.usermgmt.model.UserGroupAssignment;

public class GroupAccessService {

	public List<UserGroup> getRolesOfUser(String userID){
		
		@SuppressWarnings("unchecked")
		DAO<UserGroupAssignment> roleDAO = DAO.getByName("UserGroupAssignment");
		List<UserGroupAssignment> rolesAss = roleDAO.query("userID", userID);
		
		ArrayList<UserGroup> userRoles = new ArrayList<UserGroup>();
		for(UserGroupAssignment roleAss : rolesAss){
			UserGroup role = roleAss.getRole();
			userRoles.add(role);
		}
		
		return userRoles;
	}
	
	public List<UserGroup> getAllRoles(){
		
		@SuppressWarnings("unchecked")
		DAO<UserGroup> roleDAO = DAO.getByName("UserGroup");
		List<UserGroup> roles = roleDAO.queryAll();
		
		return roles;
	}
	
	
}
