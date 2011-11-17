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
	private static final int MAX_DIF_LAT = 700;
	private static final int MAX_DIF_LONG = 700;
	
	public LineLocations() {
		counter = 0;
		collaboratorList = new HashMap<String, Integer>();
		busLocationList = new HashMap<Integer, StoredBusLocation>();
	}
	
	public synchronized void addNewLocation(CollaboratorBusLocation receivedBusLocation) {
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
	
	public synchronized List<BusLocation> getLocations() {
		ArrayList<BusLocation> foundLocations = new ArrayList<BusLocation>();
		
		Iterator<Integer> iterator = busLocationList.keySet().iterator();
		while(iterator.hasNext()) {
			int busLocationID = iterator.next();
			StoredBusLocation sbl = busLocationList.get(busLocationID);
			if(isOldLocation(sbl.getWhenStored())) {
				// borramos colaboradores asociados
				removeCollaborators(busLocationID);
				// borramos la última localización proporcionada por el iterador
				iterator.remove();
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
		int diffLat = fromGP.getLatitude() - toGP.getLatitude();
		System.out.println("\ndiffLat: " + Math.abs(diffLat));
		if(Math.abs(diffLat) > MAX_DIF_LAT )
			return false;
		
		int diffLong = fromGP.getLongitude() - toGP.getLongitude();
		System.out.println("diffLong: " + Math.abs(diffLong));
		if(Math.abs(diffLong) > MAX_DIF_LONG)
			return false;
		
		return true;
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
