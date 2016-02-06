package com.facade;

import java.util.List;

import javax.ejb.Local;

import com.model.DocumentType;

@Local
public interface DocumentTypeFacade {

	public DocumentType getById(Integer documentTypeId);

	public List<DocumentType> getAll();
}