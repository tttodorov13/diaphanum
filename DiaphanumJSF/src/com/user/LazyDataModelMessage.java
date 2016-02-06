package com.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.model.Message;

/**
 * @author ttt
 */
public class LazyDataModelMessage extends LazyDataModel<Message> {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private List<Message> datasource;

    public LazyDataModelMessage(List<Message> datasource) {
	this.datasource = datasource;
    }

    @Override
    public Message getRowData(String rowKey) {
	for (Message message : datasource) {
	    if (message.getId().equals(rowKey))
		return message;
	}
	return null;
    }

    @Override
    public Object getRowKey(Message message) {
	return message.getId();
    }

    @Override
    public List<Message> load(int first, int pageSize, String sortField,
	    SortOrder sortOrder, Map<String, Object> filters) {
	List<Message> data = new ArrayList<Message>();

	for (Message message : datasource) {
	    boolean match = true;
	    if (filters != null) {
		for (Iterator<String> it = filters.keySet().iterator(); it
			.hasNext();) {
		    try {
			String filterProperty = it.next();
			Object filterValue = filters.get(filterProperty);
			String fieldValue = String.valueOf(message.getClass()
				.getField(filterProperty).get(message));
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
		data.add(message);
	    }
	}

	// sort
	if (sortField != null) {
	    Collections.sort(data, new LazySorterMessage(sortField, sortOrder));
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