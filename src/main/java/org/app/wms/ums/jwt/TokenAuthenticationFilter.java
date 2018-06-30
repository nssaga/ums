/**
  TokenAuthenticationFilter.java
 ***********************************************************************************************************************
 Description: 	Token authentication filter

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 29-Jun-2018		Nawal Sah				Initial Version

 Copyright (c) 2018,
 ***********************************************************************************************************************
 */
package org.app.wms.ums.jwt;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.app.wms.commonutils.constants.StatusCode;
import org.app.wms.commonutils.exception.ErrorResponse;
import org.app.wms.commonutils.exception.InvalidTokenException;
import org.app.wms.ums.constant.TokenType;
import org.app.wms.ums.helper.TokenHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

	@Autowired
	private TokenHelper tokenHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	public TokenAuthenticationFilter(TokenHelper tokenHelper, UserDetailsService userDetailsService) {
		this.tokenHelper = tokenHelper;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.debug("Inside token filter");

		TokenType tokenType;
		String api = request.getRequestURI();

		if (api.contains("verify-account")) {
			tokenType = TokenType.VERIFICATION;
		} else if (api.contains("refresh")) {
			tokenType = TokenType.REFRESH;
		} else {
			tokenType = TokenType.ACCESS;
		}

		String username = null;
		try {
			String authToken = tokenHelper.getToken(request);

			if (authToken != null) {
				// Get username from token.
				username = tokenHelper.getAllClaimsFromToken(authToken, tokenType).getId();
				if (username != null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					// Token authentication for user details
					TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
					authentication.setToken(authToken);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
			chain.doFilter(request, response);
		} catch (MalformedJwtException e) {
			log.error("Malformed Jwt Exception", e);
			handleJwtException(response, "Malformed token");

		} catch (ExpiredJwtException e) {
			log.error("Expired Jwt Exception.", e);
			handleJwtException(response, "Token expired");

		} catch (SignatureException e) {
			log.error("Malformed Signature Exception.", e);
			handleJwtException(response, "Malformed token signature");

		} catch (JwtException e) {
			log.error("Invalid Exception.", e);
			handleJwtException(response, "Invalid token");

		} catch (InvalidTokenException e) {
			log.error("Custom Invalid Token Exception", e);
			handleJwtException(response, e.getMessage());

		} catch (ServletException e) {
			log.error("Custom Servlet Exception ", e);
			handleJwtException(response, e.getMessage());

		} catch (Exception e) {
			log.error("Glbal Exception ", e);
			handleJwtException(response, "Unknown error");
		}
	}

	private HttpServletResponse handleJwtException(HttpServletResponse response, String errorMsg)
			throws JsonProcessingException, IOException {
		ErrorResponse errorResponse = new ErrorResponse(StatusCode.INVALID_TOKEN.getCode(), Arrays.asList(errorMsg));
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.getWriter().write(convertObjectToJson(errorResponse));
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		return response;
	}

	private String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
}
