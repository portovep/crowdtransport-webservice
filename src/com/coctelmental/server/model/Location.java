package com.coctelmental.server.model;

public class Location {

	private MyGeoPoint geopoint;
	private long when;
	
	public MyGeoPoint getGeopoint() {
		return geopoint;
	}

	public void setGeopoint(MyGeoPoint geopoint) {
		this.geopoint = geopoint;
	}

	public long getWhen() {
		return when;
	}

	public void setWhen(long when) {
		this.when = when;
	}

}
