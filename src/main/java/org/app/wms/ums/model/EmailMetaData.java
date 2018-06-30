/**
 EmailMetaData.java
 ***********************************************************************************************************************                                               
 Description: 	Model class for Email request

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 29-Jun-2018		Nawal Sah				Initial Version

 Copyright (c) 2018,
 ***********************************************************************************************************************
 */
package org.app.wms.ums.model;

public class EmailMetaData {

	private String receiver;
	private String subject;
	private String message;

	public EmailMetaData() {
	}

	public EmailMetaData(String receiver, String subject, String message) {
		super();
		this.receiver = receiver;
		this.subject = subject;
		this.message = message;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "EmailMetaData [receiver=" + receiver + ", subject=" + subject + ", message=" + "XXXXX" + "]";
	}

}
