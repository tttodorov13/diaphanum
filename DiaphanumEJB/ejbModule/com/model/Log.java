package com.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@RequestScoped
@ManagedBean(name = "log", eager = true)
public class Log {

	private static Calendar cal = Calendar.getInstance();
	private static SimpleDateFormat logNameFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static String logName = logNameFormat.format(cal.getTime());
	private static SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	private static String logDate = logDateFormat.format(cal.getTime());
	
	public Log(String msg) {
		writeLog(msg);
	}
	
	private static void writeLog(String msg) {
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter("/diaphanum/log/" + logName + ".txt", true));
			writer.write(logDate + " " + msg);
			writer.newLine();
			writer.close();
		} catch (SecurityException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
