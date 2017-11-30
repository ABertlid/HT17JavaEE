package com.anneli.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.anneli.bean.Serie;

public class DatabaseUtil {

	private static DatabaseUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/hibernate_serie";

	public static DatabaseUtil getInstance() throws Exception {
		if(instance == null) {
			instance = new DatabaseUtil();
		}
		return instance;
	}
	
private DatabaseUtil() throws Exception {
	dataSource = getDataSource();
}
	private DataSource getDataSource() throws NamingException{
		Context context = new InitialContext();
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
	
	public List<Serie> getSeries() throws Exception{
		
		List<Serie> series = new ArrayList<>();
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = dataSource.getConnection();
			String sql = "select * from serie order by title";
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			
			while(resultSet.next()) {
				
				int id = resultSet.getInt("serie_id");
				String title = resultSet.getString("title");
				
				Serie tempSerie = new Serie(id, title);
				
				series.add(tempSerie);
			}
			return series;
		} finally {
			connection.close();
			statement.close();
			resultSet.close();
		}
	}

	public void addSerie(Serie theSerie) throws Exception{
		
		Connection connection = null;
		PreparedStatement pStatement = null;
		
		try {
			connection = dataSource.getConnection();
			String sql = "insert into serie (title) values (?)";
			pStatement = connection.prepareStatement(sql);
			System.out.println(sql);
			
			pStatement.setString(1, theSerie.getTitle());
			
			System.out.println("Adding serie: " +theSerie.getTitle());
			
			pStatement.execute();
		} finally {
			connection.close();
			pStatement.close();
		}
		
	}

	public Serie getSerie(int serieId) throws Exception {
		
		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = dataSource.getConnection();
			String sql = "select * from serie where serie_id=?";
			pStatement = connection.prepareStatement(sql);
			System.out.println(sql);
			pStatement.setInt(1, serieId);
			System.out.println(serieId);
			
			resultSet = pStatement.executeQuery();
			
			Serie theSerie = null;
			
			if(resultSet.next()) {
				int id = resultSet.getInt("serie_id");
				String title = resultSet.getString("title");
				
				theSerie = new Serie(id, title);
				System.out.println("test: " + id + title);
				
			}
			else {
				throw new Exception("Could not find serie id:" +serieId);
			}
			System.out.println("serie som returneras är: "+ theSerie);
			return theSerie;
			
		} finally {
			connection.close();
			pStatement.close();
			resultSet.close();
		}
		
	}

	public void updateSerie(Serie theSerie) throws Exception{
		System.out.println("direkt in i updateSerie med angett id: " +theSerie.getId());
		Connection connection = null;
		PreparedStatement pStatement = null;
		
		try {
			connection = dataSource.getConnection();
			String sql = "update serie set title=? where serie_id=?";
			pStatement = connection.prepareStatement(sql);
			System.out.println("Update " +sql);
			
			pStatement.setString(1, theSerie.getTitle());
			pStatement.setInt(2, theSerie.getId());
			System.out.println("titel som ska uppdateras: " + theSerie.getTitle());
			System.out.println("id som ska uppdateras: " + theSerie.getId());
			
			pStatement.execute();
		} finally {
			connection.close();
			pStatement.close();
		}
		
	}

	public void deleteSerie(int serieId) throws Exception {
		System.out.println("in i deleteSerie med angett id: " + serieId);
		Connection connection = null;
		PreparedStatement pStatement = null;
		
		try {
			
			connection = dataSource.getConnection();
			
			String sql = "delete from serie where serie_id=?";
			
			pStatement = connection.prepareStatement(sql);
			
			pStatement.setInt(1, serieId);
			
			pStatement.execute();
			System.out.println("deletad! "+ serieId);
			
		} finally {
			
			connection.close();
			pStatement.close();
		}
			
	}

	public List<Serie> searchSeries(String searchSerie) throws Exception {
		
		List<Serie> series = new ArrayList<>();
		
		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			connection = dataSource.getConnection();
			
			if(searchSerie != null && searchSerie.trim().length() > 0) {
				
				String sql = "select * from serie where lower (title) like ?";
				pStatement = connection.prepareStatement(sql);
				
				String searchSerieLike = "%" + searchSerie.toLowerCase() + "%";
				
				pStatement.setString(1, searchSerieLike);
				
			} else {
				
				String sql = "select * from serie order by title";
				
				pStatement = connection.prepareStatement(sql);
			}
				
			resultSet = pStatement.executeQuery();
			
			while(resultSet.next()) {
				int id = resultSet.getInt("serie_id");
				String title = resultSet.getString("title");
				
				Serie tempSerie = new Serie(id, title);
				
				series.add(tempSerie);
			}
			
			return series;
			
		} finally {
			
			connection.close();
			pStatement.close();
			resultSet.close();
		}

		
	}
	
}
