package com.user;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;

import com.AuthBean;
import com.LogBean;
import com.ProjectView;
import com.facade.ProjectFacade;
import com.facade.ProjectFileFacade;
import com.facade.ProjectMemberFacade;
import com.facade.ProjectStatusFacade;
import com.model.Project;
import com.model.ProjectFile;
import com.model.ProjectMember;
import com.model.ProjectStatus;
import com.model.User;

@RequestScoped
@ManagedBean(name = "projectEdit", eager = true)
public class ProjectEditBean implements Serializable {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private User user;
    private Project project;
    private Integer projectId;
    private static Calendar cal = Calendar.getInstance();
    private Boolean isEditable = false;
    private Boolean isCommendable = false;
    private List<ProjectMember> members;
    private String memberFullname;
    private List<ProjectFile> files;
    private static final String filesDirectory = "/diaphanum/files/";
    private UploadedFile file;
    private List<ProjectStatus> projectStatuses;
    @SuppressWarnings("unused")
    private ProjectView projectView;

    private FacesContext context = FacesContext.getCurrentInstance();
    private ExternalContext externalContext = context.getExternalContext();
    private ResourceBundle bundle = context.getApplication().getResourceBundle(
	    context, "msgs");
    private HttpServletRequest req = (HttpServletRequest) externalContext
	    .getRequest();

    @EJB
    private ProjectFacade projectFacade;

    @EJB
    private ProjectMemberFacade projectMemberFacade;

    @EJB
    private ProjectFileFacade projectFileFacade;

    @EJB
    private ProjectStatusFacade projectStatusFacade;

