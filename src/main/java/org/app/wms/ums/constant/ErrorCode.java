/**
  ErrorCode.java
 ***********************************************************************************************************************
 Description: 	

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 13-Jun-2018		Nawal Sah				Initial Version

 Copyright (c) 2018,
 ***********************************************************************************************************************
 */
package org.app.wms.ums.constant;

public enum ErrorCode {
	
	INVALID_USERNAME("ERR_UMS_001","Invalid Username"),
	ALREADY_REGISTERED("ERR_UMS_002","Username already registered"),
	USER_VERIFIED("ERR_UMS_003","Username is already verified");
	
	private String code;

	private String message;

	private ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	
}
