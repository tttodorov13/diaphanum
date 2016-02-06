package com.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.model.Document;
import com.model.Log;

@Stateless
public class DocumentDAO extends GenericDAO<Document> {

    private final static String UNIT_NAME = "DiaphanumPU";

    @PersistenceContext(unitName = UNIT_NAME)
    private EntityManager em;

    public DocumentDAO() {
	super(Document.class);
    }

    public Document getById(Integer documentId) {
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("documentId", documentId);
	parameters.put("isActive", true);
	String query = "SELECT d FROM Document d WHERE d.id = :documentId AND d.isActive = :isActive";
	return super.findOneResult(query, parameters);
    }

    @SuppressWarnings("unchecked")
    public List<Document> getByType(Integer type) {
	List<Document> result = new ArrayList<Document>();
	try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("isActive", true);
	    parameters.put("type", type);
	    String namedQuery = "SELECT d FROM Document d WHERE d.isActive = :isActive AND d.type = :type";
	    Query query = em.createQuery(namedQuery);
	    if (parameters != null && !parameters.isEmpty()) {
		populateQueryParameters(query, parameters);
	    }
	    result = (List<Document>) query.getResultList();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR DocumentDAO.getByType: Error while running query: "
			    + e.getMessage());
	}
	return result;
    }

    @SuppressWarnings("unchecked")
    public List<Document> getAll() {
	List<Document> result = new ArrayList<Document>();
	try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("isActive", true);
	    String namedQuery = "SELECT d FROM Document d WHERE d.isActive = :isActive ORDER BY d.id ASC";
	    Query query = em.createQuery(namedQuery);
	    if (parameters != null && !parameters.isEmpty()) {
		populateQueryParameters(query, parameters);
	    }
	    result = (List<Document>) query.getResultList();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR DocumentDAO.getAll: Error while running query: "
			    + e.getMessage());
	}
	return result;
    }

    @SuppressWarnings("unchecked")
    public List<Document> getByUser(Integer userId) {
	List<Document> result = new ArrayList<Document>();
	try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("isActive", true);
	    parameters.put("createdBy", userId);
	    String namedQuery = "SELECT d FROM Document d WHERE d.isActive = :isActive AND d.createdBy = :createdBy ORDER BY d.id DESC";
	    Query query = em.createQuery(namedQuery);
	    if (parameters != null && !parameters.isEmpty()) {
		populateQueryParameters(query, parameters);
	    }
	    result = (List<Document>) query.getResultList();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR DocumentDAO.getAll: Error while running query: "
			    + e.getMessage());
	}
	return result;
    }

    @SuppressWarnings("unchecked")
    public List<Document> getAllReversed() {
	List<Document> result = new ArrayList<Document>();
	try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("isActive", true);
	    String namedQuery = "SELECT d FROM Document d WHERE d.isActive = :isActive ORDER BY d.id DESC";
	    Query query = em.createQuery(namedQuery);
	    if (parameters != null && !parameters.isEmpty()) {
		populateQueryParameters(query, parameters);
	    }
	    result = (List<Document>) query.getResultList();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR DocumentDAO.getAllReversed: Error while running query: "
			    + e.getMessage());
	}
	return result;
    }

    public Document getLast() {
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("isActive", true);
	String query = "SELECT d FROM Document d WHERE d.isActive = :isActive ORDER BY d.id DESC";
	Document result = (Document) super.findOneResult(query, parameters);
	return result;
    }
}