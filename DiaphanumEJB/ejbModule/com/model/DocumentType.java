package com.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DocumentTypes")
public class DocumentType {

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

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Override
	public int hashCode() {
		return getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DocumentType) {
			DocumentType dt = (DocumentType) obj;
			return dt.getId().equals(getId());
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("id: %d; isActive: %b; createdOn: %s; "
				+ "editedOn: %s; name: %s; description: %s;", getId(), getIsActive(),
				getCreatedOn(), getEditedOn(), getName(), getDescription());
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}