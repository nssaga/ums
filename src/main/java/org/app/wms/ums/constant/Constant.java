/**
  Constant.java
 ***********************************************************************************************************************
 Description: 	

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 29-Jun-2018		Nawal Sah				Initial Version

 Copyright (c) 2018,
 ***********************************************************************************************************************
 */
package org.app.wms.ums.constant;

public interface Constant {
	String AUTH_HEADER = "Authorization";
	String TOKEN_PREFIX = "Bearer ";

	// String EXCLUDE_URL = "/**";
	String EXCLUDE_URL = "/api/v1/login,/api/v1/forget-password/*,/api/v1/sign-up,/api/v1/verify-account/*,"
			+ "/api/v1/re-generate-verification-link/*";

	int PASSWORD_LENGTH = 10;

	String INVALID_USERNAME = "Invalid username";
	String INVALID_PASSWORD = "Invalid password";
	String INVALID_FIRST_NAME = "Invalid first name";
	String INVALID_LAST_NAME = "Invalid last name";
	String INVALID_PHONE = "Invalid phone number";
	String INVALID_NEW_PASSWORD = "Invalid new password";
	String INVALID_OLD_PASSWORD = "Invalid old password";

	String MSG_PASSWORD_CHANGED = "Password changed successfully";
	String MSG_PASSWORD_RESET = "Password reset successfully, check your registered mail";
	String MSG_SIGNUP = "Signup successfull, check your mail for verification";
	String MSG_LINK_REGENERATED = "Verification link regenarated successfully, check your mail for verification";

}
