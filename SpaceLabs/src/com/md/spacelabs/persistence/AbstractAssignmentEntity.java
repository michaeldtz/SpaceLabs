package com.md.spacelabs.persistence;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AbstractAssignmentEntity extends AbstractBaseEntity {
	
	private Boolean active;

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
}
