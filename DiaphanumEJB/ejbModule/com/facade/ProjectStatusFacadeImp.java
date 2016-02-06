package com.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.ProjectStatusDAO;
import com.model.ProjectStatus;

@Stateless
public class ProjectStatusFacadeImp implements ProjectStatusFacade {

	@EJB
	private ProjectStatusDAO projectStatusDAO;

	public ProjectStatus getById(Integer projectStatusId) {
		return projectStatusDAO.getById(projectStatusId);
	}

	@Override
	public List<ProjectStatus> getAll() {
		return projectStatusDAO.getAll();
	}
}