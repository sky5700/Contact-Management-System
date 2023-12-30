package com.contactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactmanager.helper.Message;
import com.contactmanager.services.EmailVerification;
import com.contactmanager.services.UserServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class EmailVerificationController {
	
	
	@Autowired
	EmailVerification verify;
	
	@Autowired
	UserServices userServices;

	@GetMapping("/check-email")
	public String checkMailId(Model model) {
		
		model.addAttribute("title", "Check Mail - User Contact Manager");
		return "checkmailPage";
	}
	
	
	@PostMapping("/process-email")
	public String processEmail(@RequestParam("email") String email, Model model, HttpSession session) {
		
		boolean ifUserAlreadyExist = this.userServices.checkIfUserAlreadyExist(email);
		
		if (ifUserAlreadyExist) {
			
			session.setAttribute("message", 
		              new Message(" User is already registered,"
		              		+ "Please enter a new mail id !!", "alert-danger"));
			
			return "redirect:/check-email";
		}
		else {
			//means user is not present in DB
			model.addAttribute("title", "Sign up - User Contact Manager");
			model.addAttribute("email", email);
			return "verifymailPage";
		}
		
	}
	
	@PostMapping("/generate-otp")
	public String verifyEmailWithOtp(@RequestParam("email") String email, Model model, HttpSession session){
		
		Integer otp = this.verify.sendMail(email);
		
		if(otp == -1) {
			model.addAttribute("title", "Checklater - User Contact Manager");
        	session.setAttribute("message", 
        			         new Message("Something went wrong, Please check after sometime !!", "alert-danger"));
        	return "redirect:/check-email"; 
		}
		
		model.addAttribute("otp", otp);
		model.addAttribute("email", email);
		model.addAttribute("title", "Verify Otp - User Contact Manager");
		return "verifyOtpPage";
	}
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("digit1") int digit1,
	                        @RequestParam("digit2") int digit2,
	                        @RequestParam("digit3") int digit3,
	                        @RequestParam("digit4") int digit4,
	                        @RequestParam("email") String email,
	                        @RequestParam("otp") Integer originalOtp,
	                        Model model, HttpSession session) {

	    // Concatenate digits to form the entered OTP
	    int enteredOtp = digit1 * 1000 + digit2 * 100 + digit3 * 10 + digit4;

	  
	    if(enteredOtp == originalOtp) {
	    	System.out.println("||||||||||||||||||||||||||||||||||||||||||||||");
			System.out.println("printing email through http session " + email + "otp is " + originalOtp);
	    	session.setAttribute("email", email);
	       session.setAttribute("message", 
	    		              new Message(" Your Mail id is successfully verified,"
	    		              		+ "enter below data to create your profile!!", "alert-success"));
	       return "redirect:/signup";
	    }
	    
	    else {
	    	model.addAttribute("email", email);
	    	session.setAttribute("message", 
		              new Message(" Invalid Otp,"
		              		+ "Please verify your mail id again !!", "alert-danger"));
	    	
            return  "verifymailPage";
			
		}
	    
	}

}
