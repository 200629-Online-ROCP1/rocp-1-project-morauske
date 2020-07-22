package com.revature.controllers;

import java.util.List;

import com.revature.models.User;


public class UserController {

	
	private final UserService us = new UserService();
	
	public List<User> findAll() {
		return us.findAll();
	}

	public User findById(int id) {
		return us.findById(id);
	}

}
