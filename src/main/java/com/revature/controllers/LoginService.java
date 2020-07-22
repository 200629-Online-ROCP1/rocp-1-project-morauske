package com.revature.controllers;

import com.revature.models.LoginDTO;
import com.revature.models.User;
import com.revature.repos.ILoginDAO;
import com.revature.repos.LoginDAO;

public class LoginService {
	// The LoginService needs to go out to the database and check to see if the there is a user defined
	private final ILoginDAO dao = new LoginDAO();

	public User login(LoginDTO l) {
		System.out.println("LoginDTO: "+ l.username);
		User usr = dao.findUserByUsername(l);
		
		if (usr != null) {
			// Successful login
			System.out.println("LoginDTO returning true"+usr.getFirstName());
			return usr;
		}else {
			// Login failure
			System.out.println("LoginDTO returning false");
			return null;
		}
	}
}
