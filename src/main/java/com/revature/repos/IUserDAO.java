package com.revature.repos;

import java.util.List;

import com.revature.models.User;

public interface IUserDAO {

	public List<User> findAll();
	public User findById(int id);
//	public boolean addAvenger(User u);
//	public boolean updateHome(User u);
}
