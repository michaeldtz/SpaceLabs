package com.md.spacelabs.usermgmt.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.md.spacelabs.persistence.AbstractEntity;

@XmlRootElement
public class UserGroup extends AbstractEntity {

	private String technicalName;
	
	private String description;
	
	private boolean isCoreGroup;
	
	public String getTechnicalName() {
		return technicalName;
	}

	public void setTechnicalName(String technicalName) {
		this.technicalName = technicalName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCoreGroup() {
		return isCoreGroup;
	}

	public void setCoreGroup(boolean isCoreGroup) {
		this.isCoreGroup = isCoreGroup;
	}
	
}
