package com.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ProjectMembers")
public class ProjectMember {

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

	// project.id
	@Column(name = "project")
	private Integer project;

	// user.id
	@Column(name = "userId")
	private Integer userId;

	// user.fullname
	@Column(name = "fullname")
	private String fullname;

	@Override
	public int hashCode() {
		return getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProjectMember) {
			ProjectMember pm = (ProjectMember) obj;
			if(pm.getId() == getId()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("id: %d; isActive: %b; createdOn: %s; "
				+ "editedOn: %s; project: %d; userId: %d; fullname: %s;",
				getId(), getIsActive(), getCreatedOn(), getEditedOn(),
				getProject(), getUserId(), getFullname());
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

	public Integer getProject() {
		return project;
	}

	public void setProject(Integer project) {
		this.project = project;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
}