package com.anneli.controller;

import java.io.Serializable;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
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


	public String validateUser(String username, String password) {
		
		try {
			boolean valid = new Login().checkLogin(username, password);		

			if (valid && isLoggedIn(username)) {

				return "library?faces-redirect=true";
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			addErrorMessage(ex);

		}

		return "index?faces-redirect=true";

	}
	
	public boolean isLoggedIn(String user) {
		if(user != null) {

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("user", user);
	
			System.out.println("isloggedin: " +user+ " ligger i mappen: "+sessionMap.toString());
			return true;
		}else {
			return false;
		}	
	}
	
	public String logout() {
		System.out.println("loggar ut användare: " );
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		
		return "index?faces-redirect=true";	
	}
	
	public String registerNewUser(User theUser) {
			
		try {
			
			login = new Login();
			
			login.addNewUser(theUser);
			
		} catch (Exception ex) {

			addErrorMessage(ex);
		
		}
		return "index?faces-redirect=true";
	}
	
	private void addErrorMessage(Exception ex) {
		FacesMessage message = new FacesMessage("Error" + ex.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

}
