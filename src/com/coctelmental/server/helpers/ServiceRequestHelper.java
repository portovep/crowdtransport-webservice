package com.coctelmental.server.helpers;

import com.coctelmental.server.c2dm.C2DMAuthentication;
import com.coctelmental.server.c2dm.C2DMessaging;
import com.coctelmental.server.serviceRequests.DeviceInfoStore;

public class ServiceRequestHelper {
	
	private static final String TAXI_NOTIFICATION_PAYLOAD = "taxi_taxirequest_received";
	
	public static void sendTaxiNotification(String taxiDriverUUID) {
		// get registrationID for taxi driver device
		String registrationID = DeviceInfoStore.getInstance().getRegistrationID(taxiDriverUUID);
		
		System.out.println("Sending taxi notification.. (RegistrationID -> " + registrationID + ")");
		
		if (registrationID != null) {
			// get auth_token to allow communication with Google server
			String authToken = C2DMAuthentication.getNewAuthToken(C2DMAuthentication.EMAIL, C2DMAuthentication.PASSWD);
			// send message to Google server
			int resultCode = -1;
			if (authToken != null && !authToken.isEmpty()) {
				resultCode = C2DMessaging.sendMessage(authToken, registrationID, TAXI_NOTIFICATION_PAYLOAD, "new request");
				System.out.println("C2DM Taxi notification sent. Result code -> " + resultCode);
			}
		}
	}

}
