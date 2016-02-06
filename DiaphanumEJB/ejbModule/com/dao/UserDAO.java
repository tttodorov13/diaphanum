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
import com.model.User;

@Stateless
public class UserDAO extends GenericDAO<User> {

    private final static String UNIT_NAME = "DiaphanumPU";

    @PersistenceContext(unitName = UNIT_NAME)
    private EntityManager em;

    public UserDAO() {
	super(User.class);
    }

    public User getById(Integer userId) {
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("userId", userId);
	String query = "SELECT u FROM User u WHERE u.id = :userId";
	return super.findOneResult(query, parameters);
    }

    public User getByUsername(String username) {
	User u = new User();
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("username", username);
	parameters.put("isActive", true);
	String query = "SELECT u FROM User u WHERE u.isActive = :isActive AND u.username = :username";
	try {
	    u = super.findOneResult(query, parameters);
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR UserDAO.getByUsername: Error while running query: "
			    + e.getMessage());
	}
	return u;
    }

    public User getByEmail(String email) {
	User u = new User();
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("email", email);
	parameters.put("isActive", true);
	String query = "SELECT u FROM User u WHERE u.isActive = :isActive AND u.email = :email";
	try {
	    u = super.findOneResult(query, parameters);
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR UserDAO.getByUsername: Error while running query: "
			    + e.getMessage());
	}
	return u;
    }

    public User getByPhone(String phone) {
	User u = new User();
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("phone", phone);
	parameters.put("isActive", true);
	String query = "SELECT u FROM User u WHERE u.isActive = :isActive AND u.phone = :phone";
	try {
	    u = super.findOneResult(query, parameters);
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR UserDAO.getByUsername: Error while running query: "
			    + e.getMessage());
	}
	return u;
    }

    public User getByUsernamePassword(String username, String password) {
	User u = new User();
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("username", username);
	parameters.put("password", password);
	parameters.put("isActive", true);
	String query = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password AND u.isActive = :isActive";
	try {
	    u = super.findOneResult(query, parameters);
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR UserDAO.getByUsernamePassword: Error while running query: "
			    + e.getMessage());
	}
	return u;
    }

    public User getByPhonePassword(String phone, String password) {
	User u = new User();
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("phone", phone);
	parameters.put("password", password);
	parameters.put("isActive", true);
	String query = "SELECT u FROM User u WHERE u.phone = :phone AND u.password = :password AND u.isActive = :isActive";
	try {
	    u = super.findOneResult(query, parameters);
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR UserDAO.getByPhonePassword: Error while running query: "
			    + e.getMessage());
	}
	return u;
    }

    public User getByEmailPassword(String email, String password) {
	User u = new User();
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("email", email);
	parameters.put("password", password);
	parameters.put("isActive", true);
	String query = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password AND u.isActive = :isActive";
	try {
	    u = super.findOneResult(query, parameters);
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR UserDAO.getByEmailPassword: Error while running query: "
			    + e.getMessage());
	}
	return u;
    }

    @SuppressWarnings("unchecked")
    public List<User> getAllUserAdmin() {
	List<User> result = new ArrayList<User>();
	try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("isAdmin", true);
	    parameters.put("isActive", true);
	    String namedQuery = "SELECT u FROM User u WHERE u.isActive = :isActive AND u.isAdmin = :isAdmin";
	    Query query = em.createQuery(namedQuery);
	    if (parameters != null && !parameters.isEmpty()) {
		populateQueryParameters(query, parameters);
	    }
	    result = (List<User>) query.getResultList();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR UserDAO.getAllUserAdmin: Error while running query: "
			    + e.getMessage());
	}
	return result;
    }

    public User getLast() {
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("isActive", true);
	String query = "SELECT u FROM User u WHERE u.isActive = :isActive ORDER BY u.id DESC";
	User result = (User) super.findOneResult(query, parameters);
	return result;
    }

    public User getNextById(Integer userId) {
	User user = new User();
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("userId", userId);
	parameters.put("isActive", true);
	parameters.put("rolePhd", true);
	try {
	    String query = "SELECT u FROM User u WHERE u.isActive = :isActive AND u.id > :userId";
	    user = super.findOneResult(query, parameters);
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR UserDAO.getNextById: Error while running query: "
			    + e.getMessage());
	}
	return user;
    }

    public User getPreviousById(Integer userId) {
	User user = new User();
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("userId", userId);
	parameters.put("isActive", true);
	try {
	    String query = "SELECT u FROM User u WHERE u.isActive = :isActive AND u.id < :userId";
	    user = super.findOneResult(query, parameters);
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR UserDAO.getPreviousById: Error while running query: "
			    + e.getMessage());
	}
	return user;
    }

    @SuppressWarnings("unchecked")
    public List<User> getAll() {
	List<User> result = new ArrayList<User>();
	try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("isActive", true);
	    String namedQuery = "SELECT u FROM User u WHERE u.isActive = :isActive ORDER BY u.id DESC";
	    Query query = em.createQuery(namedQuery);
	    if (parameters != null && !parameters.isEmpty()) {
		populateQueryParameters(query, parameters);
	    }
	    result = (List<User>) query.getResultList();
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log(
		    " ERROR UserDAO.getAll: Error while running query: "
			    + e.getMessage());
	}
	return result;
    }
}