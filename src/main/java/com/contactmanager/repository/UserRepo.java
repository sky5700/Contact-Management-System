package com.contactmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contactmanager.entities.User;

public interface UserRepo  extends JpaRepository<User, Integer>{

	@Query("select u from User u where u.email =:email ")
	public User findUserByEmail(@Param("email") String email);

	@Query("select u from User u where u.userId =:userId ")
	public User findByUserId(@Param("userId")Integer userId);
	

}
