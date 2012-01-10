package com.coctelmental.server.model;


public class BusLocation {

	private String busLocationID;
	private int latitude;
	private int longitude;
	private long when;
	private int nCollaborators;
	
	public BusLocation (String id) {
		this.busLocationID = id;
	}

	public String getBusLocationID() {
		return busLocationID;
	}

	public void setBusLocationID(String busLocationID) {
		this.busLocationID = busLocationID;
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
	
	public long getWhen() {
		return when;
	}

	public void setWhen(long when) {
		this.when = when;
	}
	
	public int getnCollaborators() {
		return nCollaborators;
	}

	public void setnCollaborators(int nCollaborators) {
		this.nCollaborators = nCollaborators;
	}
	
}
