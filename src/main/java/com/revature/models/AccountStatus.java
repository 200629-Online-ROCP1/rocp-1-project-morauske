package com.revature.models;

public class AccountStatus {
	private int statusId; // Primary Key
	private String status; // Not Null, Unique {"Pending,"Open","Closed","Denied"}
	
	public AccountStatus(int statusId, String status) {
		super();
		this.statusId = statusId;
		this.status = status;
	}
	public AccountStatus(String status) {
		super();
		//this.statusId = statusId;
		this.status = status;
		switch (status) {
		case "Pending":
			this.statusId = 1;
			break;
		case "Open":
			this.statusId = 2;
			break;
		case "Closed":
			this.statusId = 3;
			break;
		case "Denied":
			this.statusId = 4;
			break;
		}
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
