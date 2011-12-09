package com.coctelmental.server.c2dm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class C2DMessaging {

	private static final String GOOGLE_SERVICE_URI = "https://android.clients.google.com/c2dm/send";
	private static final String ENCODING = "UTF-8";
	
	public static int sendMessage(String auth_token, String registrationId, String messageKey, String messageData) {

		int responseCode = HttpURLConnection.HTTP_UNAVAILABLE;
		
		try{
			// build POST request
			StringBuilder builder = new StringBuilder();
			builder.append("registration_id=").append(registrationId);
			builder.append("&collapse_key=").append(messageKey);
			builder.append("&data.").append(messageKey).append("=");
			builder.append(URLEncoder.encode(messageData, ENCODING));
	
			byte[] requestData = builder.toString().getBytes(ENCODING);
		
			// setup connection
			URL url = new URL(GOOGLE_SERVICE_URI);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			conn.setRequestProperty("Content-Length", Integer.toString(requestData.length));
			conn.setRequestProperty("Authorization", "GoogleLogin auth=" + auth_token);
			// to avoid certificate exceptions with untrusted SSL certificates
			conn.setHostnameVerifier(new CustomizedHostnameVerifier());
		
			// send data
			OutputStream out = conn.getOutputStream();
			out.write(requestData);
			out.close();
			
			// get response code
			responseCode = conn.getResponseCode();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String response = "";
			String line = null;
			while ((line = reader.readLine()) != null){
				response = response + line;
			}
			
			System.err.println("C2DM - " + response);
		}catch (Exception e) {
			System.err.println("C2DM - Error sending message to google server.");
			e.printStackTrace();
		}
		return responseCode;
	}
	
	private static class CustomizedHostnameVerifier implements HostnameVerifier {
		 public boolean verify(String hostname, SSLSession session) {
			 return true;
		 }
	}
}
