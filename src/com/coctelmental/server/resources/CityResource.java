package com.coctelmental.server.resources;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.coctelmental.server.helpers.CityHelper;
import com.coctelmental.server.utils.JsonHandler;


public class CityResource extends ServerResource {
	
	private CityHelper cityHelper;
	
	@Override
	public void doInit() {
		cityHelper = new CityHelper();
	}

	@Get("json")
	public Representation getCitiesJSON(){
		JsonRepresentation result = null;
		String[] cities = cityHelper.getCities();
		if (cities.length == 0) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		else {
			result = new JsonRepresentation(JsonHandler.toJson(cities));
		}
		return result;
	}
}
