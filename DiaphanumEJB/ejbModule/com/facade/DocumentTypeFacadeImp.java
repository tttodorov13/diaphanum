package com.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.DocumentTypeDAO;
import com.model.DocumentType;

@Stateless
public class DocumentTypeFacadeImp implements DocumentTypeFacade {

	@EJB
	private DocumentTypeDAO DocumentTypeDAO;

	public DocumentType getById(Integer DocumentTypeId) {
		return DocumentTypeDAO.getById(DocumentTypeId);
	}

	@Override
	public List<DocumentType> getAll() {
		return DocumentTypeDAO.getAll();
	}
}