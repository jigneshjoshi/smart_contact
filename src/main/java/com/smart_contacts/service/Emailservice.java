package com.smart_contacts.service;


import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class Emailservice {
	
	public void sendemail(String subject,String to,String message) {
		boolean f= false;
		String from ="joshijignesh970@gmail.com";
		String host ="smtp.gmail.com";
		//get the system properties
	  Properties properties = System.getProperties();
	  
	  System.out.println( "properties"+properties );
	  properties.put("mail.smtp.host", host);
	  properties.put("mail.smtp.port", "465");
      properties.put("mail.smtp.ssl.enable", "true");
	  properties.put("mail.smtp.auth", "true");
	  
	  
	  //step 1 get the seesion object 
	  Session session =Session.getInstance(properties, new Authenticator(){

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			// TODO Auto-generated method stub
			return  new PasswordAuthentication("joshijignesh970@gmail.com", "zfpo cost mryn gnpo");
		}
		  
	});
	 session.setDebug(true);
	   //step 2 compose the message 
	 MimeMessage m= new MimeMessage(session);
	 try {
		 //from mail
	m.setFrom(from);
	//add recipent
	m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	//add subject 
	m.setSubject(subject);
	m.setText(message);
	//send
	//step 3 send the message using transport class 
	Transport.send(m);
	System.out.print("sent seccessfully");
		 
		
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	
	}
	
	}
	


