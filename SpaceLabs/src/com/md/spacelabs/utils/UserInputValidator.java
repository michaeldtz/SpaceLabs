package com.md.spacelabs.utils;

import org.apache.commons.validator.routines.EmailValidator;

public class UserInputValidator {

	public static boolean validateEmail(String email) {
		if (email == null)
			return false;
		return EmailValidator.getInstance().isValid(email);
	}

	public static boolean validateNotEmpty(String fullname) {
		if (fullname == null)
			return false;

		if (fullname.equals(""))
			return false;

		return true;
	}

}
