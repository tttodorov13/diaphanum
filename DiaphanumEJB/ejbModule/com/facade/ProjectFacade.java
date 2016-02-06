package com.facade;

import java.util.List;

import javax.ejb.Local;

import com.model.Project;

@Local
public interface ProjectFacade {

    public Project getById(Integer projectId);

    public Project update(Project project);

    public List<Project> getAll();

    public List<Project> getAllReversed();
    
    public List<Project> getByUser(Integer userId);

    public void save(Project project);

    public void delete(Project project);

    public Project getLast();
}