package com.coctelmental.server.model;

import com.coctelmental.server.utils.JsonHandler;

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

	public String toString() {
		return String.format("userID: %s\n" +
							 "latitude: %s\n" +
							 "longitude: %s", userID, latitude, longitude);
	}

	public String toJson() {
		return JsonHandler.toJson(this);	
	}
	
}
