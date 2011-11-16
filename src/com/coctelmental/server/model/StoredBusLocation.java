package com.coctelmental.server.model;


public class StoredBusLocation {

	private int busLocationID;
	private int latitude;
	private int longitude;
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

	public long getWhenStored() {
		return whenStored;
	}

	public void setWhenStored(long whenStored) {
		this.whenStored = whenStored;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}
	
}
