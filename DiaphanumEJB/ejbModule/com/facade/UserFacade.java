package com.facade;

import java.util.List;

import javax.ejb.Local;

import com.model.User;

@Local
public interface UserFacade {

	public User getById(Integer userId);

	public User getByUsername(String username);

	public User getByUsernamePassword(String username, String password);

	public User getByPhone(String phone);

	public User getByPhonePassword(String phone, String password);

	public User getByEmail(String email);

	public User getByEmailPassword(String email, String password);

	public List<User> findAllUserAdmin();

	public User update(User user);

	public List<User> getAll();

	public void save(User user);
	
	public User getLast();

	public User getPreviousById(Integer userId);

	public User getNextById(Integer userId);
}