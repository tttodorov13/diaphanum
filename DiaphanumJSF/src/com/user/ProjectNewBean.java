package com.user;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.AuthBean;
import com.LogBean;
import com.ProjectView;
import com.facade.ProjectFacade;
import com.facade.ProjectMemberFacade;
import com.model.Project;
import com.model.ProjectMember;
import com.model.User;

@RequestScoped
@ManagedBean(name = "projectNew", eager = true)
public class ProjectNewBean implements Serializable {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private User user;
    private Project project;
    private Integer projectId;
    private Integer projectStatusUnchecked = 1;
    private static Calendar cal = Calendar.getInstance();
    @SuppressWarnings("unused")
    private ProjectView projectView;

    @EJB
    private ProjectFacade projectFacade;

    @EJB
    private ProjectMemberFacade projectMemberFacade;

    private FacesContext context = FacesContext.getCurrentInstance();
    private ExternalContext externalContext = context.getExternalContext();
    private ResourceBundle bundle = context.getApplication().getResourceBundle(
	    context, "msgs");
    private Map<String, Object> sessionMap = externalContext.getSessionMap();

    @PostConstruct
    public void init() {
	project = new Project();
	try {
	    user = (User) externalContext.getSessionMap().get("user");
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR ProjectsBean.init: can't get user from session: "
			    + e.getMessage());
	}
	if (user == null) {
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	    return;
	}
	project.setIsActive(true);
	project.setCreatedBy(user.getId());
	project.setCreatedOn(cal.getTime());
	project.setEditedOn(cal.getTime());
	project.setStatus(projectStatusUnchecked);
	sessionMap.put("projectId", projectId);
	@SuppressWarnings("unused")
	LogBean log = new LogBean("INFO ProjectNewBean.init: OK, project.id: "
		+ projectId + "; user.id: " + user.getId());
    }

    public String projectCreate() {
	projectFacade.save(project);
	Project savedProject = projectFacade.getLast();
	ProjectMember projectMember = new ProjectMember();
	projectMember.setIsActive(true);
	projectMember.setCreatedOn(cal.getTime());
	projectMember.setEditedOn(cal.getTime());
	projectMember.setProject(project.getId());
	projectMember.setUserId(user.getId());
	projectMember.setFullname(user.getFullname());
	projectMemberFacade.save(projectMember);
	projectView = new ProjectView(savedProject, user, projectMemberFacade.getByProject(savedProject.getId()));
	@SuppressWarnings("unused")
	LogBean log = new LogBean(
		"INFO ProjectNewBean.createProject: set current user as member projectMember.id: "
			+ project.getId());
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("createProjectSuccess"), null));
	return "/pages/protected/profile.xhtml";
    }

    public String projectReturn() {
	project.setIsActive(false);
	project.setEditedOn(cal.getTime());
	projectFacade.update(project);
	@SuppressWarnings("unused")
	LogBean log = new LogBean(
		"INFO ProjectNewBean.projectReturn: OK, user.id: "
			+ user.getUsername() + ", project.id: " + projectId);
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("projectReturnSuccess"), null));
	return "/pages/protected/profile.xhtml";
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    public Integer getProjectId() {
	return projectId;
    }

    public void setProjectId(Integer projectId) {
	this.projectId = projectId;
    }
}
