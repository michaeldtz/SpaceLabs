package com.md.spacelabs.usermgmt.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.md.spacelabs.persistence.AbstractAssignmentEntity;

@XmlRootElement
public class UserGroupAssignment extends AbstractAssignmentEntity{

	private UserGroup role;
	
	private String userID;

	public UserGroup getRole() {
		return role;
	}

	public void setRole(UserGroup role) {
		this.role = role;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
}
