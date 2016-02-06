package com;

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

import org.primefaces.model.LazyDataModel;

import com.facade.MessageFacade;
import com.model.Message;
import com.model.User;
import com.user.LazyDataModelMessage;

@RequestScoped
@ManagedBean(name = "feedback", eager = true)
public class FeedbackBean implements Serializable {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private User user;
    private LazyDataModel<Message> lazyModelMessage;
    private Message selectedMessage;
    private Message message;
    private static Calendar cal = Calendar.getInstance();
    private Integer messageId;
    private String messageReply;

    private FacesContext context = FacesContext.getCurrentInstance();
    private ExternalContext externalContext = context.getExternalContext();
    private ResourceBundle bundle = context.getApplication().getResourceBundle(
	    context, "msgs");

    @EJB
    private MessageFacade messageFacade;

    @PostConstruct
    public void init() {
	setLazyModelMessage(new LazyDataModelMessage(
		messageFacade.getAllReversed()));
	message = new Message();
	try {
	    user = (User) externalContext.getSessionMap().get("user");
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean(
		    "INFO FeedbackBean.init: can't get user from session: "
			    + e.getMessage());
	}
	if (user == null) {
	    user = new User();
	} else {
	    message.setCreatedBy(user.getId());
	    message.setUserFullname(user.getFullname());
	    message.setUserEmail(user.getEmail());
	}
    }

    public String messageCreate() {
	message.setIsActive(true);
	message.setCreatedOn(cal.getTime());
	message.setEditedOn(cal.getTime());
	messageFacade.save(message);
	Message messageNew = messageFacade.getLast();
	@SuppressWarnings("unused")
	LogBean log = new LogBean(
		"INFO FeedbackBean.messageCreate: OK message.id: "
			+ messageNew.getId());
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		bundle.getString("requestSendSuccess"), null));
	return "/pages/protected/profile.xhtml?faces-redirect=true";
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

    public String getMessageReply() {
	return messageReply;
    }

    public String getMessageReply(Integer messageId) {
	Message message = new Message();
	message = messageFacade.getById(messageId);
	return message.getReply();
    }

    public void setMessageReply(String messageReply) {
	this.messageReply = messageReply;
    }

    public Integer getMessageId() {
	return messageId;
    }

    public void setMessageId(Integer messageId) {
	this.messageId = messageId;
    }
}