package com.coctelmental.server.locations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.coctelmental.server.model.BusLocation;
import com.coctelmental.server.model.CollaboratorBusLocation;
import com.coctelmental.server.model.StoredBusLocation;

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
		// comprobamos si es un nuevo colaborador
		if (isNewUser(receivedBusLocation.getUserID())) {
			System.out.println("Log: New user");
			// encuadrar en hotspot si es posible
			Integer busLocationID = matchWithStoredBusLocation(receivedBusLocation);
			if (busLocationID != null) {
				System.out.println("Log: Update bus position");
				// el nuevo punto esta cerca de otro existente
				// lo fusionamos con el existente
				mergeWithStoredBusLocation(busLocationID, receivedBusLocation);
				// añadimos al usuario a la lista de asociados
				collaboratorList.put(receivedBusLocation.getUserID(), busLocationID);
			}
			else {
				System.out.println("Log: Create new bus position");
				// probablemente es un nuevo bus
				// creamos un nuevo ID
				int newLocationID = generateLocationID();
				// creamos una nueva localización
				StoredBusLocation sbl = createStoredBusLocation(newLocationID, receivedBusLocation);
				// añadimos la localización 
				busLocationList.put(newLocationID, sbl);
				// añadimos el usuario 
				collaboratorList.put(receivedBusLocation.getUserID(), newLocationID);
			}
				
		}
		// NO ES UN USUARIO NUEVO
		else {
			System.out.println("Log: existing user");
			Integer lastSentLocationID = collaboratorList.get(receivedBusLocation.getUserID());
			if(lastSentLocationID != null) {
				if (isNear(busLocationList.get(lastSentLocationID), receivedBusLocation)) {
					System.out.println("Log: UpdateBusPosition");
					mergeWithStoredBusLocation(lastSentLocationID, receivedBusLocation);
				}
				else {
					// el usuaro ha dejado el bus
					// borramos al usuario de la lista
					System.out.println("Log: Borrado User");
					collaboratorList.remove(receivedBusLocation.getUserID());				
				}
			}
			//else			
		}
	}
	
	public List<BusLocation> getLocations() {
		ArrayList<BusLocation> foundLocations = new ArrayList<BusLocation>();
		
		Iterator<Integer> iterator = busLocationList.keySet().iterator();
		while(iterator.hasNext()) {
			int busLocationID = iterator.next();
			StoredBusLocation sbl = busLocationList.get(busLocationID);
			if(isOldLocation(sbl.getWhenStored())) {
				// borramos colaboradores asociados
				removeCollaborators(busLocationID);
				// borramos la última localización proporcionada por el iterador
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
		Double distance = calculateDistance(fromGP.getLatitude(), fromGP.getLongitude(), 
				toGP.getLatitude(), toGP.getLongitude());
		
		System.out.println("Log: Distance: " + distance+ "m");
		
		if (distance >= MAX_DISTANCE_IN_METERS)
			return false;
		return true;
	}
	
	private static double calculateDistance(double rlat1, double rlong1, double rlat2, double rlong2) {
		/*
		Haversine formula:
		
		a = sin²(Δlat/2) + cos(lat1).cos(lat2).sin²(Δlong/2)
		c = 2.atan2(√a, √(1−a))
		distance = R.c
		
		*/
		final double radious = 6371000; // earth radious in meters
		
		double lat1 = rlat1 / 1E6;
		double lat2 = rlat2 / 1E6;
		
		double long1 = rlong1 / 1E6;
		double long2 = rlong2 / 1E6;
		
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
	
	private void mergeWithStoredBusLocation(Integer busLocationID, CollaboratorBusLocation receivedBusLocation) {
		// creamos una nueva storedLocation
		StoredBusLocation sbl = createStoredBusLocation(busLocationID, receivedBusLocation);
		// añadimos el nuevo punto sobreescribiendo el anterior
		busLocationList.put(busLocationID, sbl);
	}
	
	private StoredBusLocation createStoredBusLocation(Integer id, CollaboratorBusLocation receivedBusLocation) {
		StoredBusLocation sbl = new StoredBusLocation(id);
		sbl.setLatitude(receivedBusLocation.getLatitude());
		sbl.setLongitude(receivedBusLocation.getLongitude());
		sbl.setWhenStored(System.currentTimeMillis());
		return sbl;
	}
	
	private BusLocation createBusLocation(StoredBusLocation storedBusLocation) {
		BusLocation busLocation = new BusLocation(Integer.toString(storedBusLocation.getBusLocationID()));
		busLocation.setLatitude(storedBusLocation.getLatitude());
		busLocation.setLongitude(storedBusLocation.getLongitude());
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
				// borramos el último usuario proporcionado por el iterador
				iterator.remove();
		}
	}
	
}
