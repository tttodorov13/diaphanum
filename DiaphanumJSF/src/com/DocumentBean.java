package com;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import com.facade.DocumentFacade;
import com.facade.DocumentTypeFacade;
import com.model.Document;
import com.model.DocumentType;
import com.model.User;

@RequestScoped
@ManagedBean(name = "document", eager = true)
public class DocumentBean implements Serializable {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private User user;
    private List<DocumentType> documentTypes;
    private Document document;
    private Integer documentId;
    private DocumentType documentType;
    private Integer documentTypeId;
    private static Calendar cal = Calendar.getInstance();
    private UploadedFile file;
    private static final String filesDirectory = "/diaphanum/files/";
    private String documentTitle;
    private String documentInstitution;
    private String documentSignedBy;
    private Date documentDate;

    private FacesContext context = FacesContext.getCurrentInstance();
    private ExternalContext externalContext = context.getExternalContext();
    private ResourceBundle bundle = context.getApplication().getResourceBundle(
	    context, "msgs");
    private HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();

    @EJB
    private DocumentFacade documentFacade;

    @EJB
    private DocumentTypeFacade documentTypeFacade;

    @PostConstruct
    public void init() {
	setDocumentTypes(documentTypeFacade.getAll());
	try {
	    user = (User) externalContext.getSessionMap().get("user");
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR DocumentBean.init: can't get user from session: "
			    + e.getMessage());
	}
	if (user == null) {
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	    return;
	} else if (!user.getIsAdmin()) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR DocumentBean.init: user not allowed to edit current document.id: "
			    + documentId);
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	    return;
	}
	try {
	    documentId = Integer.parseInt(req.getParameter("documentId"));
	} catch (NumberFormatException nfe) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR DocumentBean.init: can't get document from request: "
			    + nfe.getMessage());
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	    return;
	}
	document = new Document();
	document = documentFacade.getById(documentId);
	setDocumentType(documentTypeFacade.getById(document.getType()));
	setDocumentTitle(document.getTitle());
	setDocumentInstitution(document.getInstitution());
	setDocumentSignedBy(document.getSignedBy());
	setDocumentDate(document.getDocumentDate());
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

    public void documentUpdate() {
	document.setEditedOn(cal.getTime());
	document.setTitle(documentTitle);
	document.setInstitution(documentInstitution);
	document.setSignedBy(documentSignedBy);
	document.setDocumentDate(documentDate);
	if (documentType != null) {
	    document.setType(documentTypeId);
	}
	if (file != null) {
	    try {
		String file = filesDirectory + document.getName();
		File fileRemove = new File(file);
		fileRemove.delete();
	    } catch (Exception e) {
		@SuppressWarnings("unused")
		LogBean log = new LogBean(
			"ERROR DocumentBean.documentUpdate, can't remove old file document.id: "
				+ document.getId() + ": " + e.getMessage());
	    }
	}
	try {
	    SimpleDateFormat fileNameFormat = new SimpleDateFormat(
		    "yyyyMMddHHmmss");
	    String fileName = "doc" + fileNameFormat.format(cal.getTime())
		    + "r";
	    String extension = FilenameUtils.getExtension(file.getFileName());
	    File folder = new File(filesDirectory);
	    File fileUpload = File.createTempFile(fileName, "." + extension,
		    folder);
	    document.setName(fileUpload.getName());
	    InputStream input = file.getInputstream();
	    byte[] bytes = IOUtils.toByteArray(input);
	    InputStream inputTest = new ByteArrayInputStream(bytes);
	    OutputStream output = new FileOutputStream(fileUpload, true);
	    IOUtils.copy(inputTest, output);
	    setFile(null);
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR DocumentBean.documentUpdate: upload file: "
			    + e.getMessage());
	    context.addMessage(
		    null,
		    new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
			    .getString("uploadDocumentFailed"), null));
	}
	documentFacade.update(document);
	@SuppressWarnings("unused")
	LogBean log = new LogBean(
		"INFO DocumentBean.documentUpdate(): OK, documentId: "
			+ document.getId());
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("updateDocumentSuccess"), null));
	try {
	    ExternalContext ec = FacesContext.getCurrentInstance()
		    .getExternalContext();
	    String url = ec.getRequestContextPath()
		    + "/pages/public/documents.xhtml";
	    ec.redirect(url);
	} catch (IOException e) {
	    @SuppressWarnings("unused")
	    LogBean log1 = new LogBean(
		    "ERROR DocumentBean.documentUpdate: can't redirect to documents.xhtml, e: "
			    + e.getMessage());
	}
    }

    public void documentRemove(Document document) {
	documentFacade.delete(document);
	try {
	    String file = filesDirectory + document.getName();
	    File fileRemove = new File(file);
	    fileRemove.delete();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR DocumentBean.documentRemove, document.id: "
			    + document.getId() + ": " + e.getMessage());
	}
	@SuppressWarnings("unused")
	LogBean log = new LogBean(
		"INFO DocumentBean.documentRemove(): OK, documentId: "
			+ document.getId());
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("removeDocumentSuccess"), null));
	try {
	    ExternalContext ec = FacesContext.getCurrentInstance()
		    .getExternalContext();
	    String url = ec.getRequestContextPath()
		    + "/pages/public/documents.xhtml";
	    ec.redirect(url);
	} catch (IOException e) {
	    @SuppressWarnings("unused")
	    LogBean log1 = new LogBean(
		    "ERROR DocumentBean.documentRemove: can't redirect to documents.xhtml, e: "
			    + e.getMessage());
	}
    }

    public List<DocumentType> getDocumentTypes() {
	return documentTypes;
    }

    public void setDocumentTypes(List<DocumentType> documentTypes) {
	this.documentTypes = documentTypes;
    }

    public Document getDocument() {
	return document;
    }

    public void setDocument(Document documentNew) {
	this.document = documentNew;
    }

    public UploadedFile getFile() {
	return file;
    }

    public void setFile(UploadedFile file) {
	this.file = file;
    }

    public Integer getDocumentTypeId() {
	return documentTypeId;
    }

    public void setDocumentTypeId(Integer documentNewTypeId) {
	this.documentTypeId = documentNewTypeId;
    }

    public Integer getDocumentId() {
	return documentId;
    }

    public void setDocumentId(Integer documentId) {
	this.documentId = documentId;
    }

    public DocumentType getDocumentType() {
	return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
	this.documentType = documentType;
    }

    public String getDocumentTitle() {
	return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
	this.documentTitle = documentTitle;
    }

    public String getDocumentInstitution() {
	return documentInstitution;
    }

    public void setDocumentInstitution(String documentInstitution) {
	this.documentInstitution = documentInstitution;
    }

    public String getDocumentSignedBy() {
	return documentSignedBy;
    }

    public void setDocumentSignedBy(String documentSignedBy) {
	this.documentSignedBy = documentSignedBy;
    }

    public Date getDocumentDate() {
	return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
	this.documentDate = documentDate;
    }
}