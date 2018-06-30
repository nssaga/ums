/**
  LoginService.java
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
package org.app.wms.ums.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.app.wms.commonutils.exception.NotFoundException;
import org.app.wms.commonutils.exception.ProcessingException;
import org.app.wms.commonutils.model.ResponseObject;
import org.app.wms.ums.model.LoginRequest;
import org.app.wms.ums.model.LoginResponse;
import org.app.wms.ums.model.UserRegistration;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface LoginService {

	LoginResponse login(LoginRequest authenticationRequest) throws ServletException;

	LoginResponse getRefreshToken(HttpServletRequest request)
			throws ServletException, JsonProcessingException, IOException, Exception;

	ResponseObject signUp(UserRegistration userRegistration)
			throws ProcessingException, IOException, ServletException, NotFoundException, Exception;

	void verifyAccount(String verificationToken) throws ServletException, NotFoundException, IOException, Exception;

	ResponseObject reGenerateVerificationLink(String username)
			throws NotFoundException, ProcessingException, IOException, ServletException, Exception;
}
