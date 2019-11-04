package com.topwulian.service;

import com.topwulian.dto.UserDto;
import com.topwulian.model.User;

public interface UserService {

	User saveUser(UserDto userDto);
	
	User updateUser(UserDto userDto);

	String passwordEncoder(String credentials, String salt);

	User getUser(String username);

	void changePassword(String username, String oldPassword, String newPassword);

    void delete(Long userId);

    User associateFarmForUser(UserDto userDto);
}
