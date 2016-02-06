package com;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name = "layout", eager = true)
public class LayoutBean implements Serializable {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private Date currentDate;

    @PostConstruct
    public void init() {
	setCurrentDate(new Date());
    }

    public Integer getCurrentDateYear() {
	Calendar cal = Calendar.getInstance();
	cal.setTime(currentDate);
	return cal.get(Calendar.YEAR);
    }

    public Date getCurrentDate() {
	return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
	this.currentDate = currentDate;
    }
}