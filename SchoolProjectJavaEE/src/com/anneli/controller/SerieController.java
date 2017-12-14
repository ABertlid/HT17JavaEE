package com.anneli.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.anneli.bean.Serie;
import com.anneli.db.SerieRepository;

/**
 * Controller class for series, uses for evaluation and send data between the view and the
 * model
 * 
 * @author Anneli
 * @version 1.0
 * @since 2017-12-13
 */
@ManagedBean
@SessionScoped
public class SerieController implements Serializable {

	private static final long serialVersionUID = 8367650965755888828L;

	private List<Serie> series;
	private SerieRepository serieRepository;
	private String searchSerie;

	/**
	 * Constructor that creates an arraylist of serie object
	 * 
	 * @throws Exception
	 */
	public SerieController() throws Exception {
		series = new ArrayList<>();
	}

	/**
	 * @return a list of series
	 */
	public List<Serie> getSeries() {
		return series;
	}

	/**
	 * Method to handle the search input from user
	 */
	public void loadSeries() {

		// clears the list of series
		series.clear();

		// text field contains a string and length is minimum 1 character
		// create repository object and call the method searchSerie()
		try {
			if (searchSerie != null && searchSerie.trim().length() > 0) {
				serieRepository = new SerieRepository();
				series = serieRepository.searchSeries(searchSerie);

				// create repository object and call the method getSeries()
			} else {

				serieRepository = new SerieRepository();
				series = serieRepository.getSeries();

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			addErrorMessage(ex);
		} finally {

			searchSerie = null;
		}

	}

	/**
	 * Method to handle the incoming text from user and forward to repository
	 * 
	 * @param theSerie
	 *            The Serie
	 * @return library url
	 */
	public String addSerie(Serie theSerie) {

		try {
			serieRepository.addSerie(theSerie);
		} catch (Exception ex) {

			addErrorMessage(ex);

			return null;
		}
		return "library?faces-redirect=true";
	}

	/**
	 * Method that checks the id sent by the user and save in HashMap
	 * 
	 * @param serieId
	 *            The Serie ID
	 * @return update-serie url
	 */
	public String loadSerie(int serieId) {

		try {

			Serie theSerieBean = serieRepository.getSerie(serieId);

			//
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("serie", theSerieBean);

		} catch (Exception ex) {

			addErrorMessage(ex);

			return null;
		}

		return "update-serie";
	}

	/**
	 * Method that checks the new String/title sent by the user
	 * 
	 * @param theSerie
	 *            The Serie title
	 * @return library url
	 */
	public String updateSerie(Serie theSerie) {

		try {

			serieRepository.updateSerie(theSerie);

		} catch (Exception ex) {

			addErrorMessage(ex);

			return null;
		}
		return "library?faces-redirect=true";
	}

	/**
	 * Method that checks the id sent by the user
	 * 
	 * @param serieId
	 *            The Serie ID
	 * @return library url
	 */
	public String deleteSerie(int serieId) {

		try {

			serieRepository.deleteSerie(serieId);

		} catch (Exception ex) {

			addErrorMessage(ex);

			return null;
		}

		return "library";
	}

	public String getSearchSerie() {
		return searchSerie;
	}

	public void setSearchSerie(String searchSerie) {
		this.searchSerie = searchSerie;
	}

	private void addErrorMessage(Exception ex) {
		FacesMessage message = new FacesMessage("Error" + ex.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

}
