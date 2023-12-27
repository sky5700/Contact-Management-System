package com.contactmanager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "contact_details")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conid_seq")
	@SequenceGenerator(name = "conid_seq", sequenceName = "conid_seq", allocationSize = 1)
	private int cId;

	@NotBlank(message = "Name field is required !!")
	@Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters !!")
	private String name;

	private String Nickname;
	
	@NotBlank
	@Email(message="Please provide a valid email address !!")
	@Column(unique = true)
	private String email;

	private String work;

	@NotBlank(message = "Please enter mobile number !!")
	@Size(min = 10, max = 12, message = "Please enter a valid mobile number !!")
	private String phone;

	private String image;

	@Size(max = 500, message = "Max 500 characters are allowed !!")
	private String about;

	@ManyToOne
	@JsonIgnore
	private User user;
	
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Contact(int cId, String name, String nickname, String email, String work, String phone, String image,
			String about, User user) {
		super();
		this.cId = cId;
		this.name = name;
		Nickname = nickname;
		this.email = email;
		this.work = work;
		this.phone = phone;
		this.image = image;
		this.about = about;
		this.user = user;
	}

	public int getcId() {
		return cId;
	}

	public void setcId(int cId) {
		this.cId = cId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return Nickname;
	}

	public void setNickname(String nickname) {
		Nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	

	
}
