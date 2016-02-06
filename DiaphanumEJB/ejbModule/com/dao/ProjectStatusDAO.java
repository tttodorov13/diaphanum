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
import com.model.ProjectStatus;

@Stateless
public class ProjectStatusDAO extends GenericDAO<ProjectStatus> {

	private final static String UNIT_NAME = "DiaphanumPU";

	@PersistenceContext(unitName = UNIT_NAME)
	private EntityManager em;

	public ProjectStatusDAO() {
		super(ProjectStatus.class);
	}

	public ProjectStatus getById(Integer projectStatusId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("projectStatusId", projectStatusId);
		parameters.put("isActive", true);
		String query = "SELECT ps FROM ProjectStatus ps WHERE ps.id = :projectStatusId AND ps.isActive = :isActive";
		return super.findOneResult(query, parameters);
	}

	@SuppressWarnings("unchecked")
	public List<ProjectStatus> getAll() {
		List<ProjectStatus> result = new ArrayList<ProjectStatus>();
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("isActive", true);
			String namedQuery = "SELECT ps FROM ProjectStatus ps WHERE ps.isActive = :isActive ORDER BY ps.id";
			Query query = em.createQuery(namedQuery);
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}
			result = (List<ProjectStatus>) query.getResultList();
		} catch (Exception e) {
			@SuppressWarnings("unused")
			Log log = new Log(" ERROR ProjectStatusDAO.getAll: Error while running query: "
					+ e.getMessage());
		}
		return result;
	}
}