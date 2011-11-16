package com.coctelmental.server.helpers;

import java.util.List;

import com.coctelmental.server.locations.LineLocationsStore;
import com.coctelmental.server.model.BusLocation;
import com.coctelmental.server.model.CollaboratorBusLocation;

public class LocationHelper {

	public void addBusLocation(String resourceID, CollaboratorBusLocation receivedBusLocation) {
		LineLocationsStore.getInstance().addBusLocation(resourceID, receivedBusLocation);
	}

	public List<BusLocation> getBusLocations(String resourceID) {
		return LineLocationsStore.getInstance().getBusLocations(resourceID);
	}
}
