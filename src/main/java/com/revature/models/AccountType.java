package com.revature.models;

public class AccountType {
	private int typeId; // Primary Key
	private String type; // not null, unique
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
