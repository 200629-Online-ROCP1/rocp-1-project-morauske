package com.revature.models;

public class UserDTO {
	/*
	 * DTO stands for Data Transfer Object. These are useful when the data coming in from
	 * your front end needs to be placed in a temporary object, or your it does not perfectly conform 
	 * to the model in your server and needs to be converted.
	 */
	
	public int userId;
	public String username;	// Not Null, unique
	public String password;	// Not Null could be encrypted
	public String firstName;	// Not Null
	public String lastName;	// Not Null
	public String email;		// Not Null, could be verified in correct format xxx@yyy.zzz
	public Role role = new Role();			// Foreign Key N:1 relationship.


}
