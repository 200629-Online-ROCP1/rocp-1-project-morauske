package com.revature.repos;

import java.util.List;

import com.revature.models.User;
import com.revature.models.UserDTO;

public interface IUserDAO {

	public List<User> findAll();
	public User findById(int id);
	public User addUser(UserDTO u);
}
