package com.coctelmental.server.resources;

public interface ResourceTemplates {

	public static final String USER_RESOURCE = "/user";
	public static final String USER_RESOURCE_WITH_ID = "/user/{userID}";
	public static final String TAXI_RESOURCE = "/taxi";
	public static final String TAXI_RESOURCE_WITH_ID = "/taxi/{taxiDNI}";
	public static final String BUS_RESOURCE = "/bus";
	public static final String BUS_RESOURCE_WITH_ID = "/bus/{busDNI}";
	public static final String CITY_RESOURCE = "/city";
	public static final String LINE_RESOURCE_WITH_CITYID = "/city/{cityID}/line";
	public static final String LOCATION_RESOURCE = "/location";
	public static final String LOCATION_RESOURCE_WITH_ID = "/location/{locationID}";
	public static final String TAXI_LOCATION_RESOURCE = "/location-taxi";
	public static final String TAXI_LOCATION_RESOURCE_WITH_ID = "/location-taxi/{geopointOrigin}";
	
}
