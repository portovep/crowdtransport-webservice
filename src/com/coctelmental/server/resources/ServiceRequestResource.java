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

import com.coctelmental.server.helpers.ServiceRequestHelper;
import com.coctelmental.server.model.ServiceRequestInfo;
import com.coctelmental.server.utils.JsonHandler;
import com.google.gson.reflect.TypeToken;

public class ServiceRequestResource extends ServerResource{

	private String taxiUUID;
	private String requestID;
	
	public void doInit() {
		
		taxiUUID = (String)getRequestAttributes().get("taxiUUID");
		if (taxiUUID != null) {
			try{
				taxiUUID = URLDecoder.decode(taxiUUID, "UTF-8");
			}catch(UnsupportedEncodingException uce){
				uce.printStackTrace();
				taxiUUID = null;
			}
			
		}
		
		requestID = (String)getRequestAttributes().get("requestID");
		if (requestID != null) {
			try{
				requestID = URLDecoder.decode(requestID, "UTF-8");
			}catch(UnsupportedEncodingException uce){
				uce.printStackTrace();
				requestID = null;
			}
		}
	}
	
	@Get("json")
	public Representation getServiceRequest(){	
		JsonRepresentation result = null;
		if (taxiUUID != null) {
			if (requestID != null) {
				// get target service request
				ServiceRequestInfo serviceRequest = ServiceRequestHelper.getServiceRequest(taxiUUID, requestID);
				if (serviceRequest == null) {
					getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
				}
				else {
					result = new JsonRepresentation(JsonHandler.toJson(serviceRequest));
				}
			}
			else {
				// get all service request for target taxi driver
				List<ServiceRequestInfo> serviceRequests = ServiceRequestHelper.getAllServiceRequest(taxiUUID);
				if (serviceRequests == null) {
					getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
				}
				else {
					Type listType = new TypeToken<List<ServiceRequestInfo>>() {}.getType();
					result = new JsonRepresentation(JsonHandler.toJson(serviceRequests, listType));
				}
			}				
		}
		else
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);

		return result; 
	}
	
	@Put("json")
	public Representation putServiceRequestJSON(Representation representation){
		try{
			JsonRepresentation jsonRepresentation = new JsonRepresentation(representation);
			ServiceRequestInfo serviceRequest = JsonHandler.fromJson(jsonRepresentation.getText(), ServiceRequestInfo.class);
			if (serviceRequest == null)
				getResponse().setStatus(Status.CLIENT_ERROR_CONFLICT);	
			else 
				ServiceRequestHelper.addServiceRequest(serviceRequest);
		}catch(Exception ioe){
			// an error occurred
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		return null;
	}
	
}
