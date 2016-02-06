package com.facade;

import java.util.List;

import javax.ejb.Local;

import com.model.Message;

@Local
public interface MessageFacade {

    public Message getById(Integer messageId);

    public Message update(Message message);

    public List<Message> getAll();

    public List<Message> getAllReversed();

    public List<Message> getByCreator(Integer userId);

    public void save(Message message);

    public void delete(Message message);

    public Message getLast();
}