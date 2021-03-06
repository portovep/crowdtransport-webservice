package com.coctelmental.server.resources;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;


import com.coctelmental.server.helpers.UserHelper;
import com.coctelmental.server.model.User;
import com.coctelmental.server.utils.JsonHandler;

public class UserResource extends ServerResource {
	
	private String targetID;
	private String targetPasswd;
	private UserHelper userHelper;
	
	@Override
	public void doInit() {
		userHelper = new UserHelper();
		targetID = (String)getRequestAttributes().get("userID");
		if (targetID != null) {
			try{
				targetID = URLDecoder.decode(targetID, "UTF-8");
			}catch(UnsupportedEncodingException uce){
				uce.printStackTrace();
				targetID = null;
			}
		}
		
		targetPasswd = (String)getRequestAttributes().get("userPasswd");
		if (targetPasswd!= null) {
			try{
				targetPasswd = URLDecoder.decode(targetPasswd, "UTF-8");
			}catch(UnsupportedEncodingException uce){
				uce.printStackTrace();
				targetPasswd = null;
			}
		}		
	}
	
	@Get("json")
	public Representation getUserJSON(){	
		JsonRepresentation result = null;
		User user = userHelper.getUser(this.targetID);
		if (user == null || targetPasswd == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		else {
			// check password
			if (targetPasswd.equals(user.getPassword()))
				result = new JsonRepresentation(JsonHandler.toJson(user));
			else
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		return result;
	}
	
	@Put("json")
	public Representation putUserJSON(Representation representation){
		try{
			JsonRepresentation userRepresentation = new JsonRepresentation(representation);
			User user = JsonHandler.fromJson(userRepresentation.getText(), User.class);
			// trying to add new user
			int result = userHelper.addUser(user);
			// checking errors
			if (result == UserHelper.EC_INVALID_USERNAME)
				// an user with that id exists
				getResponse().setStatus(Status.CLIENT_ERROR_CONFLICT);	
			else if (result == UserHelper.EC_BBDD_ERROR)
				// error in DAO
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}catch(Exception ioe){
			// an error occurred
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		return null;
	}
	
	
}
