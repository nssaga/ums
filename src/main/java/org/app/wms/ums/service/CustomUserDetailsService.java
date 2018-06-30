/**
  CustomUserDetailsService.java
 ***********************************************************************************************************************
 Description: 	Custom user details.

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 29-Jun-2018		Nawal Sah				Initial Version

 Copyright (c) 2018,
 ***********************************************************************************************************************
 */
package org.app.wms.ums.service;

import org.app.wms.commonutils.constants.StatusCode;
import org.app.wms.commonutils.exception.ProcessingException;
import org.app.wms.commonutils.model.ResponseObject;
import org.app.wms.commonutils.util.RandomStringUtil;
import org.app.wms.ums.constant.Constant;
import org.app.wms.ums.entity.User;
import org.app.wms.ums.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(StatusCode.USER_NOT_FOUND.getMessage());
		} else {
			return user;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public ResponseObject changePassword(String oldPassword, String newPassword) throws ProcessingException {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		String username = currentUser.getName();
		try {
			log.debug("Re-authenticating user '" + username + "' for password change request.");
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
		} catch (AuthenticationException e) {
			log.debug("No authentication manager set. can't change Password!");
			throw new ProcessingException(HttpStatus.FORBIDDEN.name(), e.getMessage());
		}

		log.debug("Changing password for user '" + username + "'");
		User user = (User) loadUserByUsername(username);
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepo.save(user);
		return new ResponseObject(true, Constant.MSG_PASSWORD_CHANGED);
	}

	@Transactional(rollbackFor = Exception.class)
	public ResponseObject forgetPassword(String username) {
		log.debug("Checking user [{}] in system.", username);
		User user = (User) loadUserByUsername(username);
		log.debug("Resetting password for user [{}] ", username);
		String newPassword = RandomStringUtil.generateRandomString(Constant.PASSWORD_LENGTH);
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepo.save(user);

		// Send mail with Reset password.
		log.debug("Reset password sent successfully [{}]", username);
		return new ResponseObject(true, Constant.MSG_PASSWORD_RESET);
	}
}
