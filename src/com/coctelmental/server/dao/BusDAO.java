package com.coctelmental.server.dao;

import com.coctelmental.server.model.BusDriver;

public interface BusDAO {

	public BusDriver getBusDriverByID(String id);
	
	public int addBusDriver(BusDriver busDriver);
		
}
