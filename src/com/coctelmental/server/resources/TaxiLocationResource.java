package com.coctelmental.server.resources;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.coctelmental.server.helpers.TaxiLocationHelper;
import com.coctelmental.server.model.TaxiDriverLocation;
import com.coctelmental.server.utils.JsonHandler;

public class TaxiLocationResource extends ServerResource {

	private TaxiLocationHelper taxiLocationHelper;
	
	public void doInit() {
		taxiLocationHelper = new TaxiLocationHelper();
	}
	
	@Put("json")
	public Representation putLocationJSON(Representation representation){
		try{
			JsonRepresentation jsonRepresentation = new JsonRepresentation(representation);
			TaxiDriverLocation taxiDriverLocation = JsonHandler.fromJson(jsonRepresentation.getText(), TaxiDriverLocation.class);
			if (taxiDriverLocation == null)
				getResponse().setStatus(Status.CLIENT_ERROR_CONFLICT);	
			else 
				taxiLocationHelper.addTaxiLocation(taxiDriverLocation);
		}catch(Exception ioe){
			// an error occurred
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		return null;
	}	

}
