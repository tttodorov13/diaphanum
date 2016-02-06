package com.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import com.model.Log;

public class GenericDAO<T> {
    private final static String UNIT_NAME = "DiaphanumPU";

    @PersistenceContext(unitName = UNIT_NAME)
    private EntityManager em;

    private Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
	this.entityClass = entityClass;
    }

    public void save(T entity) {
	em.persist(entity);
	em.flush();
    }

    protected void delete(Object id, Class<T> classe) {
	T entityToBeRemoved = em.getReference(classe, id);
	em.remove(entityToBeRemoved);
    }

    public T update(T entity) {
	return em.merge(entity);
    }

    public T find(int entityID) {
	return em.find(entityClass, entityID);
    }

    // Using the unchecked because JPA does not have a
    // em.getCriteriaBuilder().createQuery()<T> method
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<T> findAll() {
	CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
	cq.select(cq.from(entityClass));
	return em.createQuery(cq).getResultList();
    }

    // Using the unchecked because JPA does not have a
    // query.getSingleResult()<T> method
    @SuppressWarnings("unchecked")
    protected T findOneResult(String namedQuery, Map<String, Object> parameters) {
	T result = null;
	try {
	    Query query = em.createQuery(namedQuery);
	    // Method that will populate parameters if they are passed not null
	    // and empty
	    if (parameters != null && !parameters.isEmpty()) {
		populateQueryParameters(query, parameters);
	    }
	    List<T> results = query.getResultList();
	    if (!results.isEmpty()) {
		result = (T) results.get(0);
	    } else {
		@SuppressWarnings("unused")
		Log log = new Log("ERROR GenericDAO: No records found in db");
	    }
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    Log log = new Log("ERROR GenericDAO: Error while running query: "
		    + e.getMessage());
	}
	return result;
    }

    protected void populateQueryParameters(Query query,
	    Map<String, Object> parameters) {

	for (Entry<String, Object> entry : parameters.entrySet()) {
	    query.setParameter(entry.getKey(), entry.getValue());
	}
    }
}