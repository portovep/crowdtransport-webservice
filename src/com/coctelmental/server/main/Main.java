package com.coctelmental.server.main;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

import com.coctelmental.server.resources.BusResource;
import com.coctelmental.server.resources.C2DMResource;
import com.coctelmental.server.resources.CityResource;
import com.coctelmental.server.resources.ServiceRequestResponseResource;
import com.coctelmental.server.resources.LineResource;
import com.coctelmental.server.resources.LocationResource;
import com.coctelmental.server.resources.ResourceTemplates;
import com.coctelmental.server.resources.ServiceRequestResource;
import com.coctelmental.server.resources.TaxiLocationResource;
import com.coctelmental.server.resources.TaxiResource;
import com.coctelmental.server.resources.UserResource;


public class Main extends Application {
	
	public static final String CONTEXT = "/webservice";
	private static final int DEFAULT_PORT = 8085;
	
	public static void startServer(int port) throws Exception {
		// Create a Restlet component and add a HTTP server
		Component component = new Component();
		component.getServers().add(Protocol.HTTP, port);
		// Create an instance of this application
		Application app = new Main();
		// Attach context and app instance to our component (server)
		component.getDefaultHost().attach(CONTEXT, app);
		component.start();
	}
	

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Integer port = DEFAULT_PORT;
		// Retrieve port number passed as an argument
		if(args.length > 0)
			port = Integer.getInteger(args[1]);
		startServer(port);
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach(ResourceTemplates.USER_RESOURCE, UserResource.class);
		router.attach(ResourceTemplates.USER_RESOURCE_WITH_ID, UserResource.class);
		router.attach(ResourceTemplates.TAXI_RESOURCE, TaxiResource.class);
		router.attach(ResourceTemplates.TAXI_RESOURCE_WITH_ID, TaxiResource.class);		
		router.attach(ResourceTemplates.BUS_RESOURCE, BusResource.class);
		router.attach(ResourceTemplates.BUS_RESOURCE_WITH_ID, BusResource.class);		
		router.attach(ResourceTemplates.CITY_RESOURCE, CityResource.class);
		router.attach(ResourceTemplates.CITY_RESOURCE, CityResource.class);
		router.attach(ResourceTemplates.LINE_RESOURCE_WITH_CITYID, LineResource.class);
		router.attach(ResourceTemplates.LOCATION_RESOURCE, LocationResource.class);
		router.attach(ResourceTemplates.LOCATION_RESOURCE_WITH_ID, LocationResource.class);
		router.attach(ResourceTemplates.TAXI_LOCATION_RESOURCE, TaxiLocationResource.class);
		router.attach(ResourceTemplates.TAXI_LOCATION_RESOURCE_WITH_ID, TaxiLocationResource.class);
		router.attach(ResourceTemplates.C2DM_REGISTRATION_RESOURCE, C2DMResource.class);
		router.attach(ResourceTemplates.SERVICE_REQUEST_RESOURCE, ServiceRequestResource.class);
		router.attach(ResourceTemplates.SERVICE_REQUEST_RESOURCE_WITH_ID, ServiceRequestResource.class);
		router.attach(ResourceTemplates.SERVICE_REQUEST_RESOURCE_WITH_REQUESTID, ServiceRequestResource.class);
		router.attach(ResourceTemplates.SERVICE_REQUEST_RESPONSE_RESOURCE, ServiceRequestResponseResource.class);
		return router;
	}
}
