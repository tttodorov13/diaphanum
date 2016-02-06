package com.facade;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.DocumentDAO;
import com.model.Document;

@Stateless
public class DocumentFacadeImp implements DocumentFacade {

    @EJB
    private DocumentDAO documentDAO;

    private static Calendar cal = Calendar.getInstance();

    public Document getById(Integer projectId) {
	return documentDAO.getById(projectId);
    }

    @Override
    public List<Document> getAll() {
	return documentDAO.getAll();
    }
    
    @Override
    public List<Document> getAllReversed() {
	return documentDAO.getAllReversed();
    }

    @Override
    public void save(Document project) {
	documentDAO.save(project);
    }

    @Override
    public void delete(Document project) {
	project.setIsActive(false);
	project.setEditedOn(cal.getTime());
	documentDAO.update(project);
    }

    @Override
    public Document update(Document project) {
	return documentDAO.update(project);
    }

    @Override
    public List<Document> getByUser(Integer userId) {
	return documentDAO.getByUser(userId);
    }

    @Override
    public Document getLast() {
	return documentDAO.getLast();
    }
}