package com.coctelmental.server.helpers;

import java.util.List;

import com.coctelmental.server.dao.CityDAO;
import com.coctelmental.server.dao.CityDAOImpl;

public class CityHelper {
	
	private CityDAO cityDAO;

	public CityHelper() {
		cityDAO = new CityDAOImpl();
	}
	
	public String[] getCities() {
		List<String> citiesAvailable = cityDAO.getCityNames();
		String[] cities = new String[citiesAvailable.size()];
		System.arraycopy(citiesAvailable.toArray(), 0, cities, 0, citiesAvailable.size());
		return cities;
	}
}
