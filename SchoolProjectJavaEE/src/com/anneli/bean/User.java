package com.anneli.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

/**
 * POJO represents table Login from database
 * 
 * @author Anneli
 * @version 1.0
 * @since 2017-12-13
 */
@ManagedBean
public class User implements Serializable {

	private static final long serialVersionUID = 7342006815638959745L;

	private int id;
	private String username;
	private String password;

	public User() {

	}

	public User(int id, String username, String password) {

		this.id = id;
		this.username = username;
		this.password = password;

	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
	}

}
