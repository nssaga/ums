/**
  TokenHelper.java
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
package org.app.wms.ums.helper;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.app.wms.commonutils.constants.StatusCode;
import org.app.wms.commonutils.exception.InvalidTokenException;
import org.app.wms.ums.constant.Constant;
import org.app.wms.ums.constant.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class TokenHelper {

	private static Logger log = LoggerFactory.getLogger(TokenHelper.class);

	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

	@Value("${access.token.expiry}")
	private Long accessTokenExpiry;

	@Value("${refresh.token.expiry}")
	private Long refreshTokenExpiry;;

	@Value("${verification.token.expiry}")
	private Long verificationTokenExpiry;

	@Value("${access.token.api.secret.key}")
	private String accessTokenApiSecretKey;

	@Value("${refresh.token.api.secret.key}")
	private String refreshTokenApiSecretKey;

	@Value("${verification.token.api.secret.key}")
	private String verificationTokenApiSecretKey;

	@Autowired
	HttpServletResponse response;

	public String getValidationToken(String username, String issuerId, String subject, TokenType tokenType) {

		long currentTimestamp = System.currentTimeMillis();
		Date currentDate = new Date(currentTimestamp);

		long expiryTime = 0;

		String apiKeySecretBytes = null;

		if (TokenType.ACCESS == tokenType) {
			apiKeySecretBytes = accessTokenApiSecretKey;
			expiryTime = accessTokenExpiry;
		} else if (TokenType.REFRESH == tokenType) {
			apiKeySecretBytes = refreshTokenApiSecretKey;
			expiryTime = refreshTokenExpiry;
		} else if (TokenType.VERIFICATION == tokenType) {
			apiKeySecretBytes = verificationTokenApiSecretKey;
			expiryTime = verificationTokenExpiry;
		} else {
			log.error("Invalid token type");
		}

		Key signatureKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(apiKeySecretBytes),
				SIGNATURE_ALGORITHM.getJcaName());

		JwtBuilder builder = Jwts.builder().setId(username).setIssuedAt(currentDate).setSubject(subject)
				.setIssuer(issuerId).signWith(SIGNATURE_ALGORITHM, signatureKey);

		if (expiryTime >= 0) {
			long expMillis = currentTimestamp + expiryTime;
			Date expDate = new Date(expMillis);
			builder.setExpiration(expDate);
		}
		return builder.compact();
	}

	public Claims getAllClaimsFromToken(String token, TokenType tokenType) throws Exception {
		//log.debug("Getting claims from token");
		String apiKeySecretBytes = null;
		Claims claims = null;

		if (TokenType.ACCESS == tokenType) {
			apiKeySecretBytes = accessTokenApiSecretKey;
		} else if (TokenType.REFRESH == tokenType) {
			apiKeySecretBytes = refreshTokenApiSecretKey;
		} else if (TokenType.VERIFICATION == tokenType) {
			apiKeySecretBytes = verificationTokenApiSecretKey;
		} else {
			log.error("Invalid token type");
		}

		try {
			claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(apiKeySecretBytes))
					.parseClaimsJws(token).getBody();

		} catch (UnsupportedJwtException e) {
			log.error("Unsupported Jwt Exception", e);
			throw new InvalidTokenException(StatusCode.INVALID_TOKEN.getCode(), "Unsupported Jwt");

		} catch (MalformedJwtException e) {
			log.error("Malformed Jwt Exception", e);
			throw new InvalidTokenException(StatusCode.INVALID_TOKEN.getCode(), "Malformed token");

		} catch (ExpiredJwtException e) {
			log.error("Expired Jwt Exception.", e);
			throw new InvalidTokenException(StatusCode.INVALID_TOKEN.getCode(), "Token expired");

		} catch (SignatureException e) {
			log.error("Malformed Signature Exception.", e);
			throw new InvalidTokenException(StatusCode.INVALID_TOKEN.getCode(), "Malformed signature token");

		} catch (JwtException e) {
			log.error("Unsupported Jwt Exception.", e);
			throw new InvalidTokenException(StatusCode.INVALID_TOKEN.getCode(), "Unsupported token Exception");
		}

		return claims;
	}

	/**
	 * Getting the bearer token from Authentication header
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	public String getToken(HttpServletRequest request) throws ServletException {
		String authHeader = getAuthHeaderFromHeader(request);

		if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith(Constant.TOKEN_PREFIX)) {
			throw new ServletException("Missing or Invalid Authorization Header");
		}
		return authHeader.substring(7);
	}

	/**
	 * Get the Authentication header from request
	 * 
	 * @param request
	 * @return
	 */
	private String getAuthHeaderFromHeader(HttpServletRequest request) {
		return request.getHeader(Constant.AUTH_HEADER);
	}

}
