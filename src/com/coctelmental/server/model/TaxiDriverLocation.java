package com.coctelmental.server.model;

public class TaxiDriverLocation {

	private String taxiDriverID;
	private GeoPointInfo geopoint;

	public String getTaxiDriverID() {
		return taxiDriverID;
	}

	public void setTaxiDriverID(String taxiDriverID) {
		this.taxiDriverID = taxiDriverID;
	}

	public GeoPointInfo getGeopoint() {
		return geopoint;
	}

	public void setGeopoint(GeoPointInfo geopoint) {
		this.geopoint = geopoint;
	}
	
}
