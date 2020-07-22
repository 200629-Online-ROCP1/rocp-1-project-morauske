package com.revature.models;

public class Account {
	private int accountId; //primary Key
	private int ownerUserId; // Foreign Key to account owner
	private double balance; // Not Null
	private AccountStatus status; // 
	private AccountType type; //
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public AccountStatus getStatus() {
		return status;
	}
	public void setStatus(AccountStatus status) {
		this.status = status;
	}
	public AccountType getType() {
		return type;
	}
	public void setType(AccountType type) {
		this.type = type;
	}
	public int getOwnerUserId() {
		return ownerUserId;
	}
	public void setOwnerUserId(int ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

}
