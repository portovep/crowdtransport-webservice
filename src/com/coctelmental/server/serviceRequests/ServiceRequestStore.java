package com.coctelmental.server.serviceRequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.coctelmental.server.model.ServiceRequestInfo;

public class ServiceRequestStore {

	private static ServiceRequestStore singleton = new ServiceRequestStore();
	
	private HashMap<String, ArrayList<ServiceRequestInfo>> requests;
	
	private ServiceRequestStore() {
		requests = new HashMap<String, ArrayList<ServiceRequestInfo>>();
	}
	
	public static ServiceRequestStore getInstance() {
		return singleton;
	}
	
	public int addServiceRequest(ServiceRequestInfo requestInfo)  {
		int result = 0; // result 0 = error
		String taxiDriverUUID = requestInfo.getTaxiDriverUUID();
		
		System.out.println("New service request received");
		
		if (taxiDriverUUID != null) {
			ArrayList<ServiceRequestInfo> requestList = null;
			synchronized (requests) {
				// get service request list for specific taxi driver
				requestList = requests.get(taxiDriverUUID);
			}
			if (requestList != null) {
				// requests found
				// looking for duplicate request
				synchronized (requestList) {
					String userID = requestInfo.getUserID();
					int duplicateIndex = checkDuplicateRequest(userID, requestList);
					if (duplicateIndex > -1) {
						// remove previous request
						requestList.remove(duplicateIndex);
					}
					// add new request
					requestList.add(requestInfo);
					// get number of requests
					result = requestList.size();
				}
		
			}
			else {
				// no requests found
				// create new request's for those taxi driver
				requestList = new ArrayList<ServiceRequestInfo>();
				// store request
				requestList.add(requestInfo);
				// create new entry in hashmap
				synchronized (requests) {
					requests.put(taxiDriverUUID, requestList);	
				}
				// get number of requests
				result = requestList.size();
			}
		}
		// return number of requests
		return result;
	}
	
	public ServiceRequestInfo getServiceRequest(String taxiDriverUUID, String requestID) {
		ArrayList<ServiceRequestInfo> requestList = null;
		synchronized (requests) {
			requestList = requests.get(taxiDriverUUID);
		}		
			if (requestList != null && !requestList.isEmpty()) {
				synchronized (requestList) {
					for(ServiceRequestInfo request : requestList) {
						// looking for target request
						if (request.getUserUUID().equals(requestID))
							return request;
					}
				}			
			}
		return null;
	}
	
	public List<ServiceRequestInfo> getAllServiceRequest(String taxiDriverUUID) {
		synchronized (requests) {
			return requests.get(taxiDriverUUID);
		}
	}

	public boolean removeServiceRequest(String taxiDriverUUID, String requestID) {
		ArrayList<ServiceRequestInfo> requestList = null;
		synchronized (requests) {
			requestList = requests.get(taxiDriverUUID);
		}
			if (requestList != null && !requestList.isEmpty()) {
				synchronized (requestList) {
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
			}
		return false;
	}
	
	public List<String> removeAllServiceRequest(String taxiDriverUUID) {
		List<String> usersUUIDs = new ArrayList<String>();
		ArrayList<ServiceRequestInfo> requestList = null;
		synchronized (requests) {
			requestList = requests.get(taxiDriverUUID);
		}
			if (requestList != null && !requestList.isEmpty()) {
				synchronized (requestList) {
					Iterator<ServiceRequestInfo> itr = requestList.iterator();
					while (itr.hasNext()) {
						ServiceRequestInfo request = itr.next();
						// save user UUID for push notification
						usersUUIDs.add(request.getUserUUID());
						// remove request
						itr.remove(); // avoid ConcurrentModificationException
					}
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
