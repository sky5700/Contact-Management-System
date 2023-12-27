package com.contactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactmanager.entities.User;
import com.contactmanager.helper.Message;
import com.contactmanager.services.UserServices;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserSettingController {
	
	@Autowired
	UserServices userServices;
	
	
	              //user's setting
	@GetMapping("/{userId}/setttings")
	public String userSettingDetails(@PathVariable("userId") Integer userId, Model model) {
		
		//fetching user from userId
		User user = this.userServices.findUserByUserId(userId);
		
		model.addAttribute("user", user);
		model.addAttribute("title", "Settings - User Contact Manager");
		return "normaluser/settingsPage";
		 
	}
	
	

	              //processing new password
	@PostMapping("/{userId}/processnewpassword")
	public String processingchangePassword (@PathVariable("userId") Integer userId, 
			                                @RequestParam("oldPassword") String oldPassword,
			                                @RequestParam("newPassword") String newPassword, Model model,
			                                HttpSession session) {
		
		//fetching user from userId
		User user = this.userServices.findUserByUserId(userId);
		
		try {
			
			if(user.getPassword().equals(oldPassword)) {
				
				user.setPassword(newPassword);// Setting new password
				
				this.userServices.saveUserData(user);// saving user data with new password
				
				session.setAttribute("message",
	   					new Message("Password updated successfully !!", "alert-success"));
				return "redirect:/user/" + user.getUserId()  +"/profile";
			}
			
			else if (!user.getPassword().equals(oldPassword)) {
				session.setAttribute("message",
	   					new Message("Current Password is wrong, please enter the correct password !!", "alert-danger"));
				return "redirect:/user/" + user.getUserId()  +"/setttings";
			} 
			
		}
		
		catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message",
   					new Message("Something went wrong , please try after some time", "alert-danger"));
			return "redirect:/user/" + user.getUserId()  +"/setttings";
			
		}
		
		session.setAttribute("message",
					new Message("Something went wrong , please change your password later", "alert-danger"));

		return "redirect:/user/" + userId  +"/setttings";
		
	}
	
	
	
                  //user details update 
	@PostMapping("/{userId}/updateuser/settings")
	public String updateUserData(@PathVariable("userId") Integer userId, Model model) {
		
		//fetching user from userId
		User user = this.userServices.findUserByUserId(userId);
		
		model.addAttribute("user", user);
		model.addAttribute("title", "Settings - User Contact Manager");
		
		return "normaluser/updateuserPage";
	}
	
	
	
	
	@PostMapping("/{userId}/process-updateddata")
	public String processUpdatedUserData(@PathVariable("userId") Integer userId, 
			                             @RequestParam("name") String name,  
		                                 @RequestParam("about") String about,
		                                 HttpSession session) {
		
		User user = this.userServices.findUserByUserId(userId); //fetching user
		
		user.setName(name);   //set new name
		user.setAbout(about); //set new about
		
		this.userServices.saveUserData(user);//updated data save in DB
		
		session.setAttribute("message",
					new Message("Your data updated successfully", "alert-success"));
		return "redirect:/user/" + user.getUserId() + "/profile";
	}

}
