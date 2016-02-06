package com.user;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import com.model.Document;


/**
 * @author ttt
 */
public class LazySorterDocument implements Comparator<Document> {

    private String sortField;

    private SortOrder sortOrder;

    public LazySorterDocument(String sortField, SortOrder sortOrder) {
	this.sortField = sortField;
	this.sortOrder = sortOrder;
    }

    public int compare(Document document1, Document document2) {
	try {
	    Object value1 = Document.class.getField(this.sortField).get(
		    document1);
	    Object value2 = Document.class.getField(this.sortField).get(
		    document2);

	    @SuppressWarnings({ "unchecked", "rawtypes" })
	    int value = ((Comparable) value1).compareTo(value2);

	    return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
	} catch (Exception e) {
	    throw new RuntimeException();
	}
    }
}