package com.coctelmental.server.helpers;

import java.net.HttpURLConnection;
import java.util.List;

import com.coctelmental.server.c2dm.C2DMAuthentication;
import com.coctelmental.server.c2dm.C2DMessaging;
import com.coctelmental.server.model.ServiceRequestInfo;
import com.coctelmental.server.serviceRequests.DeviceInfoStore;
import com.coctelmental.server.serviceRequests.ServiceRequestStore;

public class ServiceRequestHelper {
	
	public static final int E_REQUEST_NOT_FOUND = -2;
	
	private static final String TAXI_NOTIFICATION_PAYLOAD = "notify_taxiDriver";
	private static final String USER_NOTIFICATION_PAYLOAD = "notify_user";
	
	private static final String USER_PAYLOAD_ACCEPT = "accept";
	
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
		return ServiceRequestStore.getInstance().removeServiceRequest(taxiDriverUUID, requestID);
	}

	public static int acceptServiceRequest(String requestID, String taxiDriverUUID) {
		int result = -1;
		// get target request
		ServiceRequestInfo acceptedRequest = ServiceRequestStore.getInstance().getServiceRequest(taxiDriverUUID, requestID);
		if (acceptedRequest != null) {
			String userUUID = acceptedRequest.getUserUUID();
			// notify user
			int responseCode = sendUserNotification(userUUID, USER_PAYLOAD_ACCEPT);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// cancel request
				boolean canceled = ServiceRequestHelper.cancelServiceRequest(taxiDriverUUID, requestID);
				if (canceled) {
					result = 0;
				}
			}
		}
		
		return result;
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
	
	private static int sendUserNotification(String userUUID, String payloadData) {
		int result = -1;
		// get registrationID for user device
		String registrationID = DeviceInfoStore.getInstance().getRegistrationID(userUUID);
		
		System.out.println("Sending user notification.. (RegistrationID -> " + registrationID + ")");
		
		if (registrationID != null) {
			// get auth_token to allow communication with Google server
			String authToken = C2DMAuthentication.getNewAuthToken(C2DMAuthentication.EMAIL, C2DMAuthentication.PASSWD);
			// send message to Google server
			int resultCode = -1;
			if (authToken != null && !authToken.isEmpty()) {
				resultCode = C2DMessaging.sendMessage(authToken, registrationID, USER_NOTIFICATION_PAYLOAD, payloadData);
				System.out.println("C2DM User notification sent. Result code -> " + resultCode);
				// return http response code
				return resultCode;
			}
		}
		
		return result;
	}

}
