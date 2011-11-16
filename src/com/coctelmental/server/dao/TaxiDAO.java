package com.coctelmental.server.dao;

import com.coctelmental.server.model.TaxiDriver;

public interface TaxiDAO {

	public TaxiDriver getTaxiDriverByID(String id);
	
	public int addTaxiDriver(TaxiDriver taxiDriver);
		
}
