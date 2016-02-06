package com;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.UploadedFile;

import com.facade.DocumentFacade;
import com.facade.DocumentTypeFacade;
import com.model.Document;
import com.model.DocumentType;
import com.model.User;
import com.user.LazyDataModelDocument;

@RequestScoped
@ManagedBean(name = "documents", eager = true)
public class DocumentsBean implements Serializable {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private User user;
    private LazyDataModel<Document> lazyModelDocument;
    private Document selectedDocument;
    private List<DocumentType> documentTypes;
    private Document documentNew;
    private Integer documentNewTypeId;
    private static Calendar cal = Calendar.getInstance();
    private UploadedFile file;
    private static final String filesDirectory = "/diaphanum/files/";

    private FacesContext context = FacesContext.getCurrentInstance();
    private ResourceBundle bundle = context.getApplication().getResourceBundle(
	    context, "msgs");
    
    @EJB
    private DocumentFacade documentFacade;

    @EJB
    private DocumentTypeFacade documentTypeFacade;

    @PostConstruct
    public void init() {
	setLazyModelDocument(new LazyDataModelDocument(documentFacade.getAllReversed()));
	setDocumentTypes(documentTypeFacade.getAll());
	try {
	    user = (User) context.getExternalContext().getSessionMap()
		    .get("user");
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR ProjectsBean.init: can't get user from session: "
			    + e.getMessage());
	}
	documentNew = new Document();
    }

    public String getDocumentTypeName(Integer documentTypeId) {
	String result = "";
	if (documentTypeId != null) {
	    try {
		result += documentTypeFacade.getById(documentTypeId).getName();
	    } catch (Exception e) {
		@SuppressWarnings("unused")
		LogBean log = new LogBean(
			"ERROR DocumentsBean.getDocumentTypeName: can't get name of documentType: "
				+ e.getMessage());
	    }
	}
	return result;
    }

    public String documentCreate() {
	if (user == null) {
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	    return null;
	}
	documentNew.setType(documentNewTypeId);
	documentNew.setCreatedBy(user.getId());
	documentNew.setCreatedOn(cal.getTime());
	documentNew.setEditedOn(cal.getTime());
	documentNew.setIsActive(true);
	try {
	    SimpleDateFormat fileNameFormat = new SimpleDateFormat(
		    "yyyyMMddHHmmss");
	    String fileName = "doc" + fileNameFormat.format(cal.getTime())
		    + "r";
	    String extension = FilenameUtils.getExtension(file.getFileName());
	    File folder = new File(filesDirectory);
	    File fileUpload = File.createTempFile(fileName, "." + extension,
		    folder);
	    documentNew.setName(fileUpload.getName());
	    InputStream input = file.getInputstream();
	    byte[] bytes = IOUtils.toByteArray(input);
	    InputStream inputTest = new ByteArrayInputStream(bytes);
	    OutputStream output = new FileOutputStream(fileUpload, true);
	    IOUtils.copy(inputTest, output);
	    setFile(null);
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR DocumentBean.documentCreate: upload file: "
			    + e.getMessage());
	    context.addMessage(
		    null,
		    new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
			    .getString("uploadDocumentFailed"), null));
	    return "/pages/protected/profile.xhtml";
	}
	documentFacade.save(documentNew);
	Document documentNewView = new Document();
	documentNewView = documentFacade.getLast();
	@SuppressWarnings("unused")
	LogBean log = new LogBean(
		"INFO DocumentBean.documentCreate(): OK, documentId: "
			+ documentNewView.getId());
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("createDocumentSuccess"), null));
	documentNew = null;
	return "/pages/protected/profile.xhtml";
    }

    public LazyDataModel<Document> getLazyModelDocument() {
	return lazyModelDocument;
    }

    public void setLazyModelDocument(LazyDataModel<Document> lazyModelDocument) {
	this.lazyModelDocument = lazyModelDocument;
    }

    public List<DocumentType> getDocumentTypes() {
	return documentTypes;
    }

    public void setDocumentTypes(List<DocumentType> documentTypes) {
	this.documentTypes = documentTypes;
    }

    public Document getSelectedDocument() {
	return selectedDocument;
    }

    public void setSelectedDocument(Document selectedDocument) {
	this.selectedDocument = selectedDocument;
    }

    public Document getDocumentNew() {
	return documentNew;
    }

    public void setDocumentNew(Document documentNew) {
	this.documentNew = documentNew;
    }

    public Integer getDocumentNewTypeId() {
	return documentNewTypeId;
    }

    public void setDocumentNewTypeId(Integer documentNewTypeId) {
	this.documentNewTypeId = documentNewTypeId;
    }

    public UploadedFile getFile() {
	return file;
    }

    public void setFile(UploadedFile file) {
	this.file = file;
    }
}