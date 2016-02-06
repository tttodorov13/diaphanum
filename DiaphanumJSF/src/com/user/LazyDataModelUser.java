package com.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.model.User;

public class LazyDataModelUser extends LazyDataModel<User> {

    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;

    private List<User> datasource;

    public LazyDataModelUser(List<User> datasource) {
	this.datasource = datasource;
    }

    @Override
    public User getRowData(String rowKey) {
	for (User user : datasource) {
	    if (user.getId().equals(rowKey))
		return user;
	}
	return null;
    }

    @Override
    public Object getRowKey(User user) {
	return user.getId();
    }

    @Override
    public List<User> load(int first, int pageSize, String sortField,
	    SortOrder sortOrder, Map<String, Object> filters) {
	List<User> data = new ArrayList<User>();

	for (User user : datasource) {
	    boolean match = true;
	    if (filters != null) {
		for (Iterator<String> it = filters.keySet().iterator(); it
			.hasNext();) {
		    try {
			String filterProperty = it.next();
			Object filterValue = filters.get(filterProperty);
			String fieldValue = String.valueOf(user.getClass()
				.getField(filterProperty).get(user));
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
		data.add(user);
	    }
	}

	// sort
	if (sortField != null) {
	    Collections.sort(data, new LazySorterUser(sortField, sortOrder));
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