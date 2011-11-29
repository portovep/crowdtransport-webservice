package com.coctelmental.server.resources;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.List;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.coctelmental.server.helpers.TaxiLocationHelper;
import com.coctelmental.server.model.GeoPointInfo;
import com.coctelmental.server.model.TaxiDriverLocation;
import com.coctelmental.server.model.TaxiLocation;
import com.coctelmental.server.utils.JsonHandler;
import com.google.gson.reflect.TypeToken;

public class TaxiLocationResource extends ServerResource {

	private String jsonGpOrigin;
	private TaxiLocationHelper taxiLocationHelper;
	
	public void doInit() {
		taxiLocationHelper = new TaxiLocationHelper();
		
		jsonGpOrigin = (String)getRequestAttributes().get("geopointOrigin");
		if (jsonGpOrigin!= null) {
			try{
				jsonGpOrigin = URLDecoder.decode(jsonGpOrigin, "UTF-8");
			}catch(UnsupportedEncodingException uce){
				uce.printStackTrace();
				jsonGpOrigin = null;
			}
		}
	}
	
	@Get("json")
	public Representation getLocationJSON(){	
		JsonRepresentation result = null;
		GeoPointInfo gpOrigin = JsonHandler.fromJson(jsonGpOrigin, GeoPointInfo.class);
		List<TaxiLocation> foundLocations = taxiLocationHelper.getTaxiLocations(gpOrigin);
		if (foundLocations == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		else {
			Type listType = new TypeToken<List<TaxiLocation>>() {}.getType();
			result = new JsonRepresentation(JsonHandler.toJson(foundLocations, listType));
		}
		return result;
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
