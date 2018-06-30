/**
  UMSController.java
 ***********************************************************************************************************************
 Description: 	Controller class provides rest API for user management. 

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 29-Jun-2018		Nawal Sah				Initial Version

 Copyright (c) 2018,
 ***********************************************************************************************************************
 */
package org.app.wms.ums.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.app.wms.commonutils.exception.ProcessingException;
import org.app.wms.ums.entity.User;
import org.app.wms.ums.helper.TokenHelper;
import org.app.wms.ums.model.ChangePassword;
import org.app.wms.ums.model.LoginRequest;
import org.app.wms.ums.model.LoginResponse;
import org.app.wms.ums.model.UserRegistration;
import org.app.wms.ums.service.CustomUserDetailsService;
import org.app.wms.ums.service.LoginService;
import org.app.wms.ums.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class UMSController {

	private static final Logger log = LoggerFactory.getLogger(UMSController.class);

	@Autowired
	TokenHelper tokenHelper;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private LoginService loginService;

	@Autowired
	private UserService userService;

	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		log.debug("Controller login : payload - {}", loginRequest.toString());
		return new ResponseEntity<LoginResponse>(loginService.login(loginRequest), HttpStatus.OK);
	}

	@GetMapping(value = "/refresh")
	public ResponseEntity<?> getRefreshToken(HttpServletRequest request) throws Exception {
		log.debug("Controller getRefreshToken ");
		return new ResponseEntity<LoginResponse>(loginService.getRefreshToken(request), HttpStatus.OK);
	}

	@PutMapping(value = "/change-password")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePassword password) throws ProcessingException {
		log.debug("Controller changePassword ");
		return new ResponseEntity<>(
				userDetailsService.changePassword(password.getOldPassword(), password.getNewPassword()), HttpStatus.OK);
	}

	// forget password
	@GetMapping(value = "/forget-password/{username:.+}")
	public ResponseEntity<?> forgetPassword(@PathVariable(value = "username") final String username)
			throws ProcessingException {
		log.debug("Controller forgetPassword : username - {}", username);
		return new ResponseEntity<>(userDetailsService.forgetPassword(username), HttpStatus.OK);
	}

	// registration
	@PostMapping(value = "/sign-up")
	public ResponseEntity<?> signUp(@Valid @RequestBody UserRegistration registartion) throws Exception {
		log.debug("Controller signUp : payload - {}", registartion.toString());
		return new ResponseEntity<>(loginService.signUp(registartion), HttpStatus.CREATED);
	}

	// re-generate verification link
	@GetMapping(value = "/re-generate-verification-link/{username:.+}")
	public ResponseEntity<?> reGenerateVerificationLink(@PathVariable(value = "username") final String username)
			throws Exception {
		log.debug("Controller reGenerateVerificationLink : payload - {}", username);
		return new ResponseEntity<>(loginService.reGenerateVerificationLink(username), HttpStatus.CREATED);
	}

	// verify account
	@GetMapping("/verify-account/{token:.+}")
	public ResponseEntity<?> verifyAccount(@PathVariable(value = "token") final String token) throws Exception {
		log.debug("Controller Request : verifyAccount : token - {}", token);
		loginService.verifyAccount(token);
		return null;
	}

	// get profile
	@GetMapping("/profile")
	@PreAuthorize("hasRole('USER')")
	public User getProfile(Principal user) {
		log.debug("Controller Request : getProfile : {}", user.getName());
		return this.userService.findByUsername(user.getName());
	}

	// update profile

}

// Save device details OS, device name, browser,
