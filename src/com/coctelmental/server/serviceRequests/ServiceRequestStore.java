package com.coctelmental.server.serviceRequests;

import java.util.ArrayList;
import java.util.HashMap;

import com.coctelmental.server.helpers.ServiceRequestHelper;
import com.coctelmental.server.model.ServiceRequestInfo;

public class ServiceRequestStore {

	private static ServiceRequestStore singleton = new ServiceRequestStore();
	
	private HashMap<String, ArrayList<ServiceRequestInfo>> requests;
	
	public static ServiceRequestStore getInstance() {
		return singleton;
	}
	
	private ServiceRequestStore() {
		requests = new HashMap<String, ArrayList<ServiceRequestInfo>>();
	}
	
	public synchronized void addServiceRequest(ServiceRequestInfo requestInfo)  {
		String taxiDriverUUID = requestInfo.getTaxiDriverUUID();
		
		System.out.println("New service request received");
		
		if (taxiDriverUUID != null) {
			if (requests.containsKey(taxiDriverUUID)) {
				// get service request list for specific taxi driver
				ArrayList<ServiceRequestInfo> requestList = requests.get(taxiDriverUUID);
				
				// looking for duplicate request
				String userID = requestInfo.getUserID();
				int duplicateIndex = checkDuplicateRequest(userID, requestList);
				if (duplicateIndex > -1) {
					// remove previous request
					requestList.remove(duplicateIndex);
				}
				// add new request
				requestList.add(requestInfo);		
			}
			else {
				// create new request's for those taxi driver
				ArrayList<ServiceRequestInfo> requestList = new ArrayList<ServiceRequestInfo>();
				// store request
				requestList.add(requestInfo);
				// create new entry in hashmap
				requests.put(taxiDriverUUID, requestList);
			}
			
			// send push notification to taxi driver device
			ServiceRequestHelper.sendTaxiNotification(taxiDriverUUID);	
		}		
	}
	
	private int checkDuplicateRequest(String userID, ArrayList<ServiceRequestInfo> requestList) {
		/*
		 * return -1 if there are no duplicates
		 * otherwise return the position of the duplicate request
		 */
		int result = -1;
		for(ServiceRequestInfo request : requestList) {
			String requestUserID = request.getUserID();
			if(requestUserID != null && requestUserID.equals(userID))
				result = requestList.indexOf(request);
		}		
		return result;
	}
}
