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

@ManagedBean
@SessionScoped
public class SerieController implements Serializable {

	private static final long serialVersionUID = 8367650965755888828L;

	private List<Serie> series;
	private SerieRepository serieRepository;
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
				serieRepository = new SerieRepository();
				series = serieRepository.searchSeries(searchSerie);

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

	public String addSerie(Serie theSerie) {

		try {
			serieRepository.addSerie(theSerie);
		} catch (Exception ex) {

			addErrorMessage(ex);

			return null;
		}
		return "library?faces-redirect=true";
	}

	public String loadSerie(int serieId) {

		try {

			Serie theSerieBean = serieRepository.getSerie(serieId);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("serie", theSerieBean);

		} catch (Exception ex) {

			addErrorMessage(ex);

			return null;
		}

		return "update-serie";
	}

	public String updateSerie(Serie theSerie) {

		try {

			serieRepository.updateSerie(theSerie);

		} catch (Exception ex) {

			addErrorMessage(ex);

			return null;
		}
		return "library?faces-redirect=true";
	}

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
