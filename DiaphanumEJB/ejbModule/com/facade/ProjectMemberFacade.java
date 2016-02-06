package com.facade;

import java.util.List;

import javax.ejb.Local;

import com.model.ProjectMember;

@Local
public interface ProjectMemberFacade {

	public ProjectMember getById(Integer userId);

	public List<ProjectMember> getAll();

	public void save(ProjectMember projectMember);

	public void delete(ProjectMember projectMember);

	public ProjectMember update(ProjectMember projectMember);

	public List<ProjectMember> getByProject(Integer projectId);

	public List<ProjectMember> getByProjectWithoutOwner(Integer projectId);

	public ProjectMember getLast();
}