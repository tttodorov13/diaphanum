package com.facade;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.ProjectMemberDAO;
import com.model.ProjectMember;

@Stateless
public class ProjectMemberFacadeImp implements ProjectMemberFacade {

	@EJB
	private ProjectMemberDAO projectMemberDAO;

	private static Calendar cal = Calendar.getInstance();
	
	public ProjectMember getById(Integer id) {
		return projectMemberDAO.getById(id);
	}

	@Override
	public List<ProjectMember> getAll() {
		return projectMemberDAO.getAll();
	}

	@Override
	public void save(ProjectMember projectMember) {
		projectMemberDAO.save(projectMember);
	}

	@Override
	public void delete(ProjectMember projectMember) {
		projectMember.setIsActive(false);
		projectMember.setEditedOn(cal.getTime());
		projectMemberDAO.update(projectMember);
	}

	@Override
	public ProjectMember update(ProjectMember projectMember) {
		return projectMemberDAO.update(projectMember);
	}

	@Override
	public List<ProjectMember> getByProject(Integer projectId) {
		return projectMemberDAO.getByProject(projectId);
	}

	@Override
	public List<ProjectMember> getByProjectWithoutOwner(Integer projectId) {
		return projectMemberDAO.getByProjectWithoutOwner(projectId);
	}
	
	@Override
	public ProjectMember getLast() {
		return projectMemberDAO.getLast();
	}
}