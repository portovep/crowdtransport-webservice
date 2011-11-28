package com.coctelmental.server.helpers;

import java.util.List;

import com.coctelmental.server.locations.TaxiLocationsStore;
import com.coctelmental.server.model.MyGeoPoint;
import com.coctelmental.server.model.TaxiDriverLocation;
import com.coctelmental.server.model.TaxiLocation;

public class TaxiLocationHelper {
	
	public void addTaxiLocation(TaxiDriverLocation taxiDriverLocation) {
		String taxiDriverID = taxiDriverLocation.getTaxiDriverID();
		
		if (taxiDriverID != null && !taxiDriverID.isEmpty()) {
			TaxiLocationsStore.getInstance().addTaxiLocation(taxiDriverLocation);
		}
		
	}
	
	public List<TaxiLocation> getTaxiLocations(MyGeoPoint userLocation) {
		List<TaxiLocation> taxiLocations = null;
		if (userLocation != null) {
			taxiLocations = TaxiLocationsStore.getInstance().getTaxiLocations(userLocation);
			// check empty results
			if (taxiLocations != null &&  taxiLocations.isEmpty())
				return null;
		}
		return taxiLocations;
	}

}
