package com.coctelmental.server.resources;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.coctelmental.server.main.Main;
import com.coctelmental.server.model.User;
import com.coctelmental.server.utils.JsonHandler;

public class TestUserResource {
	
	private static final int TEST_PORT = 8182;
	
	@BeforeClass
	public static void startServer() throws Exception {
		Main.startServer(TEST_PORT);
	}

	@Test(expected = ResourceException.class)
	public void testFailGetUser() {
		String targetUrl = String.format("http://localhost:%d"+Main.CONTEXT+"/user/buuuu", TEST_PORT);
		ClientResource client = new ClientResource(targetUrl);
		client.get();
		assertEquals("Checking fail getting user", Status.CLIENT_ERROR_NOT_ACCEPTABLE, client.getResponse().getStatus());
	}

/*	
	@Test
	public void testValidGetUser() throws Exception {
		String userName = "pepe";
		String name = "pepe botella";
		String password = "d2104a400c7f629a197f33bb33fe80c0";
		String email = "email@email.email";
		
		User user = new User(userName, name, password, email);
		Users.getInstance().addContact(user);
		String targetUrl = String.format("http://localhost:%d"+UserManagerServer.CONTEXT+"/user/"+userName, TEST_PORT);
		ClientResource client = new ClientResource(targetUrl);
		client.get(MediaType.APPLICATION_JSON);
		System.out.println(client.getResponse().getEntity().getText());

		assertTrue("Checking get valid user", client.getResponse().getStatus().isSuccess());
	}
	

	@Test(expected = ResourceException.class)
	public void testFailPutUser() {
		String userName = "pepe";
		String name = "pepe botella";
		String password = "89789798asddasd87";
		String email = "email@email.email";
		
		User user = new User(userName, name, password, email);
		Users.getInstance().addContact(user);
		
		String targetUrl = String.format("http://localhost:%d"+UserManagerServer.CONTEXT+ResourceTemplates.USER_RESOURCE, TEST_PORT);
		ClientResource client = new ClientResource(targetUrl);
		JsonRepresentation userRepresentation = new JsonRepresentation(JsonHandler.toJson(user));
		client.put(userRepresentation);
		assertEquals("Checking get invalid user", Status.CLIENT_ERROR_CONFLICT, client.getResponse().getStatus());
	} */
	
	@Test
	public void testValidPutUser() {
		String userName = "aaabba";
		String name = "pepe botella";
		String password = "89789798asddasd87";
		String email = "email@email.email";
		
		User user = new User(userName, name, password, email);
		
		String targetUrl = String.format("http://localhost:%d"+Main.CONTEXT+ResourceTemplates.USER_RESOURCE, TEST_PORT);
		ClientResource client = new ClientResource(targetUrl);
		JsonRepresentation userRepresentation = new JsonRepresentation(JsonHandler.toJson(user));
		client.put(userRepresentation);
		assertTrue("Checking get valid user", client.getResponse().getStatus().isSuccess());
	}	
}
