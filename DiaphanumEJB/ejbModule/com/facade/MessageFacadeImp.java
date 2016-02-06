package com.facade;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.MessageDAO;
import com.model.Message;

@Stateless
public class MessageFacadeImp implements MessageFacade {

    @EJB
    private MessageDAO messageDAO;

    private static Calendar cal = Calendar.getInstance();

    public Message getById(Integer messageId) {
	return messageDAO.getById(messageId);
    }

    @Override
    public List<Message> getAll() {
	return messageDAO.getAll();
    }
    
    @Override
    public List<Message> getAllReversed() {
	return messageDAO.getAllReversed();
    }

    @Override
    public void save(Message message) {
	messageDAO.save(message);
    }

    @Override
    public void delete(Message message) {
	message.setIsActive(false);
	message.setEditedOn(cal.getTime());
	messageDAO.update(message);
    }

    @Override
    public Message update(Message message) {
	return messageDAO.update(message);
    }

    @Override
    public List<Message> getByCreator(Integer userId) {
	return messageDAO.getByCreator(userId);
    }

    @Override
    public Message getLast() {
	return messageDAO.getLast();
    }
}