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
import com.model.Project;

@Stateless
public class ProjectDAO extends GenericDAO<Project> {

    private final static String UNIT_NAME = "DiaphanumPU";

    @PersistenceContext(unitName = UNIT_NAME)
    private EntityManager em;

    public ProjectDAO() {
	super(Project.class);
    }

    public Project getById(Integer projectId) {
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("projectId", projectId);
	parameters.put("isActive", true);
	String query = "SELECT p FROM Project p WHERE p.id = :projectId AND p.isActive = :isActive";
	return super.findOneResult(query, parameters);
    }

    @SuppressWarnings("unchecked")
    public List<Project> getByStatus(Integer status) {
	List<Project> result = new ArrayList<Project>();
	try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("isActive", true);
	    parameters.put("status", status);
	    String namedQuery = "SELECT p FROM Project p WHERE u.isActive = :isActive AND u.status = :status";
	    Query query = em.createQuery(namedQuery);
	    if (parameters != null && !parameters.isEmpty()) {
		populateQueryParameters(query, parameters);
	    }
	    result = (List<Project>) query.getResultList();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR ProjectDAO.getByStatus: Error while running query: "
			    + e.getMessage());
	}
	return result;
    }

    @SuppressWarnings("unchecked")
    public List<Project> getAll() {
	List<Project> result = new ArrayList<Project>();
	try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("isActive", true);
	    String namedQuery = "SELECT p FROM Project p WHERE p.isActive = :isActive ORDER BY p.id ASC";
	    Query query = em.createQuery(namedQuery);
	    if (parameters != null && !parameters.isEmpty()) {
		populateQueryParameters(query, parameters);
	    }
	    result = (List<Project>) query.getResultList();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR ProjectDAO.getAll: Error while running query: "
			    + e.getMessage());
	}
	return result;
    }

    @SuppressWarnings("unchecked")
    public List<Project> getByUser(Integer userId) {
	List<Project> result = new ArrayList<Project>();
	try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("isActive", true);
	    parameters.put("createdBy", userId);
	    String namedQuery = "SELECT p FROM Project p WHERE p.isActive = :isActive AND p.createdBy = :createdBy ORDER BY p.id DESC";
	    Query query = em.createQuery(namedQuery);
	    if (parameters != null && !parameters.isEmpty()) {
		populateQueryParameters(query, parameters);
	    }
	    result = (List<Project>) query.getResultList();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR ProjectDAO.getAll: Error while running query: "
			    + e.getMessage());
	}
	return result;
    }

    @SuppressWarnings("unchecked")
    public List<Project> getAllReversed() {
	List<Project> result = new ArrayList<Project>();
	try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("isActive", true);
	    String namedQuery = "SELECT p FROM Project p WHERE p.isActive = :isActive ORDER BY p.id DESC";
	    Query query = em.createQuery(namedQuery);
	    if (parameters != null && !parameters.isEmpty()) {
		populateQueryParameters(query, parameters);
	    }
	    result = (List<Project>) query.getResultList();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR ProjectDAO.getAllReversed: Error while running query: "
			    + e.getMessage());
	}
	return result;
    }

    public Project getLast() {
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("isActive", true);
	String query = "SELECT p FROM Project p WHERE p.isActive = :isActive ORDER BY p.id DESC";
	Project result = (Project) super.findOneResult(query, parameters);
	return result;
    }
}