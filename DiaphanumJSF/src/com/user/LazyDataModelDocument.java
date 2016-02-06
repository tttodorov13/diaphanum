package com.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.model.Document;

public class LazyDataModelDocument extends LazyDataModel<Document> {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private List<Document> datasource;

    public LazyDataModelDocument(List<Document> datasource) {
	this.datasource = datasource;
    }

    @Override
    public Document getRowData(String rowKey) {
	for (Document document : datasource) {
	    if (document.getId().equals(rowKey))
		return document;
	}
	return null;
    }

    @Override
    public Object getRowKey(Document document) {
	return document.getId();
    }

    @Override
    public List<Document> load(int first, int pageSize, String sortField,
	    SortOrder sortOrder, Map<String, Object> filters) {
	List<Document> data = new ArrayList<Document>();

	for (Document document : datasource) {
	    boolean match = true;
	    if (filters != null) {
		for (Iterator<String> it = filters.keySet().iterator(); it
			.hasNext();) {
		    try {
			String filterProperty = it.next();
			Object filterValue = filters.get(filterProperty);
			String fieldValue = String.valueOf(document.getClass()
				.getField(filterProperty).get(document));
			if (filterValue == null
				|| fieldValue
					.startsWith(filterValue.toString())) {
			    match = true;
			} else {
			    match = false;
			    break;
			}
		    } catch (Exception e) {
			match = false;
		    }
		}
	    }
	    if (match) {
		data.add(document);
	    }
	}

	// sort
	if (sortField != null) {
	    Collections.sort(data, new LazySorterDocument(sortField, sortOrder));
	}

	// rowCount
	int dataSize = data.size();
	this.setRowCount(dataSize);

	// paginate
	if (dataSize > pageSize) {
	    try {
		return data.subList(first, first + pageSize);
	    } catch (IndexOutOfBoundsException e) {
		return data.subList(first, first + (dataSize % pageSize));
	    }
	} else {
	    return data;
	}
    }
}