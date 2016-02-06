package com;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * @author ttt
 */
@WebFilter(filterName = "CharacterEncodingFilter", urlPatterns = { "/*" })
public class CharacterEncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
	    FilterChain chain) throws IOException, ServletException {
	try {
	    request.setCharacterEncoding("UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    chain.doFilter(request, response);
	} catch (IOException ioe) {
	    @SuppressWarnings("unused")
	    LogBean logBean = new LogBean(ioe.getMessage());
	    System.out
		    .println("IOException: " + ioe.getStackTrace().toString());
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	} catch (ServletException se) {
	    @SuppressWarnings("unused")
	    LogBean logBean = new LogBean(se.getMessage());
	    System.out.println("ServletException: "
		    + se.getRootCause().toString());
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	}
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}