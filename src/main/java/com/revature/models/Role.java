package com.revature.models;

public class Role {
	
	private int roleID;
	private String role; // "Admin", "Employee","Standard" or "Premium"

	public Role() {
		// TODO Auto-generated constructor stub
		super();
	}

	public Role(int roleID, String role) {
		super();
		this.roleID = roleID;
		this.role = role;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public void setIdAndRole(int roleID, String role) {
		this.roleID = roleID;
		this.role = role;
	}

}
