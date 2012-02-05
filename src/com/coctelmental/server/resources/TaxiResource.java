package com.coctelmental.server.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.coctelmental.server.helpers.TaxiHelper;
import com.coctelmental.server.model.TaxiDriver;
import com.coctelmental.server.utils.JsonHandler;

public class TaxiResource extends ServerResource {
	
	private String targetDNI;
	private String targetPasswd;
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
		
		targetPasswd = (String)getRequestAttributes().get("taxiPasswd");
		if (targetPasswd != null) {
			try{
				targetPasswd = URLDecoder.decode(targetPasswd, "UTF-8");
			}catch(UnsupportedEncodingException uce){
				uce.printStackTrace();
				targetPasswd = null;
			}
		}
	}
	
	@Get("json")
	public Representation getTaxiDriverJSON(){	
		JsonRepresentation result = null;
		TaxiDriver taxiDriver = taxiHelper.getTaxiDriver(this.targetDNI);
		if (taxiDriver == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		else {
			// check password
			if (targetPasswd != null) {
				if(targetPasswd.equals(taxiDriver.getPassword())) {
					result = new JsonRepresentation(JsonHandler.toJson(taxiDriver));
				}
				else {
					getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
				}
			}
			else {
				// request for taxi driver name and car info
				// remove personal data
				taxiDriver.setDni("");
				taxiDriver.setPassword("");
				taxiDriver.setEmail("");
				taxiDriver.setLicenceNumber("");
				result = new JsonRepresentation(JsonHandler.toJson(taxiDriver));
			}
		}
		return result;
	}
	
	@Put("json")
	public Representation putTaxiDriverJSON(Representation representation){
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
