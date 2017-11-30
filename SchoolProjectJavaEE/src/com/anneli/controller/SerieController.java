package com.anneli.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.anneli.bean.Serie;
import com.anneli.db.DatabaseUtil;

@ManagedBean
@SessionScoped
public class SerieController implements Serializable {

	private static final long serialVersionUID = 8367650965755888828L;
	
	private List<Serie> series;
	private DatabaseUtil databaseUtil;
	private String searchSerie;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public SerieController() throws Exception {
		series = new ArrayList<>();
		
		databaseUtil = DatabaseUtil.getInstance();
	}

	public List<Serie> getSeries() {
		return series;
	}
	
	public void loadSeries() {
		logger.info("Loading series");
		
		logger.info("SearchSerie = " + searchSerie);
		
		series.clear();
		
		try {
			if (searchSerie != null && searchSerie.trim().length() > 0) {
				
				series = databaseUtil.searchSeries(searchSerie);
				
			} else {
				
				series = databaseUtil.getSeries();
			}
			
			
		} catch (Exception ex) {
			
			logger.log(Level.SEVERE, "Error loading series", ex);
			
			addErrorMessage(ex);
		}
		finally {
			
			searchSerie = null;
		}
		
	}
	
	public String addSerie(Serie theSerie) {
		logger.info("Adding serie: "+ theSerie);
		
		try {
			databaseUtil.addSerie(theSerie);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error adding series", ex);

			addErrorMessage(ex);
			
			return null;
		}
		return "database?faces-redirect=true";
	}
	
	public String loadSerie(int serieId) {
		logger.info("Loading serie: "+ serieId);
		
		try {
			
		 Serie theSerieBean = databaseUtil.getSerie(serieId);
		    
			ExternalContext externalContext =
					FacesContext.getCurrentInstance().getExternalContext();
						
			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("serie", theSerieBean);
			System.out.println("seriebönan: " + theSerieBean);
			System.out.println("Hela Mappen innehåller: "+requestMap);
			
			
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error loading serie id" + serieId, ex);
			
			addErrorMessage(ex);
			
			return null;
		}
		System.out.println("Mappen innehåller id: "+serieId);
		
		return "update-serie-form"; 
		
		//kolla upp
	}
	
	public String updateSerie(Serie theSerie) {
		System.out.println("in i update serie från xhtml-update: " +theSerie);
		logger.info("Updating serie: "+ theSerie);
		
		try {
			System.out.println("update i controllern "+ theSerie);
			databaseUtil.updateSerie(theSerie);
			
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error updating serie " + theSerie, ex);
			
			addErrorMessage(ex);
			
			return null;
		}
		return "database?faces-redirect=true";
	}
	
	public String deleteSerie(int serieId) {
		
		logger.info("Deleting serie id: " + serieId);
		
		try {
			
			databaseUtil.deleteSerie(serieId);
			
		} catch (Exception ex) {
			
			logger.log(Level.SEVERE, "Error deleting serie id: " + serieId);
			
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
		FacesMessage message = new FacesMessage("Error"+ 
				ex.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);	
		
	}	
	
}
