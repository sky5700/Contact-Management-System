package com.contactmanager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.contactmanager.entities.Contact;
import com.contactmanager.entities.User;
import com.contactmanager.repository.ContactRepo;

@Service
public class ContactServices {

	@Autowired
	ContactRepo contactRepo;

	public Page<Contact> findContactsOfUser(Integer userId, Pageable pageable) {

		Page<Contact> contacts = this.contactRepo.findContactsOfUserByUserId(userId, pageable);
		return contacts;

	}

	public Contact findContactProfileDetails(Integer conId) {
		Optional<Contact> optionalcontact = this.contactRepo.findById(conId);
		Contact contact = optionalcontact.get();
		return contact;

	}

	public void deleteUserContact(Contact contact) {

		this.contactRepo.delete(contact);
	}

	public Contact updateUserContact(Contact contact) {

		Contact savedContact = this.contactRepo.save(contact);
		return savedContact;
	}

	public List<Contact> findSearchContactResult(String searchquery, User user) {

		List<Contact> SearchContactResult = this.contactRepo.findByNameContainingIgnoreCaseAndUser(searchquery, user);
		

		return SearchContactResult;

	}

}
