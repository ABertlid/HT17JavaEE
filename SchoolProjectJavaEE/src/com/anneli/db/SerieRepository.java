package com.anneli.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.anneli.bean.Serie;

public class SerieRepository extends DatabaseConnection {

	private static final String GET_ALL_SERIES = "SELECT * FROM serie ORDER BY title";
	private static final String ADD_SERIE = "INSERT INTO serie (title) VALUES (?)";
	private static final String GET_SERIE_FROM_ID = "SELECT * FROM serie WHERE serie_id=?";
	private static final String UPDATE_ONE_SERIE = "UPDATE serie SET title=? WHERE serie_id=?";
	private static final String DELETE_ONE_SERIE = "DELETE FROM serie WHERE serie_id=?";
	private static final String SEARCH_FOR_SERIES = "SELECT * FROM serie WHERE LOWER (title) LIKE ?";

	public SerieRepository() throws Exception {
		DatabaseConnection.getInitialize();
	}

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
