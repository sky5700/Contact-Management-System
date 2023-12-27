package com.contactmanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.contactmanager.helper.RandomOtpGenerator;

@Service
public class EmailVerification {
	

	@Autowired
	private JavaMailSender mailSender;
	
	//make sure you are handling exception here later
	public Integer sendMail(String toMail) {
		
		 RandomOtpGenerator generateOtp = new RandomOtpGenerator();
		 Integer otp = generateOtp.generateOtp();
		
		String subject = ">> Smart Contact Manager Email Verification code";
		
		String body = "Dear Customer, Your one time password is " + otp;
		
		
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("akash.chakrabortty1398@gmail.com");
		message.setTo(toMail);
		message.setText(body);
		message.setSubject(subject);
		
		this.mailSender.send(message);//sending mail
		
		return otp;
				
	}
}
