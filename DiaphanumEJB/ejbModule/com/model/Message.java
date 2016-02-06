package com.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Messages")
public class Message {

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

    @Column(name = "createdBy")
    private Integer createdBy;

    @Column(name = "userFullname")
    @Size(max = 100)
    private String userFullname;

    @Column(name = "userEmail")
    @Size(max = 100)
    private String userEmail;

    @Column(name = "content")
    @Size(max = 100)
    private String content;

    @Column(name = "replyBy")
    private Integer replyBy;

    @Column(name = "reply")
    @Size(max = 100)
    private String reply;

    @Override
    public int hashCode() {
	return getId();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof Message) {
	    Message m = (Message) obj;
	    return (m.getContent().equals(getContent()) && (m.getCreatedBy()
		    .equals(getCreatedBy())));
	}
	return false;
    }

    @Override
    public String toString() {
	return String.format(
		"id: %d; isActive: %b; createdOn: %s; editedOn: %s; "
			+ "createdBy: %d; userFullname: %s; userEmail: %s;"
			+ "content: %s; replyBy: %d; reply: %s;",
		getId(), getIsActive(), getCreatedOn(), getEditedOn(),
		getCreatedBy(), getUserFullname(), getUserEmail(),
		getContent(), getReplyBy(), getReply());
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

    public String getUserFullname() {
	return userFullname;
    }

    public void setUserFullname(String userFullname) {
	this.userFullname = userFullname;
    }

    public String getUserEmail() {
	return userEmail;
    }

    public void setUserEmail(String userEmail) {
	this.userEmail = userEmail;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public Integer getReplyBy() {
	return replyBy;
    }

    public void setReplyBy(Integer responseBy) {
	this.replyBy = responseBy;
    }

    public String getReply() {
	return reply;
    }

    public void setReply(String response) {
	this.reply = response;
    }

}