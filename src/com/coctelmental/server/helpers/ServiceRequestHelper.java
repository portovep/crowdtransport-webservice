package com.coctelmental.server.helpers;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;

import com.coctelmental.server.c2dm.C2DMAuthentication;
import com.coctelmental.server.c2dm.C2DMessaging;
import com.coctelmental.server.model.ServiceRequestInfo;
import com.coctelmental.server.serviceRequests.DeviceInfoStore;
import com.coctelmental.server.serviceRequests.ServiceRequestStore;

public class ServiceRequestHelper {
	
	public static final int E_REQUEST_NOT_FOUND = -2;
	
	// TAXI DRIVER PAYLOADS
	private static final String TAXI_ADDRESS_FROM_PAYLOAD = "notification_addressFrom_name";
	private static final String TAXI_ADDRESS_TO_PAYLOAD = "notification_addressTo_name";
	private static final String TAXI_COMMENT_PAYLOAD = "notification_commnet";
	private static final String TAXI_NUMBER_REQUESTS_PAYLOAD = "taxiDriver_number_requests";
	
	// USER PAYLOADS
	private static final String USER_NOTIFICATION_PAYLOAD = "notify_user";	
	private static final String USER_PAYLOAD_ACCEPT = "accept";
	private static final String USER_PAYLOAD_CANCEL = "cancel";
	
	public static void addServiceRequest(ServiceRequestInfo serviceRequest) {
		int numberOfRequests = ServiceRequestStore.getInstance().addServiceRequest(serviceRequest);
		if (numberOfRequests > 0) {
			// no error, send push notification to taxi driver device
			// attach address names and request commentary as payload data
			String addressFrom = serviceRequest.getAddressFrom();
			if(addressFrom == null)
				addressFrom = "";
			String addressTo = serviceRequest.getAddressTo();
			if(addressTo == null)
				addressTo = "";
			String comment = serviceRequest.getComment();
			if(comment == null)
				comment = "";
			sendTaxiNotification(serviceRequest.getTaxiDriverUUID(), addressFrom, addressTo, comment, String.valueOf(numberOfRequests));
		}
		else {
			System.out.println("Error adding service request");
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

	public static int acceptServiceRequest(String requestID, String taxiDriverUUID) {
		int result = -1;
		// get target request
		ServiceRequestInfo acceptedRequest = ServiceRequestStore.getInstance().getServiceRequest(taxiDriverUUID, requestID);
		if (acceptedRequest != null) {
			String userUUID = acceptedRequest.getUserUUID();
			// notify user
			int responseCode = sendUserNotification(userUUID, USER_PAYLOAD_ACCEPT);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// remove accepted request
				boolean removed = ServiceRequestStore.getInstance().removeServiceRequest(taxiDriverUUID, requestID);
				if (removed) {
					// cancel other request
					cancelAllServiceRequest(taxiDriverUUID);
					result = 0;
				}
			}
		}
		
		return result;
	}
	
	public static boolean cancelServiceRequest(String taxiDriverUUID, String requestID) {
		return ServiceRequestStore.getInstance().removeServiceRequest(taxiDriverUUID, requestID);
	}
	
	public static void cancelAllServiceRequest(String taxiDriverUUID) {
		// remove all request associated with target taxi driver 
		List<String> usersUUIDs = ServiceRequestStore.getInstance().removeAllServiceRequest(taxiDriverUUID);
		// notify cancellation to users
		for(String userUUID : usersUUIDs) {
			int responseCode = sendUserNotification(userUUID, USER_PAYLOAD_CANCEL);
			if (responseCode != HttpURLConnection.HTTP_OK) {
				System.out.println("Error sending C2DM push notification (request cancelation) to users. Result code -> " + responseCode);
			}
		}
	}
	
	private static void sendTaxiNotification(String taxiDriverUUID, String addressFrom, String addressTo, String comment, String nRequests) {
		// get registrationID for taxi driver device
		String registrationID = DeviceInfoStore.getInstance().getRegistrationID(taxiDriverUUID);
		
		System.out.println("Sending taxi notification.. (RegistrationID -> " + registrationID + ")");
		
		if (registrationID != null) {
			// get auth_token to allow communication with Google server
			String authToken = C2DMAuthentication.getNewAuthToken(C2DMAuthentication.EMAIL, C2DMAuthentication.PASSWD);
			// send message to Google server
			int resultCode = -1;
			if (authToken != null && !authToken.isEmpty()) {
				// attach data
				HashMap<String, String> messages = new HashMap<String, String>();
				messages.put(TAXI_ADDRESS_FROM_PAYLOAD, addressFrom);
				messages.put(TAXI_ADDRESS_TO_PAYLOAD, addressTo);
				messages.put(TAXI_COMMENT_PAYLOAD, comment);
				messages.put(TAXI_NUMBER_REQUESTS_PAYLOAD, nRequests);
				resultCode = C2DMessaging.sendMessages(authToken, registrationID, TAXI_ADDRESS_FROM_PAYLOAD, messages);
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
