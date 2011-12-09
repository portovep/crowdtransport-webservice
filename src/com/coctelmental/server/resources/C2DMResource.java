package com.coctelmental.server.resources;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.coctelmental.server.model.DeviceInfo;
import com.coctelmental.server.serviceRequests.DeviceInfoStore;
import com.coctelmental.server.utils.JsonHandler;

public class C2DMResource extends ServerResource{

	@Post("json")
	public Representation addDeviceInfoJSON(Representation representation){
		try{
			JsonRepresentation jsonRepresentation = new JsonRepresentation(representation);
			DeviceInfo deviceInfo = JsonHandler.fromJson(jsonRepresentation.getText(), DeviceInfo.class);
			if (deviceInfo == null || deviceInfo.getRegistrationID() == null)
				getResponse().setStatus(Status.CLIENT_ERROR_CONFLICT);	
			else 
				// add device info
				System.out.println("Device UUID->"+deviceInfo.getUUID()+" REG-ID->"+deviceInfo.getRegistrationID());
				DeviceInfoStore.getInstance().addDeviceInfo(deviceInfo);
		}catch(Exception ioe){
			// an error occurred
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		return null;
	}	
	
}
