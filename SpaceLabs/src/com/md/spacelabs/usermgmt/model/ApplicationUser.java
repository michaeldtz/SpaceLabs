package com.md.spacelabs.usermgmt.model;

import com.googlecode.objectify.annotation.Indexed;
import com.md.spacelabs.persistence.AbstractBaseEntity;

public class ApplicationUser extends AbstractBaseEntity {

	@Indexed
	private String userID;
	
	private String emailAdress;
	
	private String fullname;

	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getEmailAdress() {
		return emailAdress;
	}

	public void setEmailAdress(String emailAdress) {
		this.emailAdress = emailAdress;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	
}
