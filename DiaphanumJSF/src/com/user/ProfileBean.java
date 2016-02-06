package com.user;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.UploadedFile;

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
@ManagedBean(name = "profile", eager = true)
public class ProfileBean implements Serializable {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private User user;
    private String password1;
    private String password2;
    private LazyDataModel<Project> lazyModelProject;
    private Project selectedProject;
    private List<ProjectStatus> projectStatuses;
    private static Calendar cal = Calendar.getInstance();
    private static final String uploadDirectory = "/diaphanum/uploads/";
    private UploadedFile file;
    private LazyDataModel<User> lazyModelUser;
    private User selectedUser;
    private LazyDataModel<Message> lazyModelMessage;
    private Message selectedMessage;

    private FacesContext context = FacesContext.getCurrentInstance();
    private ExternalContext externalContext = context.getExternalContext();
    private ResourceBundle bundle = context.getApplication().getResourceBundle(
	    context, "msgs");

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
		    "ERROR ProfileBean.init: can't get user from session: "
			    + e.getMessage());
	}
	if (user == null) {
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	    return;
	}
	setLazyModelProject(new LazyDataModelProject(
		projectFacade.getByUser(user.getId())));
	setLazyModelUser(new LazyDataModelUser(userFacade.getAll()));
	setProjectStatuses(projectStatusFacade.getAll());
	setLazyModelMessage(new LazyDataModelMessage(
		messageFacade.getByCreator(user.getId())));
    }

    public void userUpdate() {
	if (password1 != null) {
	    if (password1.equals(password2)) {
		user.setPassword(BCrypt.hashpw(password1, BCrypt.gensalt()));
	    }
	    context.addMessage(
		    null,
		    new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
			    .getString("passwordsNoMatch"), null));
	}
	user.setEditedOn(cal.getTime());
	userFacade.update(user);
	@SuppressWarnings("unused")
	LogBean log = new LogBean("INFO ProfileBean.userUpdate(): OK, userId: "
		+ user.getId());
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("changesSaved"), null));
	try {
	    ExternalContext ec = FacesContext.getCurrentInstance()
		    .getExternalContext();
	    String url = ec.getRequestContextPath()
		    + "/pages/protected/profile.xhtml?userId=" + user.getId();
	    ec.redirect(url);
	} catch (IOException e) {
	    @SuppressWarnings("unused")
	    LogBean log1 = new LogBean(
		    "ERROR AuthBean.systemLogin: can't redirect to profile.xhtml, e: "
			    + e.getMessage());
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
			"ERROR ProjectsBean.getProjectStatusName("
				+ projectStatusId
				+ "): can't get projectStatusName: "
				+ e.getMessage());
	    }
	}
	return result;
    }

    public void fileUpload() {
	String fileName = "profilePhotoUserId" + user.getId();
	try {
	    File folder = new File(uploadDirectory);
	    String extension = FilenameUtils.getExtension(file.getFileName());
	    File fileUpload = File.createTempFile(fileName, "." + extension,
		    folder);
	    if (user.getPhoto() == uploadDirectory.concat(fileName + "Default")) {
		user.setPhoto(uploadDirectory
			.concat(fileName + "." + extension));
	    }
	    InputStream input = file.getInputstream();
	    byte[] bytes = IOUtils.toByteArray(input);
	    InputStream inputTest = new ByteArrayInputStream(bytes);
	    OutputStream output = new FileOutputStream(fileUpload, true);
	    IOUtils.copy(inputTest, output);
	    FacesContext.getCurrentInstance().addMessage(
		    null,
		    new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
			    .getString("uploadPhotoSuccess"), null));
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("ERROR ProjectsBean.fileUpload: "
		    + e.getMessage());
	}
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

    public UploadedFile getFile() {
	return file;
    }

    public void setFile(UploadedFile file) {
	this.file = file;
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
}