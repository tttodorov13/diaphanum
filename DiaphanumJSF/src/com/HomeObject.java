package com;

public class HomeObject {

    private String name;
    private Integer type;

    public HomeObject() {
	setName("name");
	setType(0);
    }
    
    public HomeObject(String name) {
	setName(name);
	setType(0);
    }
    
    public HomeObject(String name, Integer type) {
	setName(name);
	setType(type);
    }
    
    public HomeObject(HomeObject homeObject) {
	setName(homeObject.getName());
	setType(homeObject.getType());
    }
    
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }
}