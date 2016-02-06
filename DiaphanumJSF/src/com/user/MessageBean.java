package com.user;

import java.io.Serializable;
import java.util.Calendar;
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
import com.facade.UserFacade;
import com.model.Message;
import com.model.User;

@RequestScoped
@ManagedBean(name = "message", eager = true)
public class MessageBean implements Serializable {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private User user;
    private Message message;
    private Integer messageId;
    private LazyDataModel<Message> lazyModelMessage;
    private Message selectedMessage;
    private static Calendar cal = Calendar.getInstance();

    private FacesContext context = FacesContext.getCurrentInstance();
    private ExternalContext externalContext = context.getExternalContext();
    private ResourceBundle bundle = context.getApplication().getResourceBundle(
	    context, "msgs");
    private HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();

    @EJB
    private UserFacade userFacade;

    @EJB
    private MessageFacade messageFacade;

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
	    messageId = Integer.parseInt(req.getParameter("messageId"));
	} catch (NumberFormatException nfe) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR MessageBean.init: can't get messageId from request: "
			    + nfe.getMessage());
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	    return;
	}
	message = messageFacade.getById(messageId);
	if (message == null) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "ERROR MessageBean.init: no message found, message.id: "
			    + messageId);
	    AuthBean authBean = new AuthBean();
	    authBean.logout();
	    return;
	}
	setLazyModelMessage(new LazyDataModelMessage(
		messageFacade.getByCreator(user.getId())));
    }

    public Boolean isEditable() {
	if (user.getId().equals(message.getCreatedBy())) {
	    return true;
	}
	return false;
    }

    public void messageUpdate() {
	message.setEditedOn(cal.getTime());
	if (user.getIsAdmin()) {
	    message.setReplyBy(user.getId());
	}
	messageFacade.update(message);
	@SuppressWarnings("unused")
	LogBean log = new LogBean(
		"INFO MessageBean.messageUpdate: OK, user.id: " + user.getId()
			+ ", message.id: " + messageId);
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("requestUpdateSuccess"), null));
    }

    public String messageDelete() {
	message.setIsActive(false);
	message.setEditedOn(cal.getTime());
	messageFacade.update(message);
	@SuppressWarnings("unused")
	LogBean log = new LogBean(
		"INFO MessageBean.messageDelete: OK, user.id: " + user.getId()
			+ ", message.id: " + messageId);
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("requestDeleteSuccess"), null));
	return "/pages/protected/profile.xhtml";
    }

    public String getMessagerEmail(Integer userId) {
	User user = userFacade.getById(userId);
	if(user != null) {
	    return user.getEmail();
	}
	return "";
    }

    public String getMessagerFullname(Integer userId) {
	User user = userFacade.getById(userId);
	if(user != null) {
	    return user.getFullname();
	}
	return "";
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Message getMessage() {
	return message;
    }

    public void setMessage(Message message) {
	this.message = message;
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