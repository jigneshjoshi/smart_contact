package com.smart_contacts.halper;

import java.util.Random;

import org.springframework.stereotype.Service;
@Service
public class Generate_method {
	
	 public static int generateOTP() {
	        // Generate random 4-digit OTP
	        Random random = new Random();
	        return 1000 + random.nextInt(9000);
	    }

}
