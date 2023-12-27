package com.contactmanager.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contactmanager.entities.Contact;
import com.contactmanager.entities.User;


public interface ContactRepo extends JpaRepository<Contact, Integer> {

	@Query("from Contact as c where c.user.userId=:userId")
	public Page<Contact> findContactsOfUserByUserId(@Param("userId") Integer userId, Pageable pageable);
	
	
	public List<Contact> findByNameContainingIgnoreCaseAndUser(String keywordname, User user);

}
