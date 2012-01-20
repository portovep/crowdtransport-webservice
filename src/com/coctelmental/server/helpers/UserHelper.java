package com.coctelmental.server.helpers;

import com.coctelmental.server.dao.UserDAO;
import com.coctelmental.server.dao.UserDAOImpl;
import com.coctelmental.server.model.User;
import com.coctelmental.server.utils.MailHandler;

public class UserHelper {
	
	public static final int EC_BBDD_ERROR = -1;
	public static final int EC_INVALID_USERNAME = -2;		
	
	private UserDAO userDAO;
	
	public UserHelper() {
		userDAO = new UserDAOImpl();
	}
	
	public User getUser(String userID) {
		return userDAO.getUserByID(userID);
	}
	
	public int addUser(User user) {
		//set default resultCode to specify correct registration
		int resultCode = 1;
		
		// check if an user with specified userName already exist
		if (this.getUser(user.getUserName()) != null)
			resultCode = EC_INVALID_USERNAME;
		else {
			int result = userDAO.addUser(user);
			// check correct update in bbdd
			if (result < 1) {
				resultCode = EC_BBDD_ERROR;
			}
			else {
				// send confirmation email
				MailHandler.sendRegitrationMail(user.getFullName(), user.getEmail());
			}
		}			
		return resultCode;
	}	
}
