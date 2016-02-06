package com.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Projects")
public class Project {

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

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    @Size(max = 1200)
    private String description;

    @Column(name = "purpose")
    @Size(max = 1200)
    private String purpose;

    @Column(name = "tasks")
    @Size(max = 1200)
    private String tasks;

    @Column(name = "schedule")
    @Size(max = 1200)
    private String schedule;

    @Column(name = "resourse")
    @Size(max = 1200)
    private String resourse;

    @Column(name = "budjet")
    private BigDecimal budjet;

    @Column(name = "budjetText")
    @Size(max = 1200)
    private String budjetText;

    @Column(name = "partners")
    @Size(max = 1200)
    private String partners;

    @Column(name = "status")
    private Integer status;

    @Column(name = "targetGroup")
    @Size(max = 1200)
    private String targetGroup;

    @Column(name = "commend")
    @Size(max = 1200)
    private String commend;

    @Override
    public int hashCode() {
	return getId();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof Project) {
	    Project p = (Project) obj;
	    return (p.getName().equals(getName()) && (p.getCreatedBy()
		    .equals(getCreatedBy())));
	}
	return false;
    }

    @Override
    public String toString() {
	return String
		.format("id: %d; isActive: %b; createdOn: %s; editedOn: %s; "
			+ "createdBy: %d; name: %s; description: %s; purpose: %s; "
			+ "tasks: %s; schedule: %s; resourse: %s; budjet: %0.2f; "
			+ "budjetText: %s; partners: %s; status: %d; targetGroup: %s; commend: %s;",
			getId(), getIsActive(), getCreatedOn(), getEditedOn(),
			getCreatedBy(), getName(), getDescription(),
			getPurpose(), getTasks(), getSchedule(), getResourse(),
			new DecimalFormat("#0.##").format(getBudjet()),
			getBudjetText(), getPartners(), getStatus(),
			getTargetGroup(), getCommend());
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

    public String getPurpose() {
	return purpose;
    }

    public void setPurpose(String purpose) {
	this.purpose = purpose;
    }

    public String getTasks() {
	return tasks;
    }

    public void setTasks(String tasks) {
	this.tasks = tasks;
    }

    public String getSchedule() {
	return schedule;
    }

    public void setSchedule(String schedule) {
	this.schedule = schedule;
    }

    public String getResourse() {
	return resourse;
    }

    public void setResourse(String resourse) {
	this.resourse = resourse;
    }

    public BigDecimal getBudjet() {
	return budjet;
    }

    public void setBudjet(BigDecimal budjet) {
	this.budjet = budjet;
    }

    public String getBudjetText() {
	return budjetText;
    }

    public void setBudjetText(String budjetText) {
	this.budjetText = budjetText;
    }

    public String getPartners() {
	return partners;
    }

    public void setPartners(String partners) {
	this.partners = partners;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getTargetGroup() {
	return targetGroup;
    }

    public void setTargetGroup(String targetGroup) {
	this.targetGroup = targetGroup;
    }

    public String getCommend() {
	return commend;
    }

    public void setCommend(String commend) {
	this.commend = commend;
    }
}