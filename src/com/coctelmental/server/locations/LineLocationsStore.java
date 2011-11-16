package com.coctelmental.server.locations;

import java.util.HashMap;
import java.util.List;

import com.coctelmental.server.model.BusLocation;
import com.coctelmental.server.model.CollaboratorBusLocation;

public class LineLocationsStore {
	
	private static LineLocationsStore singletonLineLocationsStore = new LineLocationsStore();
	private HashMap<String, LineLocations> lineLocationsList;
	
	private LineLocationsStore() {
		lineLocationsList = new HashMap<String, LineLocations>();
	}
	
	public static LineLocationsStore getInstance() {
		return singletonLineLocationsStore;
	}
	
	public synchronized void addBusLocation(String resourceID, CollaboratorBusLocation receivedBusLocation) {
		LineLocations lineLocations = lineLocationsList.get(resourceID);
		if(lineLocations != null) {
			lineLocations.addNewLocation(receivedBusLocation);
		}
		else {
			lineLocations = new LineLocations();   
			lineLocations.addNewLocation(receivedBusLocation);
			lineLocationsList.put(resourceID, lineLocations);
		}
	}

	public synchronized List<BusLocation> getBusLocations(String resourceID) {
		List<BusLocation> busLocations = null;
		LineLocations targetLineLocations = lineLocationsList.get(resourceID);
		if (targetLineLocations != null) {
			busLocations = targetLineLocations.getLocations();
			if (busLocations.isEmpty())
				busLocations = null;
		}		
		return busLocations;
	}
}
