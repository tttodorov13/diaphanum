package com.facade;

import java.util.List;

import javax.ejb.Local;

import com.model.ProjectStatus;

@Local
public interface ProjectStatusFacade {

	public ProjectStatus getById(Integer projectStatusId);

	public List<ProjectStatus> getAll();
}