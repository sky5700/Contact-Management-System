package com.contactmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanager.entities.Contact;
import com.contactmanager.entities.User;
import com.contactmanager.services.ContactServices;
import com.contactmanager.services.UserServices;

@RestController
public class SearchController {

	
	@Autowired
	UserServices userService;
	
	@Autowired
	ContactServices contactService;
	
	//search contact Handler
	@GetMapping("/contsearch/{userId}/{searchquery}")
	public ResponseEntity<?> searchContact(@PathVariable("userId") Integer userId, 
			                               @PathVariable("searchquery") String searchquery) {
		//System.out.println("checking search func");
		User user = this.userService.findUserByUserId(userId);
		List<Contact> contacts = this.contactService.findSearchContactResult(searchquery, user);
		System.out.println(contacts);
		return ResponseEntity.ok(contacts);
		
		
	}
}
