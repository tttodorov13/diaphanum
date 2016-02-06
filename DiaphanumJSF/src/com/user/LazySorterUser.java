package com.user;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import com.model.Project;
import com.model.User;

/**
 * @author ttt
 */
public class LazySorterUser implements Comparator<User> {
 
    private String sortField;
     
    private SortOrder sortOrder;
     
    public LazySorterUser(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
 
    public int compare(User user1, User user2) {
        try {
            Object value1 = Project.class.getField(this.sortField).get(user1);
            Object value2 = Project.class.getField(this.sortField).get(user2);
 
            @SuppressWarnings({ "unchecked", "rawtypes" })
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}