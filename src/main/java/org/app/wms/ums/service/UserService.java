/**
  UserService.java
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

import java.util.List;

import org.app.wms.ums.entity.User;
import org.springframework.security.access.AccessDeniedException;

public interface UserService {

	/**
	 * Find user by id
	 * 
	 * @param id
	 * @return
	 */
	User findById(Long id);

	/**
	 * Find user by username
	 * 
	 * @param username
	 * @return
	 */
	User findByUsername(String username);

	/**
	 * Find all user
	 * 
	 * @return
	 */
	List<User> findAll();

	/**
	 * Save user
	 * 
	 * @param user
	 * @return
	 * @throws AccessDeniedException
	 */
	User save(User user) throws AccessDeniedException;
}
