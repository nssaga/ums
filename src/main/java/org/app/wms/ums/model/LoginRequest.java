/**
  LoginRequest.java
 ***********************************************************************************************************************
 Description: 	Model class used to map credential from the user.

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 29-Jun-2018		Nawal Sah				Initial Version

 Copyright (c) 2018,
 ***********************************************************************************************************************
 */
package org.app.wms.ums.model;

import org.app.wms.ums.constant.Constant;
import org.hibernate.validator.constraints.NotBlank;

public class LoginRequest {

	@NotBlank(message = Constant.INVALID_USERNAME)
	private String username;

	@NotBlank(message = Constant.INVALID_PASSWORD)
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginRequest [username=" + username + ", password=" + "xxxxxx" + "]";
	}

}
