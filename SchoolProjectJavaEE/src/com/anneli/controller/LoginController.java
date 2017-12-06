package com.anneli.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.anneli.db.Login;
import com.anneli.user.User;

@ManagedBean
@SessionScoped
public class LoginController implements Serializable {

	private Login login;
	
	private static final long serialVersionUID = 7407965200910596687L;


	public LoginController() {

	}


	public String validateUser(String user, String password) {

		try {
			boolean valid = new Login().checkLogin(user, password);

			if (valid) {

				return "database?faces-redirect=true";
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			addErrorMessage(ex);

		}

		return "login-form?faces-redirect=true";

	}
	
	public String registerNewUser(User theUser) {
			
		try {
			
			login = new Login();
			
			login.addNewUser(theUser);
			
		} catch (Exception ex) {

			addErrorMessage(ex);
		
		}
		return "login-form?faces-redirect=true";
	}
	
	public String logout(User theUser) {
		boolean isLoggedIn = false;
		if(!isLoggedIn) {
			
			return "login-form?faces-redirect=true";
		}
		return "database?faces-redirect=true";
	}

	private void addErrorMessage(Exception ex) {
		FacesMessage message = new FacesMessage("Error" + ex.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

}
