/**
  WebMvcConfig.java
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

package org.app.wms.ums.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	/**
	 * This will disable trailing slashes, helpful for interceptor path matching
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseTrailingSlashMatch(false);
	}

	/**
	 * This method handle .(dot) content in url
	 */
	@Override
	public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
		// Turn off suffix-based content negotiation
		configurer.favorPathExtension(false);
	}
}
