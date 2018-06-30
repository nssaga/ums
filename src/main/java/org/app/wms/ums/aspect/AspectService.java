/**
  AspectService.java
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
package org.app.wms.ums.aspect;

import javax.servlet.http.HttpServletRequest;

import org.app.wms.commonutils.constants.CommonConstants;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class AspectService {

	private static final Logger log = LoggerFactory.getLogger(AspectService.class);

	@Before("within(org.app.wms.ums.controller.*)")
	public void beforeService() {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();

		MDC.put(CommonConstants.REQUEST_TIME, String.valueOf(System.currentTimeMillis()));

		log.debug("API = {}", request.getRequestURI());
		log.debug("Query Param = {}", request.getQueryString());

	}

	@After("within(org.app.wms.ums.controller.*)")
	public void afterService() {
		MDC.clear();
	}
}
