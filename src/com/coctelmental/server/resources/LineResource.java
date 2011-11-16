package com.coctelmental.server.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.coctelmental.server.helpers.LineHelper;
import com.coctelmental.server.utils.JsonHandler;

public class LineResource extends ServerResource {

	private String	targetCity;
	private LineHelper lineHelper;
	
	@Override
	public void doInit() {
		lineHelper = new LineHelper();
		targetCity = (String)getRequestAttributes().get("cityID");
		if (targetCity != null) {
			try{
				targetCity = URLDecoder.decode(targetCity, "UTF-8");
			}catch(UnsupportedEncodingException uce){
				uce.printStackTrace();
				targetCity = "";
			}
		}
	}

	@Get("json")
	public Representation getLinesJSON(){
		JsonRepresentation result = null;
		String[] lines = lineHelper.getLines(targetCity);
		if (lines.length == 0) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
		}
		else {
			result = new JsonRepresentation(JsonHandler.toJson(lines));
		}
		return result;
	}
}
