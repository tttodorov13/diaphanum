package com.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Documents")
public class Document {

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

    // user created the document
    @Column(name = "createdBy")
    private Integer createdBy;

    // document name
    @Column(name = "title")
    private String title;

    // document date
    @Column(name = "documentDate")
    private Date documentDate;

    // name of To/From institution
    @Column(name = "institution")
    private String institution;

    // user signed the document
    @Column(name = "signedBy")
    private String signedBy;

    // file name
    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private Integer type;

    @Override
    public int hashCode() {
	return getId();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof Document) {
	    Document d = (Document) obj;
	    return (d.getName().equals(getName()) && (d.getCreatedBy()
		    .equals(getCreatedBy())));
	}
	return false;
    }

    @Override
    public String toString() {
	return String.format("id: %d; isActive: %b; createdOn: %s; "
		+ "editedOn: %s; createdBy: %d; title: %s; documentDate: %s; "
		+ "institution: %s; signedBy: %s; name: %s; type: %d", getId(),
		getIsActive(), getCreatedOn(), getEditedOn(), getCreatedBy(),
		getTitle(), getDocumentDate(), getInstitution(), getSignedBy(),
		getName(), getType());
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

    public Integer getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(Integer createBy) {
	this.createdBy = createBy;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Date getDocumentDate() {
	return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
	this.documentDate = documentDate;
    }

    public String getInstitution() {
	return institution;
    }

    public void setInstitution(String institution) {
	this.institution = institution;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public String getSignedBy() {
	return signedBy;
    }

    public void setSignedBy(String signedBy) {
	this.signedBy = signedBy;
    }
}