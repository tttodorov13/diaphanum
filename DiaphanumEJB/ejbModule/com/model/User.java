package com.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User {

	// unique identifier
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	// mark whether record in db is in use
	@Column(name = "isActive")
	private Boolean isActive;

	// when record is created in db
	@Column(name = "createdOn")
	private Date createdOn;

	// when record is last updated in db
	@Column(name = "editedOn")
	private Date editedOn;

	// system username
	@Column(name = "username")
	private String username;

	// system password
	@Column(name = "password")
	private String password;

	// system password
	@Column(name = "fullname")
	private String fullname;

	// system email
	@Column(name = "email")
	private String email;

	// system photo
	@Column(name = "phone")
	private String phone;

	// system photo
	@Column(name = "photo")
	private String photo;

	// mark whether user is admin
	@Column(name = "isAdmin")
	private Boolean isAdmin;

	@Override
	public int hashCode() {
		return getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User u = (User) obj;
			return u.getUsername().equals(getUsername());
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("id: %d; isActive: %b; createdOn: %s; "
				+ "editedOn: %s; username: %s; password: %s; "
				+ "fullname: %s; email: %s phone: %s; isAdmin: %b;", getId(),
				getIsActive(), getCreatedOn(), getEditedOn(), getUsername(),
				getPassword(), getFullname(), getEmail(), getPhone(),
				getIsAdmin());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsActive() {
		if (isActive == null) {
			return false;
		}
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		if (isActive == null) {
			isActive = false;
		}
		this.isActive = isActive;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createOn) {
		this.createdOn = createOn;
	}

	public Date getEditedOn() {
		return editedOn;
	}

	public void setEditedOn(Date editedOn) {
		this.editedOn = editedOn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Boolean getIsAdmin() {
		if (isAdmin == null) {
			return false;
		}
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		if (isAdmin == null) {
			isAdmin = false;
		}
		this.isAdmin = isAdmin;
	}
}