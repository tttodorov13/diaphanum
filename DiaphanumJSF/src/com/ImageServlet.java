package com;

import java.io.File;
import java.nio.file.Files;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/uploads/*")
public class ImageServlet extends HttpServlet {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    String filename = request.getPathInfo().substring(1);
	    File file = new File("/diaphanum/uploads", filename);
	    response.setHeader("Content-Type",
		    getServletContext().getMimeType(filename));
	    response.setHeader("Content-Length", String.valueOf(file.length()));
	    response.setHeader("Content-Disposition", "inline; filename=\""
		    + filename + "\"");
	    Files.copy(file.toPath(), response.getOutputStream());
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}
    }
}