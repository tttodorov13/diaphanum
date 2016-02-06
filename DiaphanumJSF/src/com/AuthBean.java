package com;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.mindrot.jbcrypt.BCrypt;

import com.facade.ProjectMemberFacade;
import com.facade.UserFacade;
import com.model.ProjectMember;
import com.model.User;

@SessionScoped
@ManagedBean(name = "auth", eager = true)
public class AuthBean implements Serializable {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String email;
    private String password1;
    private String password2;
    private User user;
    private static final String uploadDirectory = "/diaphanum/uploads/";
    private static Calendar cal = Calendar.getInstance();

    @EJB
    private UserFacade userFacade;

    @EJB
    private ProjectMemberFacade projectMemberFacade;

    public void login() {
	FacesContext context = FacesContext.getCurrentInstance();
	ResourceBundle bundle = context.getApplication().getResourceBundle(
		context, "msgs");
	getLocalUser(username, password);
	if (user == null) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR AuthBean.login: no local user found");
	    context.addMessage(
		    null,
		    new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
			    .getString("wrongUsernamePassword"), null));
	} else {
	    systemLogin(user);
	    context.addMessage(null, new FacesMessage(
		    FacesMessage.SEVERITY_INFO, bundle.getString("welcome")
			    + " " + user.getFullname() + "!", null));
	}
    }

    private void getLocalUser(String username, String password) {
	user = userFacade.getByUsername(username);
	if (user == null) {
	    user = userFacade.getByEmail(username);
	}
	if (user == null) {
	    user = userFacade.getByPhone(username);
	}
	if (user != null) {
	    Boolean checkpw = false;
	    try {
		checkpw = BCrypt.checkpw(password, user.getPassword());
	    } catch (Exception e) {
		@SuppressWarnings("unused")
		LogBean log = new LogBean("ERROR BCrypt.checkpw(): "
			+ e.getMessage());
	    }
	    if (!checkpw) {
		user = null;
	    }
	}
    }

    private void systemLogin(User user) {
	ExternalContext externalContext = FacesContext.getCurrentInstance()
		.getExternalContext();
	@SuppressWarnings("unused")
	LogBean log = new LogBean("INFO AuthBean.login: OK, user.id: "
		+ user.getId());
	externalContext.getSessionMap().put("user", user);
	try {
	    ExternalContext ec = FacesContext.getCurrentInstance()
		    .getExternalContext();
	    String url = ec.getRequestContextPath()
		    + "/pages/protected/profile.xhtml";
	    ec.redirect(url);
	} catch (IOException e) {
	    @SuppressWarnings("unused")
	    LogBean log1 = new LogBean(
		    "ERROR AuthBean.systemLogin: can't redirect to profile.xhtml, e: "
			    + e.getMessage());
	}
    }

    public void logout() {
	try {
	    ExternalContext externalContext = FacesContext.getCurrentInstance()
		    .getExternalContext();
	    externalContext.invalidateSession();
	    externalContext.redirect(externalContext.getRequestContextPath());
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean lBean = new LogBean("ERROR AuthBean.init: " + e.getMessage());
	}
    }

    public void register() {
	FacesContext context = FacesContext.getCurrentInstance();
	ResourceBundle bundle = context.getApplication().getResourceBundle(
		context, "msgs");
	ExternalContext externalContext = FacesContext.getCurrentInstance()
		.getExternalContext();
	user = userFacade.getByEmail(email);
	if (user != null) {
	    context.addMessage(null, new FacesMessage(
		    FacesMessage.SEVERITY_ERROR, bundle.getString("emailUsed"),
		    null));
	} else {
	    if (password1 != null) {
		if (password1.equals(password2)) {
		    user = new User();
		    user.setIsActive(true);
		    user.setIsAdmin(false);
		    user.setCreatedOn(cal.getTime());
		    user.setEditedOn(cal.getTime());
		    user.setEmail(email);
		    user.setUsername(email);
		    try {
			user.setPassword(BCrypt.hashpw(password1,
				BCrypt.gensalt()));
		    } catch (Exception e) {
			context.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
					bundle.getString("sorrySystemError"), null));
			@SuppressWarnings("unused")
			LogBean log = new LogBean("ERROR BCrypt.hashpw(): "
				+ e.getMessage());
			return;
		    }
		    String fileName = uploadDirectory
			    .concat("profilePhotoUserDefault.png");
		    user.setPhoto(fileName);
		    userFacade.save(user);
		    Integer userId = userFacade.getLast().getId();
		    user.setId(userId);
		    externalContext.getSessionMap().put("user", user);
		    ProjectMember projectMember = new ProjectMember();
		    projectMember.setUserId(userId);
		    projectMemberFacade.save(projectMember);
		    @SuppressWarnings("unused")
		    LogBean log = new LogBean(
			    "INFO AuthBean.register: OK, user.username: "
				    + userId);
		    externalContext.getSessionMap().put("user", user);
		    context.addMessage(
			    null,
			    new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
				    .getString("registerSuccess")
				    + ", "
				    + email + "!", null));
		} else {
		    context.addMessage(null,
			    new FacesMessage(FacesMessage.SEVERITY_ERROR,
				    bundle.getString("passwordsNoMatch"), null));
		}
	    }
	}
    }

    public Boolean isLoggedIn() {
	return user != null;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public String getPassword1() {
	return password1;
    }

    public void setPassword1(String password1) {
	this.password1 = password1;
    }

    public String getPassword2() {
	return password2;
    }

    public void setPassword2(String password2) {
	this.password2 = password2;
    }
}