package com.coctelmental.server.serviceRequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
	
	public synchronized int addServiceRequest(ServiceRequestInfo requestInfo)  {
		int result = 0; // result 0 = error
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
			
			result = 1;
		}		
		return result;
	}
	
	public synchronized ServiceRequestInfo getServiceRequest(String taxiDriverUUID, String requestID) {
		ArrayList<ServiceRequestInfo> requestList = requests.get(taxiDriverUUID);
		if (requestList != null && !requestList.isEmpty()) {
			for(ServiceRequestInfo request : requestList) {
				// looking for target request
				if (request.getUserUUID().equals(requestID))
					// TO-DO (Check timestamp)
					return request;
			}
		}
		return null;
	}
	
	public synchronized List<ServiceRequestInfo> getAllServiceRequest(String taxiDriverUUID) {
		// TO-DO (Check timestamp)
		return requests.get(taxiDriverUUID);
	}

	public synchronized boolean removeServiceRequest(String taxiDriverUUID, String requestID) {
		ArrayList<ServiceRequestInfo> requestList = requests.get(taxiDriverUUID);
		if (requestList != null && !requestList.isEmpty()) {
			Iterator<ServiceRequestInfo> itr = requestList.iterator();
			while (itr.hasNext()) {
				ServiceRequestInfo request = itr.next(); 
				// looking for target request
				if (request.getUserUUID().equals(requestID))
					// remove request
					itr.remove(); // avoid ConcurrentModificationException
					return true;
			}
		}
		return false;
	}
	
	public synchronized List<String> removeAllServiceRequest(String taxiDriverUUID) {
		List<String> usersUUIDs = new ArrayList<String>();
		ArrayList<ServiceRequestInfo> requestList = requests.get(taxiDriverUUID);
		if (requestList != null && !requestList.isEmpty()) {
			Iterator<ServiceRequestInfo> itr = requestList.iterator();
			while (itr.hasNext()) {
				ServiceRequestInfo request = itr.next();
				// save user UUID for push notification
				usersUUIDs.add(request.getUserUUID());
				// remove request
				itr.remove(); // avoid ConcurrentModificationException
			}
		}
		return usersUUIDs;
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
