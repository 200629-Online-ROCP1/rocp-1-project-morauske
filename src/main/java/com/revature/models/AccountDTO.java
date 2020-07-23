package com.revature.models;

public class AccountDTO {

	/*
	 * DTO stands for Data Transfer Object. These are useful when the data coming in from
	 * your front end needs to be placed in a temporary object, or your it does not perfectly conform 
	 * to the model in your server and needs to be converted.
	 */
	
	public int accountId;
	public int ownerId;	// Not Null, unique
	public double balance;	// Not Null could be encrypted
	public String status;	// Not Null
	public String acctType;	// Not Null

}
