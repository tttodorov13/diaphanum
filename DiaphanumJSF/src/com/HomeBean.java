package com;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.facade.DocumentFacade;
import com.facade.ProjectFacade;
import com.model.Document;
import com.model.Project;

@SessionScoped
@ManagedBean(name = "home", eager = true)
public class HomeBean implements Serializable {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private Project project;
    private Document document;

    @EJB
    private ProjectFacade projectFacade;

    @EJB
    private DocumentFacade documentFacade;

    @PostConstruct
    public void init() {
	setProject(projectFacade.getLast());
	setDocument(documentFacade.getLast());
    }

    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    public Document getDocument() {
	return document;
    }

    public void setDocument(Document document) {
	this.document = document;
    }
}