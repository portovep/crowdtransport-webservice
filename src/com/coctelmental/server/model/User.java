package com.coctelmental.server.model;


public class User {
	
	private String userName;
	private String fullName;
	private String password;
	private String email;
	
	public User() {}
	
	public User(String userName, String fullName, String password, String email) {
		this.userName = userName;
		this.fullName = fullName;
		this.password = password;
		this.email = email;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setName(String name) {
		this.fullName = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}	

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserName() {
		return userName;
	}	
	
	public String getFullName() {
		return fullName;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

}
