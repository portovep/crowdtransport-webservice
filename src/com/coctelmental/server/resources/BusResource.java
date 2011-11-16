package com.coctelmental.server.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.coctelmental.server.helpers.BusHelper;
import com.coctelmental.server.model.BusDriver;
import com.coctelmental.server.utils.JsonHandler;

public class BusResource extends ServerResource {
	
	private String targetDNI;
	private BusHelper busHelper;
	
	@Override
	public void doInit() {
		busHelper = new BusHelper();
		targetDNI = (String)getRequestAttributes().get("busDNI");
		if (targetDNI != null) {
			try{
				targetDNI = URLDecoder.decode(targetDNI, "UTF-8");
			}catch(UnsupportedEncodingException uce){
				uce.printStackTrace();
				targetDNI = null;
			}
		}
	}
	
	@Get("json")
	public Representation getUserJSON(){	
		JsonRepresentation result = null;
		BusDriver busDriver = busHelper.getBusDriver(this.targetDNI);
		if (busDriver == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		else {
			result = new JsonRepresentation(busDriver.toJson());
		}
		return result;
	}
	
	@Get("htm")
	public Representation getUserHtml(){
		StringRepresentation result = null;
		BusDriver busDriver = busHelper.getBusDriver(this.targetDNI);		
		if (busDriver == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		else {
			result = new StringRepresentation(busDriver.toString());
		}
		return result;
	}
	
	@Put("json")
	public Representation putUserJSON(Representation representation){
		try{
			JsonRepresentation userRepresentation = new JsonRepresentation(representation);
			BusDriver busDriver = JsonHandler.fromJson(userRepresentation.getText(), BusDriver.class);
			// trying to add new user
			int result = busHelper.addBusDriver(busDriver);			
			// checking errors
			if (result == BusHelper.EC_INVALID_DNI)
				// an user with that id exists
				getResponse().setStatus(Status.CLIENT_ERROR_CONFLICT);	
			else if (result == BusHelper.EC_INVALID_AUTH_CODE)
				// invalid company registry authorization code
				getResponse().setStatus(Status.CLIENT_ERROR_PRECONDITION_FAILED);
			else if (result == BusHelper.EC_BBDD_ERROR)
				// error in DAO
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);			
		}catch(Exception ioe){
			// an error occurred
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		return null;
	}
}
