package com.coctelmental.server.helpers;

import java.util.List;

import com.coctelmental.server.c2dm.C2DMAuthentication;
import com.coctelmental.server.c2dm.C2DMessaging;
import com.coctelmental.server.model.ServiceRequestInfo;
import com.coctelmental.server.serviceRequests.DeviceInfoStore;
import com.coctelmental.server.serviceRequests.ServiceRequestStore;

public class ServiceRequestHelper {
	
	private static final String TAXI_NOTIFICATION_PAYLOAD = "notify_taxiDriver";
	
	public static void addServiceRequest(ServiceRequestInfo serviceRequest) {
		int result = ServiceRequestStore.getInstance().addServiceRequest(serviceRequest);
		if (result == 1) {
			// send push notification to taxi driver device
			// payloadData = serviceRequestID
			String serviceRequestID = serviceRequest.getUserUUID();
			sendTaxiNotification(serviceRequest.getTaxiDriverUUID(), serviceRequestID);
		}
		else {
			// TO-DO
		}
	}

	public static ServiceRequestInfo getServiceRequest(String taxiDriverUUID, String requestID) {
		return ServiceRequestStore.getInstance().getServiceRequest(taxiDriverUUID, requestID);
	}
	
	public static List<ServiceRequestInfo> getAllServiceRequest(String taxiDriverUUID) {
		List<ServiceRequestInfo> serviceRequests = ServiceRequestStore.getInstance().getAllServiceRequest(taxiDriverUUID);
		if (serviceRequests != null && !serviceRequests.isEmpty())
			return serviceRequests;
		return null;
	}
	
	public static boolean cancelServiceRequest(String taxiDriverUUID, String requestID) {
		return ServiceRequestStore.getInstance().cancelServiceRequest(taxiDriverUUID, requestID);
	}
	
	private static void sendTaxiNotification(String taxiDriverUUID, String payloadData) {
		// get registrationID for taxi driver device
		String registrationID = DeviceInfoStore.getInstance().getRegistrationID(taxiDriverUUID);
		
		System.out.println("Sending taxi notification.. (RegistrationID -> " + registrationID + ")");
		
		if (registrationID != null) {
			// get auth_token to allow communication with Google server
			String authToken = C2DMAuthentication.getNewAuthToken(C2DMAuthentication.EMAIL, C2DMAuthentication.PASSWD);
			// send message to Google server
			int resultCode = -1;
			if (authToken != null && !authToken.isEmpty()) {
				resultCode = C2DMessaging.sendMessage(authToken, registrationID, TAXI_NOTIFICATION_PAYLOAD, payloadData);
				System.out.println("C2DM Taxi notification sent. Result code -> " + resultCode);
			}
		}
	}

}
