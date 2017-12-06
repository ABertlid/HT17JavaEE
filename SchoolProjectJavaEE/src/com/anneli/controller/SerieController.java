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
import com.anneli.db.Queries;

@ManagedBean
@SessionScoped
public class SerieController implements Serializable {

	private static final long serialVersionUID = 8367650965755888828L;

	private List<Serie> series;
	private Queries queries;
	private String searchSerie;

	public SerieController() throws Exception {
		series = new ArrayList<>();
	}

	public List<Serie> getSeries() {
		return series;
	}

	public void loadSeries() {

		series.clear();

		try {
			if (searchSerie != null && searchSerie.trim().length() > 0) {
				queries = new Queries();
				series = queries.searchSeries(searchSerie);

			} else {
				
				queries = new Queries();
				series = queries.getSeries();
				
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			addErrorMessage(ex);
		} finally {

			searchSerie = null;
		}

	}

	public String addSerie(Serie theSerie) {

		try {
			queries.addSerie(theSerie);
		} catch (Exception ex) {

			addErrorMessage(ex);

			return null;
		}
		return "database?faces-redirect=true";
	}

	public String loadSerie(int serieId) {

		try {

			Serie theSerieBean = queries.getSerie(serieId);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("serie", theSerieBean);

		} catch (Exception ex) {

			addErrorMessage(ex);

			return null;
		}

		return "update-serie-form";
	}

	public String updateSerie(Serie theSerie) {

		try {

			queries.updateSerie(theSerie);

		} catch (Exception ex) {

			addErrorMessage(ex);

			return null;
		}
		return "database?faces-redirect=true";
	}

	public String deleteSerie(int serieId) {

		try {

			queries.deleteSerie(serieId);

		} catch (Exception ex) {

			addErrorMessage(ex);

			return null;
		}

		return "database";
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
