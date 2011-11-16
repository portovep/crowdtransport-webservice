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

import com.coctelmental.server.helpers.TaxiHelper;
import com.coctelmental.server.model.TaxiDriver;
import com.coctelmental.server.utils.JsonHandler;

public class TaxiResource extends ServerResource {
	
	private String targetDNI;
	private TaxiHelper taxiHelper;
	
	@Override
	public void doInit() {
		taxiHelper = new TaxiHelper();
		targetDNI = (String)getRequestAttributes().get("taxiDNI");
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
		TaxiDriver taxiDriver = taxiHelper.getTaxiDriver(this.targetDNI);
		if (taxiDriver == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		else {
			result = new JsonRepresentation(taxiDriver.toJson());
		}
		return result;
	}
	
	@Get("htm")
	public Representation getUserHtml(){
		StringRepresentation result = null;
		TaxiDriver taxiDriver = taxiHelper.getTaxiDriver(this.targetDNI);		
		if (taxiDriver == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		else {
			result = new StringRepresentation(taxiDriver.toString());
		}
		return result;
	}
	
	@Put("json")
	public Representation putUserJSON(Representation representation){
		try{
			JsonRepresentation userRepresentation = new JsonRepresentation(representation);
			TaxiDriver taxiDriver = JsonHandler.fromJson(userRepresentation.getText(), TaxiDriver.class);
			// trying to add new user
			int result = taxiHelper.addTaxiDriver(taxiDriver);			
			// checking errors
			if (result == TaxiHelper.EC_INVALID_DNI)
				// an user with that id exists
				getResponse().setStatus(Status.CLIENT_ERROR_CONFLICT);	
			else if (result == TaxiHelper.EC_INVALID_LICENCE)
				// invalid licence
				getResponse().setStatus(Status.CLIENT_ERROR_PRECONDITION_FAILED);
			else if (result == TaxiHelper.EC_BBDD_ERROR)
				// error in DAO
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);			
		}catch(Exception ioe){
			// an error occurred
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		return null;
	}
}
