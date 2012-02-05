package com.coctelmental.server.locations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.coctelmental.server.model.BusLocation;
import com.coctelmental.server.model.CollaboratorBusLocation;
import com.coctelmental.server.model.StoredBusLocation;
import com.coctelmental.server.utils.Tools;

public class LineLocations {
	
	private HashMap<String, Integer> collaboratorList;
	private HashMap<Integer, StoredBusLocation> busLocationList;
	
	private int counter;
	
	private static final long LOCATION_LIFE_TIME = 60000;  // 1 min
	private static final int MAX_DISTANCE_IN_METERS = 200; // distance in meters
	
	public LineLocations() {
		counter = 0;
		collaboratorList = new HashMap<String, Integer>();
		busLocationList = new HashMap<Integer, StoredBusLocation>();
	}
	
	public void addNewLocation(CollaboratorBusLocation receivedBusLocation) {
		// check if it's new collaborator
		if (isNewUser(receivedBusLocation.getUserID())) {
			System.out.println("Log: New user");
			// check whether new location is within stored location's area
			Integer busLocationID = matchWithStoredBusLocation(receivedBusLocation);
			if (busLocationID != null) {
				System.out.println("Log: Update bus position");
				// new location is within the area of previous location
				// merge with previous location
				mergeWithStoredBusLocation(busLocationID, receivedBusLocation);
				// add new collaborator
				collaboratorList.put(receivedBusLocation.getUserID(), busLocationID);
			}
			else {
				System.out.println("Log: Create new bus position");
				// new bus location
				// get new id
				int newLocationID = generateLocationID();
				// build new StoredBusLocation
				StoredBusLocation sbl = createStoredBusLocation(newLocationID, receivedBusLocation);
				// add new bus location
				busLocationList.put(newLocationID, sbl);
				// add collaborator 
				collaboratorList.put(receivedBusLocation.getUserID(), newLocationID);
			}
				
		}
		else {
			System.out.println("Log: existing user");
			Integer lastSentLocationID = collaboratorList.get(receivedBusLocation.getUserID());
			if(lastSentLocationID != null) {
				if (isNear(busLocationList.get(lastSentLocationID), receivedBusLocation)) {
					System.out.println("Log: UpdateBusPosition");
					mergeWithStoredBusLocation(lastSentLocationID, receivedBusLocation);
				}
				else {
					// collaborator left previous bus
					// remove collaborator from location list of previous bus
					System.out.println("Log: Borrado User");
					collaboratorList.remove(receivedBusLocation.getUserID());				
				}
			}			
		}
	}
	
	public List<BusLocation> getLocations() {
		ArrayList<BusLocation> foundLocations = new ArrayList<BusLocation>();
		
		Iterator<Integer> iterator = busLocationList.keySet().iterator();
		while(iterator.hasNext()) {
			int busLocationID = iterator.next();
			StoredBusLocation sbl = busLocationList.get(busLocationID);
			if(isOldLocation(sbl.getWhenStored())) {
				// remove associated collaborators
				removeCollaborators(busLocationID);
				// remove old location
				iterator.remove(); // avoid ConcurrentModificationException
			}
			else {
				BusLocation bl = createBusLocation(sbl);
				bl.setnCollaborators(getNumberOfCollaborators(busLocationID));
				foundLocations.add(bl);
			}		
		}
		return foundLocations;
	}
	
	private int generateLocationID() {
		if (counter == Integer.MAX_VALUE)
			counter = 0; // reset counter
		counter += 1;
		return counter;
	}
	
	public int getNumberOfLocationsStored() {
		return busLocationList.size();
	}
	
	private boolean isNewUser(String userID) {
		return !collaboratorList.containsKey(userID);
	}
	
	private Integer matchWithStoredBusLocation(CollaboratorBusLocation receivedBusLocation) {			
		Iterator<Integer> iterator = busLocationList.keySet().iterator();
		while (iterator.hasNext()) {
			Integer busLocationID = iterator.next();
			StoredBusLocation storedBusLocation = busLocationList.get(busLocationID);
			if (isNear(storedBusLocation, receivedBusLocation))
				return busLocationID;
		}		
		return null;
	}
	
	private boolean isNear(StoredBusLocation fromGP, CollaboratorBusLocation toGP) {
		Double distance = Tools.calculateDistance(fromGP.getGeopoint(), toGP.getGeopoint());
		
		System.out.println("Log: Distance: " + distance+ "m");
		
		if (distance >= MAX_DISTANCE_IN_METERS)
			return false;
		return true;
	}
	
	private void mergeWithStoredBusLocation(Integer busLocationID, CollaboratorBusLocation receivedBusLocation) {
		// build new StoredBusLocation
		StoredBusLocation sbl = createStoredBusLocation(busLocationID, receivedBusLocation);
		// update previous location
		busLocationList.put(busLocationID, sbl);
	}
	
	private StoredBusLocation createStoredBusLocation(Integer id, CollaboratorBusLocation receivedBusLocation) {
		StoredBusLocation sbl = new StoredBusLocation(id);
		sbl.setGeopoint(receivedBusLocation.getGeopoint());
		sbl.setWhenStored(System.currentTimeMillis());
		return sbl;
	}
	
	private BusLocation createBusLocation(StoredBusLocation storedBusLocation) {
		BusLocation busLocation = new BusLocation(Integer.toString(storedBusLocation.getBusLocationID()));
		busLocation.setGeopoint(storedBusLocation.getGeopoint());
		busLocation.setWhen(storedBusLocation.getWhenStored());
		return busLocation;
	}
	
	private boolean isOldLocation(long whenStored) {
		long now = System.currentTimeMillis();
		long diff = now - whenStored;
		if (diff > LOCATION_LIFE_TIME)
			return true;
		return false;
	}
	
	private int getNumberOfCollaborators(int locationID) {
		int nCollaborators = 0;
		Iterator<String> iterator = collaboratorList.keySet().iterator();
		while (iterator.hasNext()) {		
			if(collaboratorList.get(iterator.next()).equals(locationID))
				nCollaborators += 1;
		}
		return nCollaborators;
	}
	
	private void removeCollaborators(int locationID) {
		Iterator<String> iterator = collaboratorList.keySet().iterator();
		while (iterator.hasNext()) {		
			String collaboratorID = iterator.next();
			if(collaboratorList.get(collaboratorID).equals(locationID))
				// remove collaborator
				iterator.remove();
		}
	}
	
}
