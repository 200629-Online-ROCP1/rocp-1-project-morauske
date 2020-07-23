package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.LoginDTO;
import com.revature.models.User;
import com.revature.models.UserDTO;


public class UserController {

	
	private static final UserService us = new UserService();
	private static final ObjectMapper om = new ObjectMapper();
	
	public List<User> findAll() {
		return us.findAll();
	}

	public User findById(int id) {
		return us.findById(id);
	}

	public void register(HttpServletRequest req, HttpServletResponse res) throws IOException {
		BufferedReader reader = req.getReader();
		StringBuilder       s = new StringBuilder();
		String           line = reader.readLine();
		
		while(line != null) {
			s.append(line);
			System.out.println("login req.getReader Info: "+line);
			line=reader.readLine();
		}
		
		String body = new String(s);
		System.out.println("login body: "+body);
		
		UserDTO u = om.readValue(body, UserDTO.class);
		
		if (us.register(u) ) {
			// new user
			res.setStatus(201);
			res.getWriter().println("New User has been created");
		} else {
			// failed to create new user
			res.setStatus(400);
			res.getWriter().println("Invalid fields");
		}
		
		// ls.login() will reach out the the DBase to verify username/password
		//		User usr = ls.login(l);
		
	}

}
