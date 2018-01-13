package com.md.spacelabs.persistence;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AbstractEntity extends AbstractBaseEntity {
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
