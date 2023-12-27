package com.contactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactmanager.entities.User;
import com.contactmanager.helper.Message;
import com.contactmanager.services.SmsVerification;
import com.contactmanager.services.UserServices;

import jakarta.servlet.http.HttpSession;


@Controller
public class SmsVerificationController {
	
	@Autowired
	UserServices userServices;
	
	@Autowired
	SmsVerification smsVerification;

             	//verifying user account with email id 
	@GetMapping("/verify-account")
	public String AccountVerification(Model model, HttpSession session) {

        model.addAttribute("title", "Account verification -  Smart Contact Manager");
		return "accountVerificationPage";

	}
	
	

	
	            //processing verification and sending otp
	@PostMapping("/process-number")
	public String mobileNumberVerification(@RequestParam("email") String email, Model model,
			                               HttpSession session) {
		
	try {
	    User user = this.userServices.getUserData(email);
		
		// if user is null means entered mail id is not registered
		if(user == null) {
			session.setAttribute("message", 
					new Message("Entered mail id is not registered with us !!", "alert-danger"));
			return "redirect:/verify-account";

		}
		
		//if user is registered,  Otp will be send to  registered mobile number 
		else if(user.getEmail().equals(email)) {
			
			//otp send 
			Integer otp = this.smsVerification.sendSms(user.getPhone());
			
			if(otp == null)//Means , Exception raised while sending otp
				throw new Exception();
			
			model.addAttribute("email", user.getEmail());
			model.addAttribute("otp", otp);//change it 
			model.addAttribute("title", "Account verification -  Smart Contact Manager");
			session.setAttribute("message",
					new Message("Your mail id is verified !!" + "\n" 
							+ "One Otp is send to your registered mobile number", "alert-success"));
			
			return "mobileOtpVerification";
		}
		
		return ""; //Never some in this line, But it is not correct way to write the code, modify it later
		
	} 
	catch (Exception e) {
		
		model.addAttribute("title", "Error -  Smart Contact Manager");
		session.setAttribute("message", 
				new Message("Something went wrong, Please try again later !!", "alert-danger"));
		
		return "redirect:/verify-account";
	}
		
		
  }
	
	
	
	
	             //verifying  otp and showing change password option
	@PostMapping("/verify-mobileotp")
	public String  otpVerification(@RequestParam("email") String email,
                                   @RequestParam("otp") int originalOtp,
                                   @RequestParam("enteredOtp") int enteredOtp,
                                   Model model , HttpSession session) {
		try {

			if(enteredOtp == originalOtp) {
				
				session.setAttribute("message",
						new Message("We have verified your account"
								+ "Please update your password", "alert-success"));
				
				model.addAttribute("email", email);
				model.addAttribute("title", "Update Password - Smart Contact Manager");
				return "updatePasswordPage";
			}
			else if(enteredOtp != originalOtp) {
	
				session.setAttribute("message", 
						new Message("Incorrect otp, please verify your account again !!", "alert-danger"));
				
				return "redirect:/verify-account";
			}
		} 
		catch (Exception e) {
			
			session.setAttribute("message", 
					new Message("Something went wrong, please try after some time !!", "alert-danger"));
			
			return "redirect:/verify-account";
		}
		
		
		session.setAttribute("message", 
				new Message("Server is busy, please try after some time !!", "alert-danger"));
		return "normaluser/unauthorizedContactpage";
		
	}
	
	
	
	             //Updating new password of user
	@PostMapping("/update-password")
	public String updateNewPassword(@RequestParam("email") String email, 
			                        @RequestParam("newPassword") String newPassword, HttpSession session) {
		
		User user = this.userServices.getUserData(email);//fetching user data
		
		user.setPassword(newPassword);//updating new password
		
		this.userServices.saveUserData(user);// updating user with new password
		session.setAttribute("message",
				new Message("Your password is updated successfully"
						+ "Please login to your account !!", "alert-success"));
		
		System.out.println("Password updated successfully");//printing msg in console
		return "redirect:/user/login";
		
	}

}
