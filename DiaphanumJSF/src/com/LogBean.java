package com;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ttt
 */
@RequestScoped
@ManagedBean(name = "log", eager = true)
public class LogBean {

    private static Calendar cal = Calendar.getInstance();
    private static SimpleDateFormat logNameFormat = new SimpleDateFormat(
	    "yyyy-MM-dd");
    private static String logName = logNameFormat.format(cal.getTime());
    private static SimpleDateFormat logDateFormat = new SimpleDateFormat(
	    "yyyy-MM-dd HH:mm:ss.S");
    private static String logDate = logDateFormat.format(cal.getTime());
    private static HttpServletRequest request = null;
    private static String ip = "";

    @PostConstruct
    public void init() {
	try {
	    request = (HttpServletRequest) FacesContext.getCurrentInstance()
		    .getExternalContext().getRequest();
	    ip = request.getRemoteAddr();
	} catch (Exception e) {
	    System.err.println("ERROR LogBean.init: e: " + e.getMessage());
	}
    }

    public LogBean() {
	writeLog(null);
    }

    public LogBean(String msg) {
	writeLog(msg);
    }

    private static void writeLog(String msg) {
	try {
	    BufferedWriter writer = new BufferedWriter(new FileWriter(
		    "/diaphanum/log/" + logName + ".txt", true));
	    writer.write(logDate + " " + ip + " " + msg);
	    writer.newLine();
	    writer.close();
	} catch (SecurityException e) {
	    System.out.println(e.getMessage());
	} catch (IOException e) {
	    System.out.println(e.getMessage());
	}
    }
}
