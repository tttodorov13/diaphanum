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
import com.model.ProjectFile;

@Stateless
public class ProjectFileDAO extends GenericDAO<ProjectFile> {

	private final static String UNIT_NAME = "DiaphanumPU";

	@PersistenceContext(unitName = UNIT_NAME)
	private EntityManager em;

	public ProjectFileDAO() {
		super(ProjectFile.class);
	}

	public ProjectFile getById(Integer id) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", id);
		parameters.put("isActive", true);
		String query = "SELECT u FROM ProjectFile u WHERE u.id = :id AND u.isActive = :isActive";
		return super.findOneResult(query, parameters);
	}

	@SuppressWarnings("unchecked")
	public List<ProjectFile> getByProject(Integer projectId) {
		List<ProjectFile> result = new ArrayList<ProjectFile>();
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("projectId", projectId);
			parameters.put("isActive", true);
			String namedQuery = "SELECT u FROM ProjectFile u WHERE u.project = :projectId AND u.isActive = :isActive";
			Query query = em.createQuery(namedQuery);
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}
			result = (List<ProjectFile>) query.getResultList();
		} catch (Exception e) {
			@SuppressWarnings("unused")
			Log log = new Log(
					" ERROR ProjectFileDAO.getByProject: Error while running query: "
							+ e.getMessage());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<ProjectFile> getAll() {
		List<ProjectFile> result = new ArrayList<ProjectFile>();
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("isActive", true);
			String namedQuery = "SELECT u FROM ProjectFile u WHERE u.isActive = :isActive ORDER BY u.id";
			Query query = em.createQuery(namedQuery);
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}
			result = (List<ProjectFile>) query.getResultList();
		} catch (Exception e) {
			@SuppressWarnings("unused")
			Log log = new Log(
					" ERROR ProjectFileDAO.getAll: Error while running query: "
							+ e.getMessage());
		}
		return result;
	}

	public ProjectFile getLast() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("isActive", true);
		String query = "SELECT u FROM ProjectFile u WHERE u.isActive = :isActive ORDER BY u.id DESC";
		ProjectFile result = (ProjectFile) super.findOneResult(query,
				parameters);
		return result;
	}
}