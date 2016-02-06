package com.facade;

import java.util.List;

import javax.ejb.Local;

import com.model.Document;

@Local
public interface DocumentFacade {

    public Document getById(Integer documentId);

    public Document update(Document document);

    public List<Document> getAll();

    public List<Document> getAllReversed();

    public List<Document> getByUser(Integer userId);

    public void save(Document document);

    public void delete(Document document);

    public Document getLast();
}