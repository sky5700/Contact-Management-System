package com.contactmanager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contactmanager.entities.Contact;
import com.contactmanager.entities.User;
import com.contactmanager.helper.Message;
import com.contactmanager.helper.UserValidationStatus;
import com.contactmanager.services.ContactServices;
import com.contactmanager.services.UserServices;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserServices services;
	
	@Autowired
	ContactServices contactServices;

	
	
	                 //login page of user
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("title", "UserLogin - User Contact Manager");
		return "normaluser/loginuser";
	}

	

	
	                //Processing login data
	@PostMapping("/processlogin")
	public String processLoginData(@RequestParam("email") String email,
			                       @RequestParam("password") String password,
			                       HttpSession session, Model model) {
		
		UserValidationStatus userValidation = services.checkIfUserValid(email, password);
		
		
		
		String processPage;
		
		switch (userValidation) {
        case NO_USER :
        	model.addAttribute("title", "Notfound - User Contact Manager");
        	model.addAttribute("oldData", email);
        	session.setAttribute("message", 
        			         new Message("User doesn't exist, please create your account!!", "alert-danger"));
        	processPage = "normaluser/loginuser";
            break;

        case WRONG_PASSWORD : 
        	model.addAttribute("title", "Invalidpassword - User Contact Manager");
        	model.addAttribute("oldData", email);
        	session.setAttribute("message", 
        			         new Message("Invalid password, Please check your password !!", "alert-danger"));
        	processPage = "normaluser/loginuser";
            break;
            
        case EXCEPTION : 
        	model.addAttribute("title", "Checklater - User Contact Manager");
        	session.setAttribute("message", 
        			         new Message("Something went wrong, Please check after sometime !!", "alert-danger"));
        	processPage = "normaluser/loginuser";
            break;
            
		
		case SUCCESS :
			//fetching user details
			try {
				User user = services.getUserData(email);
				model.addAttribute("title","Successfullogin - User Contact Manager"); 
				model.addAttribute("user", user);
				
				processPage ="normaluser/userdashboard";
			}
			catch (Exception e) {
				System.out.println("exception occour inside userController");
				e.printStackTrace();
				model.addAttribute("title", "Checklater - User Contact Manager");
	        	session.setAttribute("message", 
	        			         new Message("Something went wrong, Please check after sometime !!", "alert-danger"));
	        	processPage = "normaluser/loginuser";
			}
			 
			break;
		 

        default:
        	model.addAttribute("title", "Checklater - User Contact Manager");
        	session.setAttribute("message", 
        			         new Message("Server is busy, Please check after few hours !!", "alert-danger"));
        	processPage = "normaluser/loginuser";
            break;
    }

    return processPage;
}

	


	                //user Home Page
	@GetMapping("/{userId}/home")
	public String userHome(@PathVariable("userId") Integer userId, Model model) {
		
		//fetching user data 
		User user = services.findUserByUserId(userId);
		
		model.addAttribute("title", "Home - User Contact Manager");
		model.addAttribute("user", user);
		
		return "normaluser/userdashboard";
	}
	
	

	
 	              //Adding new contact in user
    @GetMapping("/addContact/{userId}")
	public String addContact(@PathVariable("userId") Integer userId, Model model) {
		
    	User user = services.findUserByUserId(userId);
		model.addAttribute("title", "Addcontact - User Contact Manager");
		model.addAttribute("user", user);
		model.addAttribute("contact", new Contact());
		return "normaluser/addcontactpage";
	}

    
    
 
	    /* From here All contact related Handler Present */
    
                  //Processing new contact data
	@PostMapping("/{userId}/processcontact")
    public String processcontact(@PathVariable("userId") Integer userId ,
    		                     @Valid @ModelAttribute Contact contact,BindingResult bindResult,
    		                     HttpSession session, Model model, 
    		                     @RequestParam("prifilepic") MultipartFile image) {
		
		User user = services.findUserByUserId(userId);//fetching user data from id 
		
	try {
		 
		 if(bindResult.hasErrors()) {
			 System.out.println("Eoors present in : " + bindResult);
			 model.addAttribute("contact", contact);
			 model.addAttribute("user", user);
			 //return "redirect:/user/addContact/" + user.getUserId();
			 return "normaluser/addcontactpage";
		 }
		
	    
		contact.setUser(user); //setting the user to contact
		List<Contact> contacts = user.getContacts();//fetching contact variable  from user
		contacts.add(contact);//adding to the same user's contact
		
		 //processing and uploading file
		if(image.isEmpty()) {
			
			//if No contact selected (file is empty) set the default image name in contact
			contact.setImage("defaultprofilepic.png");
			System.out.println("No file choosed, default name set");//console msg
		}
		
		else {
			 //upload the file to folder and update the name to contact
			 contact.setImage(user.getUserId()+contact.getPhone().substring(7, 9)+image.getOriginalFilename());
			 
			 File file = new ClassPathResource("static/img").getFile();
			 Path contactProfilePath = Paths.get(file.getAbsolutePath()+
					 File.separator+user.getUserId()+contact.getPhone().substring(7, 9)+image.getOriginalFilename());//setting the path
			 
			 Files.copy(image.getInputStream(), contactProfilePath, 
					 StandardCopyOption.REPLACE_EXISTING);//copying image to our path
			 
			 System.out.println("File is saved in folder");//console msg
			 
		}
		
		
		//Saving contact in DB profilepic
		this.services.saveUserData(user);
		
		model.addAttribute("title", "Addcontact - User Contact Manager");
		model.addAttribute("user", user);
		session.setAttribute("message",
				new Message("Contact added successfully !!, add more..", "alert-success"));
		}

	catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong !!, Please try again later", "alert-danger"));
			
		}
	
	return "redirect:/user/addContact/" + user.getUserId();
    	
    }

	
	

	
	              //view all contacts of user
	@GetMapping("/viewContact/{userId}/{page}")
	public String showContacts(@PathVariable("userId") Integer userId, @PathVariable("page") Integer page, Model model) {
		
		User user = services.findUserByUserId(userId);
		model.addAttribute("title", "Viewcontacts - User Contact Manager");
		model.addAttribute("user", user);
		
		//using pageable
		Pageable pageable =  PageRequest.of(page, 5);//we want to see 5 contacts in single page
		//fetching contacts
		 Page<Contact> contacts = this.contactServices.findContactsOfUser(user.getUserId(), pageable);
		 
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentpage", page);
		model.addAttribute("totalpage", contacts.getTotalPages());
		
		
		return "normaluser/viewcontactspage";
	}

	
	
	
	              
	              //showing contact profile
	@GetMapping("/{userId}/contact/{conId}")
	public String showSpecificContact(@PathVariable("userId") Integer userId,
			                          @PathVariable("conId") Integer conId, Model model) {
		try {
			//fetching user details 
			User user = services.findUserByUserId(userId);
			
			//fetching contact info 
			Contact contact = contactServices.findContactProfileDetails(conId);
			
			model.addAttribute("user", user);
			if(contact.getUser().getUserId() == user.getUserId()) {
			  model.addAttribute("contact", contact); 
			  model.addAttribute("title", "Profilecontacts - User Contact Manager");
			  return "normaluser/Contactprofilespage";
			}
			
			else {
				model.addAttribute("title", "Unauthorizedview - User Contact Manager");
				return "normaluser/unauthorizedContactpage";
			}
			
			
		} catch (Exception e) {
			
			model.addAttribute("title", "Unauthorizedview - User Contact Manager");
			System.out.println(e.getLocalizedMessage());
			return "normaluser/unauthorizedContactpage";
		}
     }
	
	
	

	
	              //delete specific contact 
	@GetMapping("/{userId}/delete-con/{conId}")
	public String deleteUserContactDetails(@PathVariable("userId") Integer userId, HttpSession session,
			                        @PathVariable("conId") Integer conId, Model model) {
		try {

			User user = services.findUserByUserId(userId);
			Contact contact = this.contactServices.findContactProfileDetails(conId);
			contact.setUser(null);
			
			//delete that contact
			this.contactServices.deleteUserContact(contact);
			
			//delete existing old photo of contact when it's not default image
			if(!contact.getImage().equals("defaultprofilepic.png") ) {
			
				File savedFile = new ClassPathResource("static/img").getFile();
	    		File deleteFile = new File(savedFile, contact.getImage());
	    		deleteFile.delete();
			}
			
			session.setAttribute("message",
					new Message("Contact deleted successfully !!", "alert-success"));
			
			return "redirect:/user/viewContact/" + user.getUserId() +"/0"; 
			
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message",
			          new Message("Something went wrong !!, Contact is not deleted Please try again later", "alert-danger"));
		}
		
		// redirect to the view contact page
		return "redirect:/user/viewContact/" + userId +"/0"; 
	}

	

	
	
	              //update contact data
	@GetMapping("/{userId}/update-con/{conId}")
	public String updateUserContactDetails(@PathVariable("userId") Integer userId, HttpSession session,
                                           @PathVariable("conId") Integer conId, Model model) {
		
		User user = services.findUserByUserId(userId);
		Contact contact = this.contactServices.findContactProfileDetails(conId);
		
		model.addAttribute("user", user);
		model.addAttribute("contact", contact);
		model.addAttribute("profilepic", contact.getImage());
		model.addAttribute("title", "Updatecontact- Smart Contact Manager");
		
		return "normaluser/updatecontactpage";
	 }
	
	

	
	
	               //process updated contact data
    @PostMapping("/{userId}/processupdate-con/{conId}")
    public String processUpdatedContactDetails(@PathVariable("userId") Integer userId, HttpSession session,
                                               @PathVariable("conId") Integer conId, Model model,
                                               @Valid @ModelAttribute("contact") Contact contact, BindingResult bindResult,
                                               @RequestParam("prifilepic") MultipartFile image) {
    	System.out.println("just checking");
    	User user = services.findUserByUserId(userId);//fetching user data from id 
    	
    	//fetching old contact profile picture name 
    	Contact oldContactprofile = this.contactServices.findContactProfileDetails(conId);
    	String profilepic = oldContactprofile.getImage();
    	
    	try {
    		if(bindResult.hasErrors()) {
   			 System.out.println("Eoors present in : " + bindResult);
   			 model.addAttribute("contact", contact);
   			 model.addAttribute("profilepic", profilepic);
   			 model.addAttribute("user", user);
   			 //return "redirect:/user/addContact/" + user.getUserId();
   			 return "normaluser/updatecontactpage";
   		 }
			
    		
    		//delete existing old photo of contact when it's not default image
    		if(!profilepic.equals("defaultprofilepic.png") ) {
    		
    			File savedFile = new ClassPathResource("static/img").getFile();
        		File deleteFile = new File(savedFile, profilepic);
        		deleteFile.delete();
    		}
    		
    		
    		contact.setcId(conId);//setting cid bcoz not getting from updated form
    		contact.setUser(user); //setting the user to contact
    		

   		 //processing and uploading file
   		if(image.isEmpty()) {
   			
   			//if No image selected (file is empty) set the default image name in contact
   			contact.setImage("defaultprofilepic.png");
   			System.out.println("No file choosed in updated data , default name set");//console msg
   		}
   		
   		else {
   			
   			 //upload the file to folder and update the name to contact
   			 contact.setImage(user.getUserId()+contact.getPhone().substring(7, 9)+image.getOriginalFilename());
   			 
   			 File file = new ClassPathResource("static/img").getFile();
   			 Path contactProfilePath = Paths.get(file.getAbsolutePath()+
   					 File.separator+user.getUserId()+contact.getPhone().substring(7, 9)+image.getOriginalFilename());//setting the path
   			 
   			 Files.copy(image.getInputStream(), contactProfilePath, 
   					 StandardCopyOption.REPLACE_EXISTING);//copying image to our path
   			 
   			 System.out.println("File is saved in folder");//console msg
   			 
   		}
   	       //Updating  contact in DB profilepic
   			 this.contactServices.updateUserContact(contact);
   			
   			model.addAttribute("title", "Updatedcontact - User Contact Manager");
   			model.addAttribute("user", user);
   			session.setAttribute("message",
   					new Message("Contact updated successfully !!", "alert-success"));
			
		} 
    	catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message",
					          new Message("Something went wrong !!, Please try again later", "alert-danger"));
			
		}
	   
    	
	   return "redirect:/user/" + user.getUserId() + "/contact/" + conId;//redirect to view contact page
    }

 
	    /* up to this all contact related handler present */
    
    

    
    
                  //user profile page
    @GetMapping("/{userId}/profile")
    public String userProfile(@PathVariable("userId") Integer userId, Model model) {
    	
    	//fetching user details
    	User user = this.services.findUserByUserId(userId);
    	
    	model.addAttribute("user", user);
    	model.addAttribute("title", "Your Profile - Smart contact Manager");
    	return "normaluser/Userprofilepage";
    }
    
    
		
	
}
