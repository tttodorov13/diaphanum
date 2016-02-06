package com.facade;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.ProjectDAO;
import com.model.Project;

@Stateless
public class ProjectFacadeImp implements ProjectFacade {

    @EJB
    private ProjectDAO projectDAO;

    private static Calendar cal = Calendar.getInstance();

    public Project getById(Integer projectId) {
	return projectDAO.getById(projectId);
    }

    @Override
    public List<Project> getAll() {
	return projectDAO.getAll();
    }

    @Override
    public List<Project> getAllReversed() {
	return projectDAO.getAllReversed();
    }

    @Override
    public void save(Project project) {
	projectDAO.save(project);
    }

    @Override
    public void delete(Project project) {
	project.setIsActive(false);
	project.setEditedOn(cal.getTime());
	projectDAO.update(project);
    }

    @Override
    public Project update(Project project) {
	return projectDAO.update(project);
    }

    @Override
    public Project getLast() {
	return projectDAO.getLast();
    }

    @Override
    public List<Project> getByUser(Integer userId) {
	return projectDAO.getByUser(userId);
    }

}