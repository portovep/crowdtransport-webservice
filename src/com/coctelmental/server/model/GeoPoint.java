package com.coctelmental.server.model;

public class GeoPoint {
	
	private int latitudeE6;
	private int longitudeE6;
	
	public GeoPoint(int latitudeE6, int longitudeE6) {
		this.latitudeE6 = latitudeE6;
		this.longitudeE6 = longitudeE6;
	}

	public int getLatitudeE6() {
		return latitudeE6;
	}

	public int getLongitudeE6() {
		return longitudeE6;
	}
		
}
