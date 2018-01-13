package com.md.spacelabs.repository.model;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.appengine.api.datastore.Blob;
import com.md.spacelabs.persistence.AbstractEntity;

@XmlRootElement
public class RepositoryItem extends AbstractEntity{

	private boolean startable = false;
		
	private Long projectID = 0l;
	
	private String userID;
	
	private String groupID;
	
	private Long creationDate;
	
	private Long changeDate;
	
	private boolean template = false;
	
	@XmlTransient
	private Blob contentBlob;
	
	@Transient
	private String content;
	
	
	public boolean isStartable() {
		return startable;
	}

	public void setStartable(boolean startable) {
		this.startable = startable;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	public Long getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Long changeDate) {
		this.changeDate = changeDate;
	}
	
	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public Long getProjectID() {
		return projectID;
	}

	public void setProjectID(Long projectID) {
		this.projectID = projectID;
	}

	@XmlTransient
	public Blob getContentBlob() {
		return contentBlob;
	}

	public void setContentBlob(Blob contentBlob) {
		this.contentBlob = contentBlob;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean template) {
		this.template = template;
	}

}

