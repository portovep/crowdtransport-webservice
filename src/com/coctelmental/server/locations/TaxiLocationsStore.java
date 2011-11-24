package com.coctelmental.server.locations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.coctelmental.server.model.GeoPoint;
import com.coctelmental.server.model.Location;
import com.coctelmental.server.model.TaxiDriverLocation;
import com.coctelmental.server.model.TaxiLocation;
import com.coctelmental.server.utils.Tools;

public class TaxiLocationsStore {
	
	private static final int MAX_DISTANCE = 2000; // 2 km
	private static final long LOCATION_LIFE_TIME = 30000; // 1/2 minute
	
	private static TaxiLocationsStore singletonTaxiLocationsStore = new TaxiLocationsStore();
	private HashMap<String, Location> taxiLocationsList;
	
	private TaxiLocationsStore() {
		taxiLocationsList = new HashMap<String, Location>();
	}
	
	public static TaxiLocationsStore getInstance() {
		return singletonTaxiLocationsStore;
	}
	
	public void addTaxiLocation(TaxiDriverLocation taxiDriverLocation) {
		String taxiDriverID = taxiDriverLocation.getTaxiDriverID();
		
		if (taxiDriverID != null && !taxiDriverID.isEmpty()) {
			// create new location
			Location location = new Location();
			location.setGeopoint(taxiDriverLocation.getGeopoint());
			location.setWhen(System.currentTimeMillis());			
							
			// add to list
			synchronized (taxiLocationsList) {
				/* NOTE: 
				 * 		It's new taxi driver -> new entry is created
				 * 		Taxi driver exists in the list -> old entry is replaced
				 */		
				taxiLocationsList.put(taxiDriverID, location);
			}					
		}
	}
	
	public List<TaxiLocation> getTaxiLocations(GeoPoint gpUserLocation) {
		List<TaxiLocation> taxiLocations = new ArrayList<TaxiLocation>();
		
		synchronized (taxiLocationsList) {
			Iterator<String> itr = taxiLocationsList.keySet().iterator();
			while(itr.hasNext()) {
				String taxiDriverID = itr.next();
				Location location = taxiLocationsList.get(taxiDriverID);
				if (!isOld(location)) {
					// is near to user location?
					if (isWithinRange(gpUserLocation, location.getGeopoint())) {
						// build TaxiLocation
						TaxiLocation taxiLocation = new TaxiLocation();
						taxiLocation.setTaxiDriverID(taxiDriverID);
						taxiLocation.setLocation(location);
						// add to location list which will be sent to user
						taxiLocations.add(taxiLocation);
					}
				}
				else
					// is old, stored location is removed
					itr.remove(); // avoid ConcurrentModificationException
			}
		}	
		return taxiLocations;
	}
	
	private boolean isWithinRange(GeoPoint gp1, GeoPoint gp2) {
		double distance = Tools.calculateDistance(gp1, gp2);
		if (distance <= MAX_DISTANCE)
			return true;
		return false;
	}

	private boolean isOld(Location location) {
		long diff = System.currentTimeMillis() - location.getWhen();
		if (diff >= LOCATION_LIFE_TIME)
			return true;
		return false;
	}
}
