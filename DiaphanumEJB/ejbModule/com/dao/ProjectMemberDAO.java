package com.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.model.Log;
import com.model.ProjectMember;

@Stateless
public class ProjectMemberDAO extends GenericDAO<ProjectMember>
{

    private final static String UNIT_NAME = "DiaphanumPU";

    @PersistenceContext(unitName = UNIT_NAME)
    private EntityManager em;

    public ProjectMemberDAO()
    {
        super(ProjectMember.class);
    }

    public ProjectMember getById(Integer id)
    {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", id);
        parameters.put("isActive", true);
        String query = "SELECT pm FROM ProjectMember pm WHERE pm.id = :id AND pm.isActive = :isActive";
        return super.findOneResult(query, parameters);
    }

    @SuppressWarnings("unchecked")
    public List<ProjectMember> getByProject(Integer projectId)
    {
        List<ProjectMember> result = new ArrayList<ProjectMember>();
        try
        {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("projectId", projectId);
            parameters.put("isActive", true);
            String namedQuery = "SELECT pm FROM ProjectMember pm WHERE pm.project = :projectId AND pm.isActive = :isActive";
            Query query = em.createQuery(namedQuery);
            if (parameters != null && !parameters.isEmpty())
            {
                populateQueryParameters(query, parameters);
            }
            result = (List<ProjectMember>) query.getResultList();
        }
        catch (Exception e)
        {
            @SuppressWarnings("unused")
            Log log = new Log(" ERROR ProjectMemberDAO.getByProject: Error while running query: "
                    + e.getMessage());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<ProjectMember> getByProjectWithoutOwner(Integer projectId)
    {
        List<ProjectMember> result = new ArrayList<ProjectMember>();
        try
        {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("projectId", projectId);
            parameters.put("isActive", true);
            String namedQuery = "SELECT pm FROM ProjectMember pm WHERE pm.userId IS NULL AND pm.project = :projectId AND pm.isActive = :isActive";
            Query query = em.createQuery(namedQuery);
            if (parameters != null && !parameters.isEmpty())
            {
                populateQueryParameters(query, parameters);
            }
            result = (List<ProjectMember>) query.getResultList();
        }
        catch (Exception e)
        {
            @SuppressWarnings("unused")
            Log log = new Log(" ERROR ProjectMemberDAO.getByProjectWithoutOwner: Error while running query: "
                    + e.getMessage());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<ProjectMember> getAll()
    {
        List<ProjectMember> result = new ArrayList<ProjectMember>();
        try
        {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("isActive", true);
            String namedQuery = "SELECT pm FROM ProjectMember pm WHERE pm.isActive = :isActive ORDER BY pm.id";
            Query query = em.createQuery(namedQuery);
            if (parameters != null && !parameters.isEmpty())
            {
                populateQueryParameters(query, parameters);
            }
            result = (List<ProjectMember>) query.getResultList();
        }
        catch (Exception e)
        {
            @SuppressWarnings("unused")
            Log log = new Log(" ERROR ProjectMemberDAO.getAll: Error while running query: "
                    + e.getMessage());
        }
        return result;
    }

    public ProjectMember getLast()
    {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("isActive", true);
        String query = "SELECT pm FROM ProjectMember pm WHERE pm.isActive = :isActive ORDER BY pm.id DESC";
        ProjectMember result = (ProjectMember) super.findOneResult(query, parameters);
        return result;
    }
}
