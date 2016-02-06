package com;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@RequestScoped
@ManagedBean(name = "menu", eager = true)
public class MenuBean implements Serializable {

    /**
     * @author ttt
     */
    static final long serialVersionUID = 1L;

    public static String redirectToRoot() {
	ExternalContext ec = FacesContext.getCurrentInstance()
		.getExternalContext();
	return ec.getRequestContextPath();
    }

    public static String moveToHomePage() {
	return "home?faces-redirect=true";
    }

    public static String moveToProjectNewPage() {
	return "projectNew?faces-redirect=true";
    }

    public static String moveToProjectsPage() {
	return "projects?faces-redirect=true";
    }

    public static String moveToProtocolsPage() {
	return "documents?faces-redirect=true";
    }

    public static String moveToFeedbackPage() {
	return "feedback?faces-redirect=true";
    }

    public static String moveToLoginPage() {
	return "login?faces-redirect=true";
    }

    public static String moveToProfilePage() {
	return "profile?faces-redirect=true";
    }

    public String logOut() {
	getRequest().getSession().invalidate();
	return "logout";
    }

    private HttpServletRequest getRequest() {
	return (HttpServletRequest) FacesContext.getCurrentInstance()
		.getExternalContext().getRequest();
    }
}