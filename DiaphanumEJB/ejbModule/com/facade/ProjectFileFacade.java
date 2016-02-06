package com.facade;

import java.util.List;

import javax.ejb.Local;

import com.model.ProjectFile;

@Local
public interface ProjectFileFacade {

	public ProjectFile getById(Integer userId);

	public List<ProjectFile> getAll();
	
	public void save(ProjectFile projectFile);

	public void delete(ProjectFile projectFile);

	public ProjectFile update(ProjectFile projectFile);
	
	public List<ProjectFile> getByProject(Integer projectId);

	public ProjectFile getLast();
}