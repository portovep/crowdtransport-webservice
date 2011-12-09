package com.coctelmental.server.c2dm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class C2DMAuthentication {

	private static final String CLIENT_LOGIN_URI = "https://www.google.com/accounts/ClientLogin";
	
	private static final String ACCOUNT_TYPE = "GOOGLE";
	private static final String SERVICE_NAME = "ac2dm";
	
	public static final String EMAIL = "project1886@gmail.com";
	public static final String PASSWD = "1886project1886";
	
	private static String auth_token;
	
	public static String getNewAuthToken(String email, String password) {

		// build authentication request
		StringBuilder builder = new StringBuilder();
		builder.append("Email=").append(email);
		builder.append("&Passwd=").append(password);
		builder.append("&accountType=").append(ACCOUNT_TYPE);
		builder.append("&source=").append("projet1886-0.5");
		builder.append("&service=").append(SERVICE_NAME);
		
		byte[] requestData = builder.toString().getBytes();
		
		String token = null;
		
		try{		
			URL url = new URL(CLIENT_LOGIN_URI);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			// setup connection properties
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Content-Length", Integer.toString(requestData.length));
			con.setRequestMethod("POST");
	
			// send request
			OutputStream output = con.getOutputStream();
			output.write(requestData);
			output.close();
	
			// parse response
			String line = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while((line = reader.readLine()) != null) {
				if (line.startsWith("Auth="))
					token = line.substring(5);
			}
			reader.close();
		}catch (Exception e) {
			System.err.println("C2DM - Error getting response data.");
		}
		// store auth_token
		if (token != null)
			auth_token = token;
		
		return token;
	}
	
	public static String getAuthToken(){
		return auth_token;
	}
	
}
