package com.contactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactmanager.entities.User;
import com.contactmanager.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.contactmanager.services.UserServices;

@Controller
public class HomeController {

	@Autowired
	private UserServices service;

	//Home page of our web-site
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - User Contact Manager");
		return "homepage";
	}

	//Description of the web-site
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - User Contact Manager");
		return "aboutpage";
	}
	
	//Signup-page
	@GetMapping("/signup")
	public String signUp(Model model, HttpSession session) {
		
		String email = (String) session.getAttribute("email");
		
		if(email != null) {
			model.addAttribute("title", "Signup - User Contact Manager");
			model.addAttribute("email", email);
			model.addAttribute("user", new User());
			return "signup";
		}
		model.addAttribute("title", "Error - User Contact Manager");
		return "normaluser/unauthorizedContactpage.html";
	}
	
	//Processing the data coming from signup-page
	@PostMapping("/processRegisterData")
	public String registeredUser(Model model,@Valid @ModelAttribute("user") User user, BindingResult bindResult,
			                    @RequestParam(value = "checkbox",defaultValue = "false") boolean agreement,
			                    HttpSession session) {
	  try {
			
		 if(!agreement)
			throw new Exception("You have not agreed Terms and Condition");
		 
		 if(bindResult.hasErrors()) {
			 System.out.println("Eoors present in : " + bindResult);
			 model.addAttribute("user", user);
			 model.addAttribute("email", user.getEmail());
			 return "signup";
		 }
		 
		 //setting as a Normal User
		 user.setEnable(true);
		 user.setRole("Role-User");
		
		 //saving in DB
		 User savedUserData = this.service.saveUserData(user);
		 session.setAttribute("message", new Message("Hi " + savedUserData.getName() + 
				 ", You have Successfully Registered !!" + "\n" + "Please login to add your contacts !!",
				 "alert-success"));
		 model.addAttribute("title", "SuccessRegistered - User Contact Manager");
		 return "signup";
		
		}
	 catch (Exception e) {
		    System.out.println("exception occour inside homeController");
			e.printStackTrace();
			model.addAttribute("user", user);
			model.addAttribute("email", user.getEmail());
			session.setAttribute("message", new Message("Something went wrong !!", "alert-danger"));
			model.addAttribute("title", "FailedRegistered - User Contact Manager");
			return "signup";
		}
		
	}

}
