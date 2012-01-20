package com.coctelmental.server.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailHandler {

	// SENDER ACCOUNT DATA
	private static final String USER = "crowdtransport@gmail.com";
	private static final String PASSWORD = "1886project1886";
	private static final String SENDER= "crowdtransport@gmail.com";

	// SERVER CONFIG
	private static final String HOST="smtp.gmail.com";
	private static final String PORT="587";
	private static final String AUTH = "true";
	private static final String STARTTLS = "true";

	private static final String SUBJECT="Crowdtransport - Confirmación de registro";

	
	public static void sendRegitrationMail(String userName, String userEmail) {

	    Properties props = new Properties();
	    props.setProperty("mail.smtp.host", HOST);
	    props.setProperty("mail.smtp.starttls.enable", STARTTLS);
	    props.setProperty("mail.smtp.port", PORT);
	    props.setProperty("mail.smtp.user", USER);
	    props.setProperty("mail.smtp.auth", AUTH);
	
	    try {
	    	// get session
	        Session session = Session.getDefaultInstance(props);
	        session.setDebug(false);
	    	
	        MimeMessage message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(SENDER));
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
	        
	        // build message
	        StringBuilder msg = new StringBuilder();
	        msg.append("Hola " + userName + ", \n");
	        msg.append("\nEl proceso de registro se ha completado correctamente.");
	        msg.append(" Ya puede autenticarse en la aplicación introduciendo los datos que proporcionó en el proceso de registro.\n");
	        msg.append("\nSi tiene algún problema con la aplicación o quiere sugerir alguna mejora, puede enviar un email a " + USER + ".\n");
	        msg.append("\nGracias por usar Crowdtransport!");
	        
	        message.setSubject(SUBJECT);
	        // attach message
	        message.setText(msg.toString());
	        
	        // send message
	        Transport transport = session.getTransport("smtp");
	        transport.connect(HOST, USER, PASSWORD);
	        transport.sendMessage(message, message.getAllRecipients());
	        transport.close();
	            
	    }catch (Exception e) {
	    	System.err.println("Error sending registration email");
	        e.printStackTrace();
	    }
	}

}