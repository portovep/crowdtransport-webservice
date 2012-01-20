package com.coctelmental.server.helpers;

import com.coctelmental.server.dao.TaxiLicenceDAO;
import com.coctelmental.server.dao.TaxiLicenceDAOImpl;
import com.coctelmental.server.dao.TaxiDAO;
import com.coctelmental.server.dao.TaxiDAOImpl;
import com.coctelmental.server.model.TaxiDriver;
import com.coctelmental.server.utils.MailHandler;


public class TaxiHelper {

	public static final int EC_BBDD_ERROR = -1;
	public static final int EC_INVALID_DNI = -2;	
	public static final int EC_INVALID_LICENCE = -3;	

	private TaxiDAO taxiDAO;
	private TaxiLicenceDAO taxiLicenceDAO;
	
	public TaxiHelper() {
		taxiDAO = new TaxiDAOImpl();
		taxiLicenceDAO = new TaxiLicenceDAOImpl();
	}
	
	public TaxiDriver getTaxiDriver(String dni) {
		return taxiDAO.getTaxiDriverByID(dni);
	}
	
	public int addTaxiDriver(TaxiDriver taxiDriver) {
		//set default resultCode to specify correct registration
		int resultCode = 1;
		String dni = taxiDriver.getDni();
		String licenceNumber = taxiDriver.getLicenceNumber();
		
		// check if user with targetDni already exist
		if (this.getTaxiDriver(dni) != null)
			resultCode = EC_INVALID_DNI;
		else {
			// licence verification
			String validDni = taxiLicenceDAO.getOwnerID(licenceNumber);
			if (!dni.equals(validDni))
				//set resultCode to invalid licenceNumber
				resultCode = EC_INVALID_LICENCE;
			else {
				// valid licence, then, add user
				int result= taxiDAO.addTaxiDriver(taxiDriver);
				// check correct update in bbdd
				if (result < 1) {
					resultCode = EC_BBDD_ERROR;
				}
				else {
					// send confirmation email
					MailHandler.sendRegitrationMail(taxiDriver.getFullName(), taxiDriver.getEmail());
				}
			}			
		}
		return resultCode;
	}
}
