/**
  LoginServiceImpl.java
 ***********************************************************************************************************************
 Description: 	Login service class

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 29-Jun-2018		Nawal Sah				Initial Version

 Copyright (c) 2018,
 ***********************************************************************************************************************
 */
package org.app.wms.ums.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.app.wms.commonutils.constants.StatusCode;
import org.app.wms.commonutils.exception.NotFoundException;
import org.app.wms.commonutils.exception.ProcessingException;
import org.app.wms.commonutils.model.ResponseObject;
import org.app.wms.ums.constant.AuthorityType;
import org.app.wms.ums.constant.Constant;
import org.app.wms.ums.constant.ErrorCode;
import org.app.wms.ums.constant.TokenType;
import org.app.wms.ums.controller.UMSController;
import org.app.wms.ums.entity.Authority;
import org.app.wms.ums.entity.User;
import org.app.wms.ums.helper.TokenHelper;
import org.app.wms.ums.model.LoginRequest;
import org.app.wms.ums.model.LoginResponse;
import org.app.wms.ums.model.UserRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginServiceImpl implements LoginService {

	private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Value("${account.verification.success.redirect.url}")
	private String verificationSuccessRedirectURL;

	@Value("${account.verification.error.redirect.url}")
	private String verificationErrorRedirectURL;
	
	@Autowired
	private TokenHelper tokenHelper;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private HttpServletResponse response;

	@Override
	public LoginResponse login(LoginRequest authenticationRequest) throws ServletException {
		// Perform the security
		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
						authenticationRequest.getPassword()));

		// Inject into security context
		SecurityContextHolder.getContext().setAuthentication(authentication);
		User user = (User) authentication.getPrincipal();
		// TODO save access/refresh and verify every api call

		String accessToken = tokenHelper.getValidationToken(user.getUsername(), "app_name", "access-token",
				TokenType.ACCESS);
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setAccessToken(accessToken);
		loginResponse.setRefreshToken(
				tokenHelper.getValidationToken(user.getUsername(), "app_name", "refresh-token", TokenType.REFRESH));
		loginResponse
				.setScope(user.getAuthorities().stream().map(role -> role.getAuthority()).collect(Collectors.toList()));
		return loginResponse;
	}

	@Override
	public LoginResponse getRefreshToken(HttpServletRequest request) throws Exception {
		String refreshToken = tokenHelper.getToken(request);

		LoginResponse loginResponse = new LoginResponse();
		if (refreshToken != null) {
			// Validate token
			String username = tokenHelper.getAllClaimsFromToken(refreshToken, TokenType.REFRESH).getId();
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			String accessToken = tokenHelper.getValidationToken(username, "app_name", "access-token", TokenType.ACCESS);
			loginResponse.setAccessToken(accessToken);
			loginResponse.setRefreshToken(
					tokenHelper.getValidationToken(username, "app_name", "refresh-token", TokenType.REFRESH));
			loginResponse.setScope(userDetails.getAuthorities().stream().map(role -> role.getAuthority())
					.collect(Collectors.toList()));
		}
		return loginResponse;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject signUp(UserRegistration userRegistration) throws Exception {
		User userDetails = userService.findByUsername(userRegistration.getUsername());
		if (userDetails != null) {
			log.debug("User - {} already registered", userRegistration.getUsername());
			throw new ProcessingException(ErrorCode.ALREADY_REGISTERED.getCode(),
					ErrorCode.ALREADY_REGISTERED.getMessage());
		}
		User user = new User();
		BeanUtils.copyProperties(userRegistration, user);
		user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
		user.setEnabled(false);

		List<Authority> authorities = new ArrayList<>();
		Authority authority = new Authority(AuthorityType.ROLE_USER);
		authority.setUser(user);
		authorities.add(authority);
		user.setAuthorities(authorities);

		userService.save(user);
		// send a mail for account verification, if unable to mail then roll back the
		// registration
		String verificationLink = generateVerificationLink(userRegistration.getUsername());
		log.debug("{}", verificationLink);
		log.debug("User [{}] registered successfully", userRegistration.toString());
		return new ResponseObject(true, Constant.MSG_SIGNUP);
	}

	@Override
	public ResponseObject reGenerateVerificationLink(String username) throws Exception {
		User userDetails = userService.findByUsername(username);
		if (userDetails == null) {
			log.debug("User [{}] not registered", username);
			throw new NotFoundException(ErrorCode.INVALID_USERNAME.getCode(), ErrorCode.INVALID_USERNAME.getMessage());
		}

		if (userDetails.isEnabled()) {
			log.debug("Username [{}] is already verified", username);
			throw new ProcessingException(ErrorCode.USER_VERIFIED.getCode(), ErrorCode.USER_VERIFIED.getMessage());
		}
		String verificationLink = generateVerificationLink(username);
		log.debug("{}", verificationLink);
		// Send Mail
		log.debug("Verification link mailed successfully");
		return new ResponseObject(true, Constant.MSG_LINK_REGENERATED);
	}

	@Override
	public void verifyAccount(String verificationToken) throws Exception {
		try {
			String username = tokenHelper.getAllClaimsFromToken(verificationToken, TokenType.VERIFICATION).getId();
			User user = userService.findByUsername(username);
			if (user == null) {
				throw new NotFoundException(StatusCode.USER_NOT_FOUND);
			}
			user.setEnabled(true);
			userService.save(user);

			log.debug("Username [{}] - verified and enabled", username);
			response.sendRedirect(verificationSuccessRedirectURL);
		}catch (Exception e) {
			log.error("Account verification failed", e);
			response.sendRedirect(verificationErrorRedirectURL);
		}
		
	}

	/**
	 * Generate verification link
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	private String generateVerificationLink(String username) throws Exception {
		String accountVerificationToken = tokenHelper.getValidationToken(username, "app-name",
				"account-verification-token", TokenType.VERIFICATION);
		return ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(UMSController.class).verifyAccount(accountVerificationToken))
				.toString();
	}

}