    @PostConstruct
    public void init() {
	try {
	    user = (User) externalContext.getSessionMap().get("user");
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR ProjectEditBean.init: can't get user from session: "
			    + e.getMessage());
	}
	if (user == null) {
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	    return;
	}
	try {
	    projectId = Integer.parseInt(req.getParameter("projectId"));
	} catch (NumberFormatException nfe) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR ProjectEditBean.init: can't get projectId from request: "
			    + nfe.getMessage());
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	    return;
	}
	project = projectFacade.getById(projectId);
	if (project == null) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR ProjectEditBean.init: no project found, project.id: "
			    + projectId);
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	    return;
	} else {
	    if ((project.getCreatedBy() != user.getId())
		    && (!user.getIsAdmin())) {
		@SuppressWarnings("unused")
		LogBean log = new LogBean(
			"ERROR ProjectEditBean.init: unathorized access to projectEdit.xhtml, project.id: "
				+ projectId);
		AuthBean authBean = new AuthBean();
		authBean.logout();
		return;
	    }
	    if (project.getCreatedBy().equals(user.getId())) {
		Integer[] editableStatuses = { 1, 2 };
		if (Arrays.asList(editableStatuses).contains(
			project.getStatus())) {
		    isEditable = true;
		}
	    }
	    if (!project.getCreatedBy().equals(user.getId())
		    && (user.getIsAdmin())) {
		isCommendable = true;
	    }
	}
	setMembers(projectMemberFacade.getByProjectWithoutOwner(projectId));
	setFiles(projectFileFacade.getByProject(projectId));
	setProjectStatuses(projectStatusFacade.getAll());
    }

    public void projectUpdate() {
	if (user.getFullname() == null) {
	    context.addMessage(
		    null,
		    new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
			    .getString("currentUserFullnameNull"), null));
	    return;
	}
	if (user.getEmail() == null) {
	    context.addMessage(
		    null,
		    new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
			    .getString("currentUserEmailNull"), null));
	    return;
	}
	if (user.getPhone() == null) {
	    context.addMessage(
		    null,
		    new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
			    .getString("currentUserPhoneNull"), null));
	    return;
	}
	project.setEditedOn(cal.getTime());
	projectFacade.update(project);
	projectView = new ProjectView(project, user, members);
	@SuppressWarnings("unused")
	LogBean log = new LogBean(
		"INFO ProjectEditBean.updateProject: OK, user.id: "
			+ user.getId() + ", project.id: " + projectId);
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("updateProjectSuccess"), null));
    }

    public String projectDelete() {
	project.setIsActive(false);
	project.setEditedOn(cal.getTime());
	projectFacade.update(project);
	@SuppressWarnings("unused")
	LogBean log = new LogBean(
		"INFO ProjectEditBean.projectDelete: OK, user.id: "
			+ user.getId() + ", project.id: " + projectId);
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("removeProjectSuccess"), null));
	return "/pages/protected/profile.xhtml";
    }

    public void memberAdd() {
	ProjectMember member = new ProjectMember();
	member.setIsActive(true);
	member.setCreatedOn(cal.getTime());
	member.setEditedOn(cal.getTime());
	member.setProject(project.getId());
	member.setFullname(memberFullname);
	projectMemberFacade.save(member);
	@SuppressWarnings("unused")
	LogBean log = new LogBean(
		"INFO ProjectEditBean.memberAdd: OK, by user.id: "
			+ user.getId() + ", to project.id: " + projectId);
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("addProjectMemberSuccess"), null));
    }

    public void memberRemove(ProjectMember projectMember) {
	projectMemberFacade.delete(projectMember);
	@SuppressWarnings("unused")
	LogBean log = new LogBean(
		"INFO ProjectEditBean.memberRemove: OK, user.id: "
			+ user.getUsername() + ", project.id: " + projectId);
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("removeProjectMemberSuccess"), null));
    }

    public void fileRemove(ProjectFile projectFile) {
	projectFileFacade.delete(projectFile);
	try {
	    String file = filesDirectory + projectFile.getName();
	    File fileRemove = new File(file);
	    fileRemove.delete();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR ProjectEditBean.fileRemove: file.id: "
			    + projectFile.getId() + "; " + e.getMessage());
	}
	@SuppressWarnings("unused")
	LogBean log = new LogBean(
		"INFO ProjectEditBean.fileRemove(): OK, file.id: "
			+ projectFile.getId());
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("removeProjectFileSuccess"), null));
    }

    public void fileUpload() {
	ProjectFile projectFile = new ProjectFile();
	projectFile.setIsActive(true);
	projectFile.setCreatedOn(cal.getTime());
	projectFile.setEditedOn(cal.getTime());
	projectFile.setProject(projectId);
	try {
	    SimpleDateFormat fileNameFormat = new SimpleDateFormat(
		    "yyyyMMddHHmmss");
	    String fileName = "projectFile"
		    + fileNameFormat.format(cal.getTime()) + "r";
	    String extension = FilenameUtils.getExtension(file.getFileName());
	    projectFile.setTitle(file.getFileName());
	    File folder = new File(filesDirectory);
	    File fileUpload = File.createTempFile(fileName, "." + extension,
		    folder);
	    projectFile.setName(fileUpload.getName());
	    projectFileFacade.save(projectFile);
	    InputStream input = file.getInputstream();
	    byte[] bytes = IOUtils.toByteArray(input);
	    InputStream inputTest = new ByteArrayInputStream(bytes);
	    OutputStream output = new FileOutputStream(fileUpload, true);
	    IOUtils.copy(inputTest, output);
	    FacesContext.getCurrentInstance().addMessage(
		    null,
		    new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
			    .getString("uploadFileSuccess"), null));
	    setFile(null);
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("ERROR ProjectEditBean.fileUpload: "
		    + e.getMessage());
	}
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

    public Boolean getIsEditable() {
	return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
	this.isEditable = isEditable;
    }

    public List<ProjectMember> getMembers() {
	return members;
    }

    public void setMembers(List<ProjectMember> members) {
	this.members = members;
    }

    public String getMemberFullname() {
	return memberFullname;
    }

    public void setMemberFullname(String memberFullname) {
	this.memberFullname = memberFullname;
    }

    public List<ProjectFile> getFiles() {
	return files;
    }

    public void setFiles(List<ProjectFile> files) {
	this.files = files;
    }

    public UploadedFile getFile() {
	return file;
    }

    public void setFile(UploadedFile file) {
	this.file = file;
    }

    public Boolean getIsCommendable() {
	return isCommendable;
    }

    public void setIsCommendable(Boolean isCommendable) {
	this.isCommendable = isCommendable;
    }

    public List<ProjectStatus> getProjectStatuses() {
	return projectStatuses;
    }

    public void setProjectStatuses(List<ProjectStatus> projectStatuses) {
	this.projectStatuses = projectStatuses;
    }
}
