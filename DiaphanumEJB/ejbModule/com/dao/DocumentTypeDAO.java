package com.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.model.DocumentType;
import com.model.Log;

@Stateless
public class DocumentTypeDAO extends GenericDAO<DocumentType> {

	private final static String UNIT_NAME = "DiaphanumPU";

	@PersistenceContext(unitName = UNIT_NAME)
	private EntityManager em;

	public DocumentTypeDAO() {
		super(DocumentType.class);
	}

	public DocumentType getById(Integer documentTypeId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("documentTypeId", documentTypeId);
		parameters.put("isActive", true);
		String query = "SELECT dt FROM DocumentType dt WHERE dt.id = :documentTypeId AND dt.isActive = :isActive";
		return super.findOneResult(query, parameters);
	}

	@SuppressWarnings("unchecked")
	public List<DocumentType> getAll() {
		List<DocumentType> result = new ArrayList<DocumentType>();
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("isActive", true);
			String namedQuery = "SELECT dt FROM DocumentType dt WHERE dt.isActive = :isActive ORDER BY dt.id";
			Query query = em.createQuery(namedQuery);
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}
			result = (List<DocumentType>) query.getResultList();
		} catch (Exception e) {
			@SuppressWarnings("unused")
			Log log = new Log(" ERROR DocumentTypeDAO.getAll: Error while running query: "
					+ e.getMessage());
		}
		return result;
	}
}