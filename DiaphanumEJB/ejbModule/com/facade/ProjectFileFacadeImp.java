package com.facade;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.ProjectFileDAO;
import com.model.ProjectFile;

@Stateless
public class ProjectFileFacadeImp implements ProjectFileFacade {

	@EJB
	private ProjectFileDAO projectFileDAO;

	private static Calendar cal = Calendar.getInstance();
	
	public ProjectFile getById(Integer id) {
		return projectFileDAO.getById(id);
	}

	@Override
	public List<ProjectFile> getAll() {
		return projectFileDAO.getAll();
	}

	@Override
	public void save(ProjectFile projectFile) {
		projectFileDAO.save(projectFile);
	}

	@Override
	public void delete(ProjectFile ProjectFile) {
		ProjectFile.setIsActive(false);
		ProjectFile.setEditedOn(cal.getTime());
		projectFileDAO.update(ProjectFile);
	}

	@Override
	public ProjectFile update(ProjectFile projectFile) {
		return projectFileDAO.update(projectFile);
	}

	@Override
	public List<ProjectFile> getByProject(Integer projectId) {
		return projectFileDAO.getByProject(projectId);
	}

	@Override
	public ProjectFile getLast() {
		return projectFileDAO.getLast();
	}
}