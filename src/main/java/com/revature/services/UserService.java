package com.revature.services;

import java.util.List;

import com.revature.models.User;
import com.revature.models.UserDTO;
import com.revature.repos.IUserDAO;
import com.revature.repos.UserDAO;

public class UserService {
	
	private final IUserDAO dao = new UserDAO();
	
	public List<User> findAll(){
		return dao.findAll();
	}
	
	public User findById(int id) {
		return dao.findById(id);
	}

	public Boolean register (UserDTO u) {
		System.out.println("LoginDTO: "+ u.username);
		User usr = dao.addUser(u);
		
		if (usr != null) {
			// Successful login
			System.out.println("LoginDTO returning true"+usr.getFirstName());
			return true;
		}else {
			// Login failure
			System.out.println("LoginDTO returning false");
			return false;
		}
	}

}
