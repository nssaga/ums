/**
  MailHelper.java
 ***********************************************************************************************************************
 Description: 	This class is used to send a mail.

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 29-Jun-2018		Nawal Sah				Initial Version

 Copyright (c) 2018,
 ***********************************************************************************************************************
 */
package org.app.wms.ums.helper;

import org.app.wms.ums.model.EmailMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class MailHelper {

	private static final Logger log = LoggerFactory.getLogger(MailHelper.class);

	@Value("${email.service.url}")
	private String emailServiceURL;

	@Autowired
	private RestTemplate restTemplate;

	public boolean sendMail(String receiver, String subject, String message) {
		EmailMetaData mailRequest = new EmailMetaData(receiver, subject, message);
		log.debug("Sending mail. payload : {}", mailRequest.toString());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<EmailMetaData> requestEntity = new HttpEntity<EmailMetaData>(mailRequest, headers);

		ResponseEntity<String> resp = null;
		try {
			resp = restTemplate.exchange(emailServiceURL, HttpMethod.POST, requestEntity, String.class);

			if (resp.getStatusCodeValue() == 200 && resp.getBody() != null) {
				log.debug("Mail sent successfully to {}", receiver);
				return true;
			} else {
				log.error("Not able to send email to = {}", receiver);
				return false;
			}
		} catch (RestClientException e) {
			log.error("Not able to send email to = {}", e);
			return false;
		}
	}
}
