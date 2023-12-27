package com.contactmanager.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "user_details")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userid_seq")
	@SequenceGenerator(name = "userid_seq", sequenceName = "userid_seq", allocationSize = 1)
	private int userId;
	
	@NotBlank(message = "Name field is required !!")
	@Size(min = 3, max = 20, message = "min 2 and max 20 characters are allowed !!")
	private String name;
	
	//already verifying through email verification, hibernate validation is not required
	private String email;
	
	@NotBlank(message = "Password should not be blank !!")
	private String password;
	
	@NotBlank(message = "Please enter mobile number !!")
	@Size(min = 10, max = 12, message = "Please enter a valid mobile number !!")
	private String phone;
	
	private String role;
	
	private String imageurl;
	
	private boolean  isEnable;
	
	@Column(name = "description")
	@NotBlank(message = "Please provide something about yourself !!")
	private String about;
	
	@OneToMany(mappedBy = "user" ,cascade = CascadeType.ALL)
	private List<Contact> contacts = new ArrayList<>();
	

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}


	

	public User(int userId, String name, String email, String password, String phone, String role, String imageurl, boolean isEnable,
			String about, List<Contact> contacts) {
		super();
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.role = role;
		this.imageurl = imageurl;
		this.isEnable = isEnable;
		this.about = about;
		this.contacts = contacts;
	}




	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}




	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", email=" + email + ", password=" + password + ", phone="
				+ phone + ", role=" + role + ", imageurl=" + imageurl + ", isEnable=" + isEnable + ", about=" + about
				+ ", contacts=" + contacts + "]";
	}


	
	

}
