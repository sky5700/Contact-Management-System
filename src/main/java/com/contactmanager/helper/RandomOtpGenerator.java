package com.contactmanager.helper;

import java.util.Random;

public class RandomOtpGenerator {

	public Integer generateOtp() {

		Random random = new Random();// Create an instance of the Random class
		int random6DigitNumber = 1000 + random.nextInt(9000); // Generate a random 4-digit integer between 
		
		return random6DigitNumber;
	}

}
