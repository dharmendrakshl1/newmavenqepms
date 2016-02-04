package com.qepms.business.service;


public interface SendingMailService {
	
	public void sendMail(String to,String CC,String subject,String content);

}
