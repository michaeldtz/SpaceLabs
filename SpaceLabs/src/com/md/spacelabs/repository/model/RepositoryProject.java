package com.md.spacelabs.repository.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.md.spacelabs.persistence.AbstractEntity;
import com.md.spacelabs.persistence.AuthorizationModeEnum;
import com.md.spacelabs.persistence.EntityAuthorization;

@XmlRootElement
public class RepositoryProject extends AbstractEntity implements EntityAuthorization{
	
	private Long startupItem;
	
	private Boolean template = false;
	
	private int changeAuth = AuthorizationModeEnum.PRIVATE.getValue();
	
	private int displayAuth = AuthorizationModeEnum.PUBLIC.getValue();
	
	private int executeAuth = AuthorizationModeEnum.PRIVATE.getValue();
	
	private String userID;
	
	private String groupID;
	
	private Boolean core;
	
	private Long creationDate;
	
	private Long changeDate;


	public Long getStartupItem() {
		return startupItem;
	}

	public void setStartupItem(Long startupItem) {
		this.startupItem = startupItem;
	}

	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean template) {
		this.template = template;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
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

	public int getChangeAuth() {
		return changeAuth;
	}

	public void setChangeAuth(int changeAuth) {
		this.changeAuth = changeAuth;
	}

	public int getDisplayAuth() {
		return displayAuth;
	}

	public void setDisplayAuth(int displayAuth) {
		this.displayAuth = displayAuth;
	}

	public Boolean getCore() {
		return core;
	}

	public void setCore(Boolean core) {
		this.core = core;
	}

	@Override
	public int getExecuteAuth() {
		return executeAuth;
	}

	@Override
	public void setExecuteAuth(int executeAuth) {
		this.executeAuth = executeAuth;
	}


	

	

}
