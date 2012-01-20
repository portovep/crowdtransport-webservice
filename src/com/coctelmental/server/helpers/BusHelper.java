package com.coctelmental.server.helpers;

import com.coctelmental.server.dao.BusCompanyDAO;
import com.coctelmental.server.dao.BusCompanyDAOImpl;
import com.coctelmental.server.dao.BusDAO;
import com.coctelmental.server.dao.BusDAOImpl;
import com.coctelmental.server.model.BusDriver;
import com.coctelmental.server.utils.MailHandler;

public class BusHelper {

	public static final int EC_BBDD_ERROR = -1;
	public static final int EC_INVALID_DNI = -2;	
	public static final int EC_INVALID_AUTH_CODE = -3;	

	private BusDAO busDAO;
	private BusCompanyDAO busCompanyDAO;
	
	public BusHelper() {
		busDAO = new BusDAOImpl();
		busCompanyDAO = new BusCompanyDAOImpl();
	}
	
	public BusDriver getBusDriver(String dni) {
		return busDAO.getBusDriverByID(dni);
	}
	
	public int addBusDriver(BusDriver busDriver) {
		//set default resultCode to specify correct registration
		int resultCode = 1;
		String dni = busDriver.getDni();
		String companyAuthCOde = busDriver.getCompanyAuthCode();
		
		// check if user with targetDni already exist
		if (this.getBusDriver(dni) != null)
			resultCode = EC_INVALID_DNI;
		else {
			// company authorization code verification
			String validCompanyAuthCode = busCompanyDAO.getCompanyAuthCode(busDriver.getCompanyCIF());
			if (!companyAuthCOde.equals(validCompanyAuthCode))
				//set resultCode to invalid licenceNumber
				resultCode = EC_INVALID_AUTH_CODE;
			else {
				// valid code, then, add user
				int result= busDAO.addBusDriver(busDriver);
				// check correct update in bbdd
				if (result < 1) {
					resultCode = EC_BBDD_ERROR;
				}
				else {
					// send confirmation email
					MailHandler.sendRegitrationMail(busDriver.getFullName(), busDriver.getEmail());
				}
			}			
		}
		return resultCode;
	}
}	
