package com.coctelmental.server.model;

public class LocationInfo {

	private String senderUUID;
	private GeoPointInfo geopoint;
	private long when;
	
	public LocationInfo (String senderUUID) {
		this.senderUUID = senderUUID;
	}
	
	public String getSenderUUID() {
		return senderUUID;
	}

	public void setSenderUUID(String senderUUID) {
		this.senderUUID = senderUUID;
	}

	public GeoPointInfo getGeopoint() {
		return geopoint;
	}

	public void setGeopoint(GeoPointInfo geopoint) {
		this.geopoint = geopoint;
	}

	public long getWhen() {
		return when;
	}

	public void setWhen(long when) {
		this.when = when;
	}

}
