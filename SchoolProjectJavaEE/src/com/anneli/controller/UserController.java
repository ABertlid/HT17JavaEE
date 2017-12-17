package com.anneli.controller;

import java.io.Serializable;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.anneli.bean.User;
import com.anneli.db.UserRepository;

/**
 * Controller class for users, uses for evaluation and send data between the
 * view and the model
 * 
 * @author Anneli
 * @version 1.0
 * @since 2017-12-13
 */
@ManagedBean
@SessionScoped
public class UserController implements Serializable {

	private UserRepository userRepository;

	private static final long serialVersionUID = 7407965200910596687L;

	public UserController() {

	}

	/**
	 * Method that checks the username and password sent by user and redirect to
	 * correct url
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return url depending on successful log in
	 * @throws Exception
	 */
	public String validateUser(String username, String password) throws Exception {

		boolean valid = new UserRepository().checkLogin(username, password);

		if (valid && isLoggedIn(username)) {

			return "library?faces-redirect=true";

		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Wrong username or password", ""));

		}

		return "";
	}

	/**
	 * Method that checks the username and save object in a HashMap. Keeps track if
	 * user is logged in
	 * 
	 * @param user
	 *            The username
	 * @return true if user and session is valid otherwise false
	 */
	public boolean isLoggedIn(String user) {
		if (user != null) {

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("user", user);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method that log out user and close current session
	 * 
	 * @return index url
	 */
	public String logout() {

		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		return "index?faces-redirect=true";
	}

	/**
	 * Method that checks the incoming username and password
	 * 
	 * @param theUser
	 *            The user
	 * @return index url
	 */
	public String registerNewUser(User theUser) {

		try {

			userRepository = new UserRepository();

			userRepository.addNewUser(theUser);

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
