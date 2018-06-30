/**
  UserServiceImpl.java
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
import org.app.wms.ums.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepo userRepo;

	@Override
	public User findByUsername(String username) throws UsernameNotFoundException {
		log.debug("Fetching user by username : {}",username);
		return userRepo.findByUsername(username);
	}

	@Override
	public User findById(Long id) {
		log.debug("Fetching user by id : {}",id);
		return userRepo.findOne(id);
	}

	@Override
	public List<User> findAll() {
		log.debug("Fetching all user : {}");
		return userRepo.findAll();
	}
	
	@Override
	public User save(User user) {
		log.debug("Save user : {}", user.toString());
		return userRepo.save(user);
	}
}
