package com;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.LazyDataModel;

import com.facade.ProjectFacade;
import com.facade.ProjectStatusFacade;
import com.facade.UserFacade;
import com.model.Project;
import com.model.ProjectStatus;
import com.user.LazyDataModelProject;

@RequestScoped
@ManagedBean(name = "projects", eager = true)
public class ProjectsBean implements Serializable {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private LazyDataModel<Project> lazyModelProject;
    private Project selectedProject;
    private List<ProjectStatus> projectStatuses;
    private List<Project> projects;

    @EJB
    private UserFacade userFacade;

    @EJB
    private ProjectFacade projectFacade;

    @EJB
    private ProjectStatusFacade projectStatusFacade;

    @PostConstruct
    public void init() {
	setProjects(projectFacade.getAll());
	setLazyModelProject(new LazyDataModelProject(projectFacade.getAllReversed()));
	setProjectStatuses(projectStatusFacade.getAll());
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
			"ERROR ProjectsBean.getProjectStatusName: can't get name of projectStatus: "
				+ e.getMessage());
	    }
	}
	return result;
    }

    public String getProjectUserFullname(Integer userId) {
	String result = "";
	if (userId != null) {
	    try {
		result += userFacade.getById(userId).getFullname();
	    } catch (Exception e) {
		@SuppressWarnings("unused")
		LogBean log = new LogBean(
			"ERROR ProjectsBean.getProjectUserFullname: can't get name of project.createBy: "
				+ e.getMessage());
	    }
	}
	return result;
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

    public List<Project> getProjects() {
	return projects;
    }

    public void setProjects(List<Project> projects) {
	this.projects = projects;
    }

    public List<ProjectStatus> getProjectStatuses() {
	return projectStatuses;
    }

    public void setProjectStatuses(List<ProjectStatus> projectStatuses) {
	this.projectStatuses = projectStatuses;
    }
}
