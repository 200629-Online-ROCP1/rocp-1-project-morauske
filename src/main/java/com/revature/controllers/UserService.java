package com.revature.controllers;

import java.util.List;

import com.revature.models.User;
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
//
//	public boolean addUser(User a) {
//		List<User> list = findAll();
//		
//		for(User av: list) {
////			System.out.println(av);
////			System.out.println(a);
//			// The following checks to see if the avenger 'a' is currently already in the database.  We don't want duplicates
//			// so if their firstName's, lastName's AND superName's match then they are equal.
//			// TODO Seems like we could have this us Comparable or Comparator?
//			if(av.getfName().equals(a.getfName()) && av.getlName().equals(a.getlName()) && av.getSupName().equals(a.getSupName()) ) {
//				return false;
//			}
//		}
//	
//		boolean b = dao.addUser(a);
//		System.out.println("boolean in AS = " +b);
//		return b;
//		}
//	

}
