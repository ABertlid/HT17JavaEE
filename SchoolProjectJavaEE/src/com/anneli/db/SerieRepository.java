package com.anneli.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.anneli.bean.Serie;

/**
 * Serie Repository class that handles queries to the database
 * 
 * @author Anneli
 * @version 1.0
 * @since 2017-12-13
 */
public class SerieRepository extends DatabaseConnection {

	private static final String GET_ALL_SERIES = "SELECT * FROM serie ORDER BY title";
	private static final String ADD_SERIE = "INSERT INTO serie (title) VALUES (?)";
	private static final String GET_SERIE_FROM_ID = "SELECT * FROM serie WHERE serie_id=?";
	private static final String UPDATE_ONE_SERIE = "UPDATE serie SET title=? WHERE serie_id=?";
	private static final String DELETE_ONE_SERIE = "DELETE FROM serie WHERE serie_id=?";
	private static final String SEARCH_FOR_SERIES = "SELECT * FROM serie WHERE LOWER (title) LIKE ?";

	/**
	 * Constructor that initialize a single connection to database
	 * 
	 * @throws Exception
	 */
	public SerieRepository() throws Exception {
		DatabaseConnection.getInitialize();
	}

	/**
	 * Method that creates query and gets all the series
	 * 
	 * @return List of series
	 * @throws Exception
	 */
	public List<Serie> getSeries() throws Exception {

		List<Serie> series = new ArrayList<>();

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();

			statement = connection.createStatement();
			resultSet = statement.executeQuery(GET_ALL_SERIES);

			while (resultSet.next()) {

				int id = resultSet.getInt("serie_id");
				String title = resultSet.getString("title");

				Serie tempSerie = new Serie(id, title);

				series.add(tempSerie);
			}
			return series;
		} finally {
			closeConnStatRset(connection, statement, resultSet);
		}
	}

	/**
	 * Method that creates query and adds a new serie to the database
	 * 
	 * @param theSerie
	 *            The Serie
	 * @throws Exception
	 */
	public void addSerie(Serie theSerie) throws Exception {

		Connection connection = null;
		PreparedStatement pStatement = null;

		try {
			connection = dataSource.getConnection();

			pStatement = connection.prepareStatement(ADD_SERIE);

			pStatement.setString(1, theSerie.getTitle());

			pStatement.execute();
		} finally {
			closeConnPstat(connection, pStatement);
		}

	}

	/**
	 * Method that creates query and gets one serie based on Serie ID
	 * 
	 * @param serieId
	 *            The Serie ID
	 * @return object of Serie
	 * @throws Exception
	 */
	public Serie getSerie(int serieId) throws Exception {

		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();

			pStatement = connection.prepareStatement(GET_SERIE_FROM_ID);
			pStatement.setInt(1, serieId);

			resultSet = pStatement.executeQuery();

			Serie theSerie = null;

			if (resultSet.next()) {
				int id = resultSet.getInt("serie_id");
				String title = resultSet.getString("title");

				theSerie = new Serie(id, title);

			} else {
				throw new Exception("Could not find serie id:" + serieId);
			}

			return theSerie;

		} finally {
			closeConnPstatRset(connection, pStatement, resultSet);
		}

	}

	/**
	 * Method that creates query and update a serie based on user input
	 * 
	 * @param theSerie
	 *            The Serie
	 * @throws Exception
	 */
	public void updateSerie(Serie theSerie) throws Exception {

		Connection connection = null;
		PreparedStatement pStatement = null;

		try {
			connection = dataSource.getConnection();

			pStatement = connection.prepareStatement(UPDATE_ONE_SERIE);

			pStatement.setString(1, theSerie.getTitle());
			pStatement.setInt(2, theSerie.getId());

			pStatement.execute();
		} finally {
			closeConnPstat(connection, pStatement);
		}

	}

	/**
	 * Method that creates query and delete a serie based on Serie ID
	 * 
	 * @param serieId
	 *            The Serie ID
	 * @throws Exception
	 */
	public void deleteSerie(int serieId) throws Exception {

		Connection connection = null;
		PreparedStatement pStatement = null;

		try {

			connection = dataSource.getConnection();

			pStatement = connection.prepareStatement(DELETE_ONE_SERIE);

			pStatement.setInt(1, serieId);

			pStatement.execute();

		} finally {

			closeConnPstat(connection, pStatement);
		}

	}

	/**
	 * Method that creates query to search for specific serie/series based on user input
	 * 
	 * @param searchSerie
	 * @return List with series
	 * @throws Exception
	 */
	public List<Serie> searchSeries(String searchSerie) throws Exception {

		List<Serie> series = new ArrayList<>();

		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;

		try {

			connection = dataSource.getConnection();

			if (searchSerie != null && searchSerie.trim().length() > 0) {

				pStatement = connection.prepareStatement(SEARCH_FOR_SERIES);

				String searchSerieLike = "%" + searchSerie.toLowerCase() + "%";

				pStatement.setString(1, searchSerieLike);

			} else {

				pStatement = connection.prepareStatement(GET_ALL_SERIES);
			}

			resultSet = pStatement.executeQuery();

			while (resultSet.next()) {
				int id = resultSet.getInt("serie_id");
				String title = resultSet.getString("title");

				Serie tempSerie = new Serie(id, title);

				series.add(tempSerie);
			}

			return series;

		} finally {

			closeConnPstatRset(connection, pStatement, resultSet);
		}

	}

	private void closeConnPstatRset(Connection connection, PreparedStatement pStatement, ResultSet resultSet)
			throws SQLException {
		closeConnPstat(connection, pStatement);
		resultSet.close();
	}

	private void closeConnPstat(Connection connection, PreparedStatement pStatement) throws SQLException {
		connection.close();
		pStatement.close();
	}

	private void closeConnStatRset(Connection connection, Statement statement, ResultSet resultSet)
			throws SQLException {
		connection.close();
		statement.close();
		resultSet.close();
	}

}
