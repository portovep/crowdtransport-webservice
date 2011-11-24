package com.coctelmental.server.model;

public class TaxiLocation {

	private String taxiDriverID;
	private Location location;
	
	public String getTaxiDriverID() {
		return taxiDriverID;
	}
	public void setTaxiDriverID(String taxiDriverID) {
		this.taxiDriverID = taxiDriverID;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
}
