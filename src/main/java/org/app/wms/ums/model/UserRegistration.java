/**
 UserRegistration.java
 ***********************************************************************************************************************                                               
 Description: 	Model class for user registration request.

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 29-Jun-2018		Nawal Sah				Initial Version

 Copyright (c) 2018,
 ***********************************************************************************************************************
 */
package org.app.wms.ums.model;

import javax.validation.constraints.Pattern;

import org.app.wms.commonutils.constants.CommonConstants;
import org.app.wms.ums.constant.Constant;
import org.hibernate.validator.constraints.NotEmpty;

public class UserRegistration {

	@NotEmpty(message = Constant.INVALID_USERNAME)
	@Pattern(regexp = CommonConstants.EMAIL_REGEX, message = Constant.INVALID_USERNAME)
	private String username;

	@NotEmpty(message = Constant.INVALID_PASSWORD)
	private String password;

	@NotEmpty(message = Constant.INVALID_FIRST_NAME)
	@Pattern(regexp = CommonConstants.FIRST_NAME_REGEX, message = Constant.INVALID_FIRST_NAME)
	private String firstName;

	@Pattern(regexp = CommonConstants.LAST_NAME_REGEX, message = Constant.INVALID_LAST_NAME)
	private String lastName;

	@NotEmpty(message = Constant.INVALID_PHONE)
	@Pattern(regexp = CommonConstants.MOBILE_REGEX, message = Constant.INVALID_PHONE)
	private String phoneNumber;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	private String address;
	private String state;

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "UserRegistration [username=" + username + ", password=xxxxx, firstName=" + firstName + ", lastName="
				+ lastName + ", phone=" + phoneNumber + ", address=" + address + ", state=" + state + "]";
	}

}
