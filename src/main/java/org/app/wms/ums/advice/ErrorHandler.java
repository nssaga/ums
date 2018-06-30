/**
  ErrorHandler.java
 ***********************************************************************************************************************
 Description: 	Global exception handler for UMS.

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 29-Jun-2018		Nawal Sah				Initial Version

 Copyright (c) 2018,
 ***********************************************************************************************************************
 */
package org.app.wms.ums.advice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolationException;

import org.app.wms.commonutils.constants.StatusCode;
import org.app.wms.commonutils.exception.BadRequestException;
import org.app.wms.commonutils.exception.ErrorResponse;
import org.app.wms.commonutils.exception.InvalidTokenException;
import org.app.wms.commonutils.exception.ProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class ErrorHandler {

	private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

	/**
	 * Handle MissingServletRequestParameterException. Triggered when a 'required'
	 * request parameter is missing.
	 *
	 * @param exception
	 *            MissingServletRequestParameterException
	 * @return the RestExceptionHandler object
	 */

	@ExceptionHandler(value = { MissingServletRequestParameterException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorResponse handleMissingServletRequestParameter(MissingServletRequestParameterException exception) {
		log.error("handleMissingServletRequestParameter : ", exception);
		return new ErrorResponse(HttpStatus.BAD_REQUEST.name(),
				Arrays.asList(exception.getParameterName() + " parameter is missing"));
	}

	/**
	 * This method handles method argument Exceptions.
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorResponse handleMethodArgument(MethodArgumentNotValidException exception) {
		log.error("Method argument not valid exception", exception);
		List<String> errorMessages = new ArrayList<>();
		List<ObjectError> objectErrors = exception.getBindingResult().getAllErrors();
		for (ObjectError objectError : objectErrors) {
			errorMessages.add(objectError.getDefaultMessage());
		}
		return new ErrorResponse(HttpStatus.BAD_REQUEST.name(), errorMessages);
	}

	/**
	 * This method handles method not allowed Exception.
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
	@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
	public ErrorResponse handleRequestMethod(HttpRequestMethodNotSupportedException exception) {
		log.error("Http request method not supported exception: ", exception);
		return new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.name(), Arrays.asList(exception.getMessage()));
	}

	/**
	 * This method handles media type not supported Exception.
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = { HttpMediaTypeNotSupportedException.class })
	@ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public ErrorResponse handleMediaType(HttpMediaTypeNotSupportedException exception) {
		log.error("Http media type not supported exception: ", exception);
		return new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.name(), Arrays.asList(exception.getMessage()));
	}

	/**
	 * This method handles messaging Exceptions.
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = { HttpMessageNotReadableException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorResponse handleMessaging(HttpMessageNotReadableException exception) {
		log.error("Http message not readable exception", exception);
		return new ErrorResponse(HttpStatus.BAD_REQUEST.name(), Arrays.asList(exception.getMessage()));
	}

	/**
	 * This method handles path variable and header validation exception.
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = { ConstraintViolationException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorResponse handleConstraintViolation(ConstraintViolationException exception) {
		log.error("Constraint violation exception", exception);
		return new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new ArrayList<>(exception.getConstraintViolations()));
	}

	/**
	 * Handler for badRequestException
	 * 
	 * @param badRequestException
	 * @return
	 */
	@ExceptionHandler(value = { BadRequestException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorResponse handleBadRequest(BadRequestException badRequestException) {
		log.error("Handling BadRequestException: ", badRequestException);
		return new ErrorResponse(badRequestException.getCode(), Arrays.asList(badRequestException.getMessage()));
	}

	/**
	 * Exception handler for Jwt Exception
	 * 
	 * @param badRequestException
	 * @return
	 */
	@ExceptionHandler(value = { JwtException.class })
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public ErrorResponse handleJwtException(JwtException badRequestException) {
		log.error("Handling BadRequestException: ", badRequestException);
		return new ErrorResponse(StatusCode.INVALID_TOKEN.getCode(), Arrays.asList());
	}

	/**
	 * Exception handler for processing exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = { ProcessingException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleProcessingException(ProcessingException exception) {
		log.error("Processing exception", exception);
		return new ErrorResponse(exception.getCode(), Arrays.asList(exception.getMessage()));
	}

	/**
	 * Exception handler for bad credential
	 * 
	 * @param badCredentialsException
	 * @return
	 */
	@ExceptionHandler(value = { BadCredentialsException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorResponse handleBadCredential(BadCredentialsException badCredentialsException) {
		log.error("Handling BadRequestException: ", badCredentialsException);
		return new ErrorResponse(HttpStatus.BAD_REQUEST.name(), Arrays.asList(badCredentialsException.getMessage()));
	}

	/**
	 * Exception handler for account disable exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = { DisabledException.class })
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public ErrorResponse handleDisabledException(DisabledException exception) {
		log.error("User disabled exception", exception);
		return new ErrorResponse(HttpStatus.FORBIDDEN.name(), Arrays.asList(exception.getMessage()));
	}

	/**
	 * Exception handler for access denied.
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = { AccessDeniedException.class })
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public ErrorResponse handleAccessDeniedException(AccessDeniedException exception) {
		log.error("Access denied exception", exception);
		return new ErrorResponse(HttpStatus.UNAUTHORIZED.name(), Arrays.asList(exception.getMessage()));
	}

	/**
	 * Exception handler for Invalid Token Exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = { InvalidTokenException.class })
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public ErrorResponse handleInvalidTokenException(InvalidTokenException exception) {
		log.error("Invalid Token Exception", exception);
		return new ErrorResponse(HttpStatus.UNAUTHORIZED.name(), Arrays.asList(exception.getMessage()));
	}

	/**
	 * Exception handler for Servlet Exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = { ServletException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorResponse handleServletException(ServletException exception) {
		log.error("Servlet Exception", exception);
		return new ErrorResponse(HttpStatus.BAD_REQUEST.name(), Arrays.asList(exception.getMessage()));
	}

	@ExceptionHandler(value = { UsernameNotFoundException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorResponse handleUsernameNotFoundException(UsernameNotFoundException exception) {
		log.error("Servlet Exception", exception);
		return new ErrorResponse(StatusCode.USER_NOT_FOUND.getCode(),
				Arrays.asList(StatusCode.USER_NOT_FOUND.getMessage()));
	}

	/**
	 * This method handles other general exceptions.
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = { Exception.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleGeneralException(Exception exception) {
		log.error("Unknown Exception ", exception);
		return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), Arrays.asList(exception.getMessage()));
	}

}
