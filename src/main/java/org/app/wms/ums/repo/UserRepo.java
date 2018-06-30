/**
  UserRepo.java
 ***********************************************************************************************************************
 Description: 	User repo interface to connect with databse

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 29-Jun-2018		Nawal Sah				Initial Version

 Copyright (c) 2018,
 ***********************************************************************************************************************
 */
package org.app.wms.ums.repo;

import org.app.wms.ums.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
