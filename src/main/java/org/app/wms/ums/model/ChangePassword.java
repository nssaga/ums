/**
 ForgetPassword.java
 ***********************************************************************************************************************                                               
 Description: 	Model class  for change password

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

public class ChangePassword {

	@NotBlank(message = Constant.INVALID_OLD_PASSWORD)
	private String oldPassword;

	@NotBlank(message = Constant.INVALID_NEW_PASSWORD)
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
