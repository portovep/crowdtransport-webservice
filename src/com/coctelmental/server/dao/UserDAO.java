package com.coctelmental.server.dao;


import com.coctelmental.server.model.User;


public interface UserDAO {

	public User getUserByID(String userID);
	
	public int addUser(User user);
	
}
