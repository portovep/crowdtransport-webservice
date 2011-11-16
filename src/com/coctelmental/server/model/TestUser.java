package com.coctelmental.server.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.coctelmental.server.utils.JsonHandler;

public class TestUser {

	@Test
	public void testToString() {
		String userName = "pepe";
		String name = "pepe botella";
		String password = "89789798asddasd87";
		String email = "email@email.email";
		
		User user = new User(userName, name, password, email);		
		String validStringUser = String.format("userName: %s\n" +
				 "name: %s\n" +
				 "email: %s", userName, name, email );
		
		assertEquals("Checking String conversion..",validStringUser, user.toString());
	}
	
	@Test
	public void testToJson() {
		String userName = "pepe";
		String name = "pepe botella";
		String password = "89789798asddasd87";
		String email = "email@email.email";
		
		User user = new User(userName, name, password, email);				
		String validJsonUser = "{\"userName\":\"pepe\",\"name\":\"pepe botella\",\"password\":\"89789798asddasd87\",\"email\":\"email@email.email\"}";
		
		assertEquals("Checking JSON conversion..", validJsonUser, user.toJson());
	}
	
	@Test
	public void testUserFromJson() {
		String userName = "pepe";
		String name = "pepe botella";
		String password = "89789798asddasd87";
		String email = "email@email.email";
		
		User user = new User(userName, name, password, email);
		User user2 = JsonHandler.fromJson(user.toJson(), User.class);
		
		assertEquals("Checking JSON constructor", user.toString(), user2.toString());
	}
}
