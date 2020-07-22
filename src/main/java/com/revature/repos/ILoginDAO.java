package com.revature.repos;

import com.revature.models.LoginDTO;
import com.revature.models.User;

public interface ILoginDAO {
	
	public User findUserByUsername(LoginDTO user);

}
