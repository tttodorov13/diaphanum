package com.user;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.LazyDataModel;

import com.AuthBean;
import com.LogBean;
import com.facade.MessageFacade;
import com.facade.ProjectFacade;
import com.facade.ProjectStatusFacade;
import com.facade.UserFacade;
import com.model.Message;
import com.model.Project;
import com.model.ProjectStatus;
import com.model.User;

@RequestScoped
@ManagedBean(name = "profileView", eager = true)
public class ProfileViewBean implements Serializable {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private User user;
    private User profile;
    private Integer userId;
    private LazyDataModel<Project> lazyModelProject;
    private Project selectedProject;
    private List<ProjectStatus> projectStatuses;
    private LazyDataModel<Message> lazyModelMessage;
    private Message selectedMessage;
    private LazyDataModel<User> lazyModelUser;
    private User selectedUser;
    private static Calendar cal = Calendar.getInstance();
    
    private FacesContext context = FacesContext.getCurrentInstance();
    private ExternalContext externalContext = context.getExternalContext();
    private ResourceBundle bundle = context.getApplication().getResourceBundle(
	    context, "msgs");
    private HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();

    @EJB
    private UserFacade userFacade;

    @EJB
    private ProjectFacade projectFacade;

    @EJB
    private ProjectStatusFacade projectStatusFacade;

    @EJB
    private MessageFacade messageFacade;

    @PostConstruct
    public void init() {
	try {
	    user = (User) externalContext.getSessionMap().get("user");
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "INFO ProfileBean.init: can't get user from session: "
			    + e.getMessage());
	}
	try {
	    userId = Integer.parseInt(req.getParameter("userId"));
	    if (userId != null) {
		profile = userFacade.getById(userId);
	    }
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR ProfileViewBean.init: can't get profile from request: "
			    + e.getMessage());
	}
	if (profile == null) {
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	    return;
	}
	setLazyModelProject(new LazyDataModelProject(
		projectFacade.getByUser(userId)));
	setProjectStatuses(projectStatusFacade.getAll());
	setLazyModelMessage(new LazyDataModelMessage(
		messageFacade.getByCreator(userId)));
	setLazyModelUser(new LazyDataModelUser(userFacade.getAll()));
    }

    public void updateUser() {
	profile.setEditedOn(cal.getTime());
	userFacade.update(profile);
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("changesSaved"), null));
	if (user.getId() == profile.getId()) {
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	    return;
	}
    }

    public String getProjectStatusName(Integer projectStatusId) {
	String result = "";
	if (projectStatusId != null) {
	    try {
		result += projectStatusFacade.getById(projectStatusId)
			.getName();
	    } catch (Exception e) {
		@SuppressWarnings("unused")
		LogBean log = new LogBean(
			"ERROR ProjectsViewBean.getProjectStatusName("
				+ projectStatusId
				+ "): can't get projectStatusName: "
				+ e.getMessage());
	    }
	}
	return result;
    }

    public User getProfile() {
	return profile;
    }

    public void setProfile(User user) {
	this.profile = user;
    }

    public LazyDataModel<Project> getLazyModelProject() {
	return lazyModelProject;
    }

    public void setLazyModelProject(LazyDataModel<Project> lazyModelProject) {
	this.lazyModelProject = lazyModelProject;
    }

    public Project getSelectedProject() {
	return selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
	this.selectedProject = selectedProject;
    }

    public ProjectFacade getProjectFacade() {
	return projectFacade;
    }

    public void setProjectFacade(ProjectFacade projectFacade) {
	this.projectFacade = projectFacade;
    }

    public ProjectStatusFacade getProjectStatusFacade() {
	return projectStatusFacade;
    }

    public void setProjectStatusFacade(ProjectStatusFacade projectStatusFacade) {
	this.projectStatusFacade = projectStatusFacade;
    }

    public List<ProjectStatus> getProjectStatuses() {
	return projectStatuses;
    }

    public void setProjectStatuses(List<ProjectStatus> projectStatuses) {
	this.projectStatuses = projectStatuses;
    }

    public LazyDataModel<User> getLazyModelUser() {
	return lazyModelUser;
    }

    public void setLazyModelUser(LazyDataModel<User> lazyModelUser) {
	this.lazyModelUser = lazyModelUser;
    }

    public User getSelectedUser() {
	return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
	this.selectedUser = selectedUser;
    }

    public LazyDataModel<Message> getLazyModelMessage() {
	return lazyModelMessage;
    }

    public void setLazyModelMessage(LazyDataModel<Message> lazyModelMessage) {
	this.lazyModelMessage = lazyModelMessage;
    }

    public Message getSelectedMessage() {
	return selectedMessage;
    }

    public void setSelectedMessage(Message selectedMessage) {
	this.selectedMessage = selectedMessage;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }
}