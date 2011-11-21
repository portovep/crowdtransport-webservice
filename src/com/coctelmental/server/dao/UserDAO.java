package com.coctelmental.server.dao;


import com.coctelmental.server.model.User;


public interface UserDAO {

	public User getUserByID(String id);
	
	public int addUser(User user);
	
}
