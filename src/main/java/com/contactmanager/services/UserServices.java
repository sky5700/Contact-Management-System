package com.contactmanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactmanager.entities.User;
import com.contactmanager.helper.UserValidationStatus;
import com.contactmanager.repository.UserRepo;

@Service
public class UserServices {

	@Autowired
	UserRepo repo;

	// Saving user data in DB
	public User saveUserData(User user) {
		User savedUser = this.repo.save(user);
		return savedUser;

	}

	// checking If user is valid and sending the response as a String
	public UserValidationStatus checkIfUserValid(String email, String password) {

		try {
			User user = this.repo.findUserByEmail(email);

			// User doesn't exist, please create your account!!
			if (user == null)
				return UserValidationStatus.NO_USER; //"no-user";

			// Invalid password, Please check your password !!
			if (!user.getPassword().equals(password))
				return UserValidationStatus.WRONG_PASSWORD; //"wrong-password";

			// Both mail and password is valid
			else if (user.getEmail().equals(email) && user.getPassword().equals(password)) {

				return UserValidationStatus.SUCCESS; // "success";

			}
		}

		catch (Exception e) {
			System.out.println("exception occour inside userService");
			e.printStackTrace();
			return UserValidationStatus.EXCEPTION; //"exception";

		}

		return UserValidationStatus.UNKNOWN;  
	}

	// fetching & sending user data by email
	// 1.Please note I have checked in previous function if user is null
	public User getUserData(String email) {

		User user = this.repo.findUserByEmail(email);
		return user;

	}

	// fetching & sending user data by userId
	public User findUserByUserId(Integer userId) {

		User user = this.repo.findByUserId(userId);
		return user;

	}

	public boolean checkIfUserAlreadyExist(String email) {
		
		User user = this.repo.findUserByEmail(email);
		
		if(user!= null)
			return true;
		else
			return false;
		
	}

}
