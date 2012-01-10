package com.coctelmental.server.model;


public class StoredBusLocation {

	private int busLocationID;
	private GeoPointInfo geopoint;
	private long whenStored;
	
	public StoredBusLocation (int id) {
		this.busLocationID = id;
	}

	public Integer getBusLocationID() {
		return busLocationID;
	}

	public void setBuSLocationID(Integer busLocationID) {
		this.busLocationID = busLocationID;
	}

	public GeoPointInfo getGeopoint() {
		return geopoint;
	}

	public void setGeopoint(GeoPointInfo geopoint) {
		this.geopoint = geopoint;
	}
	
	public long getWhenStored() {
		return whenStored;
	}

	public void setWhenStored(long whenStored) {
		this.whenStored = whenStored;
	}
	
}
