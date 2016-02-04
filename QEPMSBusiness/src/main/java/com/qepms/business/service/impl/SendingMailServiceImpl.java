package com.qepms.business.service.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.qepms.business.service.SendingMailService;
import com.qepms.data.employee.Resume;
import com.qepms.infra.ldap.ConfigValues;

public class SendingMailServiceImpl implements SendingMailService
{
	final String password = ConfigValues.getConfigValue("MAIL_SMTP_PASSWORD");
	final String userName = ConfigValues.getConfigValue("MAIL_SMTP_USER");
	final String mailFrom = ConfigValues.getConfigValue("MAIL_FROM");
	final String mailHost = ConfigValues.getConfigValue("MAIL_SMTP_SERVER");
	final String mailPort = ConfigValues.getConfigValue("MAIL_SMTP_PORT");
	
	public void sendMail(String to,String cc,String subject,String content) 
	{
		//Setting mail properties
		
	      	Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "false");
			props.put("mail.smtp.host", mailHost);
			props.put("mail.smtp.port", mailPort);
						
			Session session = Session.getInstance(props,
					  new javax.mail.Authenticator() {
						protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(userName, password);
						}
					  });
			
	      try{
	    	  
	    	  
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: QPMS application
	         message.setFrom(new InternetAddress(mailFrom));

	         // Set To : manager
	         message.setRecipients(Message.RecipientType.TO,
	 				InternetAddress.parse(to));	
	         // Set CC : employee
	         message.addRecipients(Message.RecipientType.CC,
						InternetAddress.parse(cc));

	         // Set Subject: header field
	         
	         message.setSubject(subject);

	         // Now set the actual message

	         message.setContent(content, "text/html");

	         // Send message
	         System.out.println("ready to send mail");
	         Transport.send(message);
	         System.out.println("mail sent");
	      }
	      catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}

}
	
	

