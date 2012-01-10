package com.coctelmental.server.model;


public class CollaboratorBusLocation {

	private String userID;
	private int latitude;
	private int longitude;

	public CollaboratorBusLocation (String id) {
		this.userID = id;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getUserID() {
		return userID;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}
	
	public int getLongitude() {
		return longitude;
	}

}
