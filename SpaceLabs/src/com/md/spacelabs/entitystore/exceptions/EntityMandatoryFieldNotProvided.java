package com.md.spacelabs.entitystore.exceptions;

public class EntityMandatoryFieldNotProvided extends Exception {

	private String fieldName;

	public EntityMandatoryFieldNotProvided(String fieldName) {
		this.fieldName = fieldName;
	}

}
