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
	
	public void addBusLocation(String resourceID, CollaboratorBusLocation receivedBusLocation) {
		LineLocations lineLocations;
		synchronized (lineLocationsList) {
			lineLocations = lineLocationsList.get(resourceID);
		}
		if(lineLocations != null) {
			synchronized (lineLocations) {
				lineLocations.addNewLocation(receivedBusLocation);				
			}
		}
		else {
			lineLocations = new LineLocations();   
			lineLocations.addNewLocation(receivedBusLocation);
			synchronized (lineLocationsList) {
				lineLocationsList.put(resourceID, lineLocations);	
			}
		}
	}

	public List<BusLocation> getBusLocations(String resourceID) {
		List<BusLocation> busLocations = null;
		LineLocations targetLineLocations;
		synchronized (lineLocationsList) {
			targetLineLocations = lineLocationsList.get(resourceID);	
		}
		if (targetLineLocations != null) {
			synchronized (targetLineLocations) {
				busLocations = targetLineLocations.getLocations();	
				if (busLocations.isEmpty())
					busLocations = null;
			}
		}		
		return busLocations;
	}
}
