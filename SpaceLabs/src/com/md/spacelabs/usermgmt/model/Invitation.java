package com.md.spacelabs.usermgmt.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.googlecode.objectify.annotation.Indexed;
import com.md.spacelabs.persistence.AbstractEntity;

@XmlRootElement
public class Invitation extends AbstractEntity {

	@Indexed
	private String userID;

	private Date sentDate;

	private String reason;

	private String email;

	public Invitation() {

	}

	public Invitation(String userID, String email, String fullanme, String reason) {
		super.setName(fullanme);
		this.userID = userID;
		this.email = email;
		this.reason = reason;
		this.sentDate = new Date();
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
