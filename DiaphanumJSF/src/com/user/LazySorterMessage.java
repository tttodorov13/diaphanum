package com.user;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import com.model.Message;

/**
 * @author ttt
 */
public class LazySorterMessage implements Comparator<Message> {
 
    private String sortField;
     
    private SortOrder sortOrder;
     
    public LazySorterMessage(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
 
    public int compare(Message message1, Message message2) {
        try {
            Object value1 = Message.class.getField(this.sortField).get(message1);
            Object value2 = Message.class.getField(this.sortField).get(message2);
 
            @SuppressWarnings({ "unchecked", "rawtypes" })
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}