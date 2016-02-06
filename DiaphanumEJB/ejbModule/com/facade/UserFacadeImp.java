package com.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.UserDAO;
import com.model.User;

@Stateless
public class UserFacadeImp implements UserFacade {

	@EJB
	private UserDAO userDAO;

	public User getByUsername(String username) {
		return userDAO.getByUsername(username);
	}

	public User getById(Integer userId) {
		return userDAO.getById(userId);
	}

	public User getByUsernamePassword(String username, String password) {
		return userDAO.getByUsernamePassword(username, password);
	}

	public User getByPhone(String phone) {
		return userDAO.getByPhone(phone);
	}

	public User getByPhonePassword(String phone, String password) {
		return userDAO.getByPhonePassword(phone, password);
	}

	public User getByEmail(String email) {
		return userDAO.getByEmail(email);
	}
	
	public User getByEmailPassword(String email, String password) {
		return userDAO.getByEmailPassword(email, password);
	}

	public List<User> findAllUserAdmin() {
		return userDAO.getAllUserAdmin();
	}

	@Override
	public User update(User user) {
		return userDAO.update(user);
	}

	@Override
	public List<User> getAll() {
		return userDAO.getAll();
	}

	@Override
	public void save(User user) {
		userDAO.save(user);
	}

	@Override
	public User getLast() {
		return userDAO.getLast();
	}

	public User getPreviousById(Integer userId) {
		return userDAO.getPreviousById(userId);
	}

	public User getNextById(Integer userId) {
		return userDAO.getNextById(userId);
	}
}