package com.user;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import com.model.Project;

/**
 * @author ttt
 */
public class LazySorterProject implements Comparator<Project> {
 
    private String sortField;
     
    private SortOrder sortOrder;
     
    public LazySorterProject(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
 
    public int compare(Project project1, Project project2) {
        try {
            Object value1 = Project.class.getField(this.sortField).get(project1);
            Object value2 = Project.class.getField(this.sortField).get(project2);
 
            @SuppressWarnings({ "unchecked", "rawtypes" })
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}