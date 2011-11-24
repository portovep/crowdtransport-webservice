package com.coctelmental.server.utils;

import com.coctelmental.server.model.GeoPoint;

public class Tools {

	public static double calculateDistance(GeoPoint gp1, GeoPoint gp2) {
		/*
		Haversine formula:
		
		a = sin²(Δlat/2) + cos(lat1).cos(lat2).sin²(Δlong/2)
		c = 2.atan2(√a, √(1−a))
		distance = R.c
		
		*/
		final double radious = 6371000; // earth radious in meters
		
		double lat1 = gp1.getLatitudeE6() / 1E6;
		double lat2 = gp2.getLatitudeE6() / 1E6;
		
		double long1 = gp1.getLongitudeE6() / 1E6;
		double long2 = gp2.getLongitudeE6() / 1E6;
		
		// calculate Δlat and Δlong in radians
		double dLat = Math.toRadians(lat2 - lat1);
		double dLong = Math.toRadians(long2 - long1);
		
		// calculate a
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) + 
					Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
					Math.sin(dLong/2) * Math.sin(dLong/2);
		
		// calculate c
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		
		// calculate distance
		double distance = radious * c;
		return distance;
	}
	
}
