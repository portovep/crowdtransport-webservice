package com.coctelmental.server.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.coctelmental.server.helpers.ServiceRequestHelper;
import com.coctelmental.server.utils.JsonHandler;

public class ServiceRequestResponseResource extends ServerResource {

	private String requestID;
	
	public void doInit() {

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
	
	@Post("json")
	public Representation manageResponseJSON(Representation representation){
		try{
			JsonRepresentation jsonRepresentation = new JsonRepresentation(representation);
			String[] requestData = JsonHandler.fromJson(jsonRepresentation.getText(), String[].class);
			if (requestData[0] != null && requestData[1] != null) {
				if (requestData[0].equals("accept")) {
					// accept request
					// get taxi driver UUID
					String taxiDriverUUID = requestData[1];
					int result = ServiceRequestHelper.acceptServiceRequest(requestID, taxiDriverUUID);
					if (result == -1)
						// an error occurred
						getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
				}
				else if (requestData[0].equals("reject")) {
					// TO-DO reject request
				}
			}
			else
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		}catch(Exception ioe){
			// an error occurred
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		}
		return null;
	}
	
}
