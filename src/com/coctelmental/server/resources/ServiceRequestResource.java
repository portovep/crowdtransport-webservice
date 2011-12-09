package com.coctelmental.server.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.coctelmental.server.model.ServiceRequestInfo;
import com.coctelmental.server.serviceRequests.ServiceRequestStore;
import com.coctelmental.server.utils.JsonHandler;

public class ServiceRequestResource extends ServerResource{

	private String targetUUID;
	
	public void doInit() {
		
		targetUUID = (String)getRequestAttributes().get("UUID");
		if (targetUUID!= null) {
			try{
				targetUUID = URLDecoder.decode(targetUUID, "UTF-8");
			}catch(UnsupportedEncodingException uce){
				uce.printStackTrace();
				targetUUID = "";
			}
		}
	}
	
	@Put("json")
	public Representation putServiceRequestJSON(Representation representation){
		try{
			JsonRepresentation jsonRepresentation = new JsonRepresentation(representation);
			ServiceRequestInfo serviceRequest = JsonHandler.fromJson(jsonRepresentation.getText(), ServiceRequestInfo.class);
			if (serviceRequest == null)
				getResponse().setStatus(Status.CLIENT_ERROR_CONFLICT);	
			else 
				ServiceRequestStore.getInstance().addServiceRequest(serviceRequest);
		}catch(Exception ioe){
			// an error occurred
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		return null;
	}
	
}
