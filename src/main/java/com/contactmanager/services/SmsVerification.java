package com.contactmanager.services;

import org.springframework.stereotype.Service;

import com.contactmanager.helper.RandomOtpGenerator;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsVerification {
	
	
	
	public Integer sendSms(String destinationNumber) {
		
		try {
			
			//generating otp
			RandomOtpGenerator otpGenerator = new RandomOtpGenerator();
			Integer otp = otpGenerator.generateOtp();
			
			//Sms description
			String message = "Smart contact Manager : "
					+ "Your mobile verification code is " + otp;

			//twilio config
			String twilio_sid = "AC11637736e24dca69c3ed59e6c5654fb5";
			String twilio_auth_token = "9b35fb6e98e32b4cf8e6b3d1f0a7cb26";
			Twilio.init(twilio_sid, twilio_auth_token);


			Message.creator(
			        new PhoneNumber("+91" + destinationNumber),  // Destination number (to)
			        new PhoneNumber("+19105651271"),  // Twilio phone number (from)
			        message
			).create();
			
			System.out.println("Sms send successfully");


			return otp;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to send sms");
			return null;
		}

	}

}
