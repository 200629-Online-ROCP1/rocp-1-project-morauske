package com.revature.models;

public class AccountStatus {
	private int statusId; // Primary Key
	private String status; // Not Null, Unique {"Pending,"Open","Closed","Denied"}
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
