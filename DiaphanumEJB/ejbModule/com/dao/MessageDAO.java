package com.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.model.Log;
import com.model.Message;

@Stateless
public class MessageDAO extends GenericDAO<Message> {

    private final static String UNIT_NAME = "DiaphanumPU";

    @PersistenceContext(unitName = UNIT_NAME)
    private EntityManager em;

    public MessageDAO() {
	super(Message.class);
    }

    public Message getById(Integer messageId) {
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("messageId", messageId);
	parameters.put("isActive", true);
	String query = "SELECT m FROM Message m WHERE m.id = :messageId AND m.isActive = :isActive";
	return super.findOneResult(query, parameters);
    }

    @SuppressWarnings("unchecked")
    public List<Message> getAll() {
	List<Message> result = new ArrayList<Message>();
	try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("isActive", true);
	    String namedQuery = "SELECT m FROM Message m WHERE m.isActive = :isActive ORDER BY m.id ASC";
	    Query query = em.createQuery(namedQuery);
	    if (parameters != null && !parameters.isEmpty()) {
		populateQueryParameters(query, parameters);
	    }
	    result = (List<Message>) query.getResultList();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR MessageDAO.getAll: Error while running query: "
			    + e.getMessage());
	}
	return result;
    }

    @SuppressWarnings("unchecked")
    public List<Message> getByCreator(Integer userId) {
	List<Message> result = new ArrayList<Message>();
	try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("isActive", true);
	    parameters.put("createdBy", userId);
	    String namedQuery = "SELECT m FROM Message m WHERE m.isActive = :isActive AND m.createdBy = :createdBy ORDER BY m.id DESC";
	    Query query = em.createQuery(namedQuery);
	    if (parameters != null && !parameters.isEmpty()) {
		populateQueryParameters(query, parameters);
	    }
	    result = (List<Message>) query.getResultList();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR MessageDAO.getByCreator: Error while running query: "
			    + e.getMessage());
	}
	return result;
    }

    @SuppressWarnings("unchecked")
    public List<Message> getAllReversed() {
	List<Message> result = new ArrayList<Message>();
	try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("isActive", true);
	    String namedQuery = "SELECT m FROM Message m WHERE m.isActive = :isActive ORDER BY m.id DESC";
	    Query query = em.createQuery(namedQuery);
	    if (parameters != null && !parameters.isEmpty()) {
		populateQueryParameters(query, parameters);
	    }
	    result = (List<Message>) query.getResultList();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR MessageDAO.getAllReversed: Error while running query: "
			    + e.getMessage());
	}
	return result;
    }

    public Message getLast() {
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("isActive", true);
	String query = "SELECT m FROM Message m WHERE m.isActive = :isActive ORDER BY m.id DESC";
	Message result = (Message) super.findOneResult(query, parameters);
	return result;
    }
}