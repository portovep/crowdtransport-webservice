package com.coctelmental.server.resources;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.List;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.coctelmental.server.helpers.LocationHelper;
import com.coctelmental.server.model.BusLocation;
import com.coctelmental.server.model.CollaboratorBusLocation;
import com.coctelmental.server.utils.JsonHandler;
import com.google.gson.reflect.TypeToken;

public class LocationResource extends ServerResource{

	private String targetID;
	private LocationHelper locationHelper;
	
	public void doInit() {
		locationHelper = new LocationHelper();
		
		targetID = (String)getRequestAttributes().get("locationID");
		if (targetID!= null) {
			try{
				targetID = URLDecoder.decode(targetID, "UTF-8");
			}catch(UnsupportedEncodingException uce){
				uce.printStackTrace();
				targetID = "";
			}
		}
	}

	@Get("json")
	public Representation getLocationJSON(){	
		JsonRepresentation result = null;	 
		List<BusLocation> foundLocations = locationHelper.getBusLocations(targetID);
		if (foundLocations == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		else {
			Type listType = new TypeToken<List<BusLocation>>() {}.getType();
			result = new JsonRepresentation(JsonHandler.toJson(foundLocations, listType));
		}
		return result; 
	}
	
	@Put("json")
	public Representation putLocationJSON(Representation representation){
		try{
			JsonRepresentation jsonRepresentation = new JsonRepresentation(representation);
			CollaboratorBusLocation receivedBusLocation = JsonHandler.fromJson(jsonRepresentation.getText(), CollaboratorBusLocation.class);
			if (receivedBusLocation == null || targetID.equals(""))
				getResponse().setStatus(Status.CLIENT_ERROR_CONFLICT);	
			else 
				locationHelper.addBusLocation(targetID, receivedBusLocation);
		}catch(Exception ioe){
			// an error occurred
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		return null;
	}	
}
