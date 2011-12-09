package com.coctelmental.server.serviceRequests;

import java.util.HashMap;

import com.coctelmental.server.model.DeviceInfo;

public class DeviceInfoStore {

	private static DeviceInfoStore singleton = new DeviceInfoStore();
	private HashMap<String, String> deviceList;
	
	private DeviceInfoStore() {
		deviceList = new HashMap<String, String>();
	}
	
	public static DeviceInfoStore getInstance() {
		return singleton;
	}
	
	public synchronized void addDeviceInfo(DeviceInfo deviceInfo) {
		// add device UUID and device REGISTRATION_ID
		deviceList.put(deviceInfo.getUUID(), deviceInfo.getRegistrationID());
	}
	
	public synchronized String getRegistrationID(String deviceUUID) {
		return deviceList.get(deviceUUID);
	}
	

}
