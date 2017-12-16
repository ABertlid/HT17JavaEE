package com.anneli.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import com.anneli.bean.User;

/**
 * User Repository class that handles queries to the database
 * 
 * @author Anneli
 * @version 1.0
 * @since 2017-12-13
 */
public class UserRepository extends DatabaseConnection {

	private static final String USER_PW_CHECK = "SELECT * FROM login WHERE username=? AND password=?";
	private static final String REG_NEW_USER = "INSERT INTO login (username, password) VALUES (?,?) ";

	/**
	 * Constructor that initialize a single connection to database
	 * 
	 * @throws Exception
	 */
	public UserRepository() throws Exception {
		DatabaseConnection.getInitialize();
	}

	/**
	 * Method that creates query and check if username and password is valid
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return true if valid
	 * @throws Exception
	 */
	public boolean checkLogin(String username, String password) throws Exception {
		String hashedPassword = getHashedPassword(password);

		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();

			pStatement = connection.prepareStatement(USER_PW_CHECK);
			pStatement.setString(1, username);
			pStatement.setString(2, hashedPassword);

			resultSet = pStatement.executeQuery();

			if (resultSet.next()) {

				return true;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		finally {
			closeConnPstatRset(connection, pStatement, resultSet);
		}
		return false;

	}

	/**
	 * Method that creates query and add the new user into database with hashed
	 * password
	 * 
	 * @param theUser
	 *            The User
	 * @throws SQLException
	 */
	public void addNewUser(User theUser) throws SQLException {
		String hashedPassword = setHashedPassword(theUser);

		Connection connection = null;
		PreparedStatement pStatement = null;

		try {
			connection = dataSource.getConnection();

			pStatement = connection.prepareStatement(REG_NEW_USER);

			pStatement.setString(1, theUser.getUsername());
			pStatement.setString(2, hashedPassword);

			pStatement.execute();
		} finally {
			closeConnPstat(connection, pStatement);
		}

	}

	/**
	 * Method that creates a hashed password from user. Gets an instance of class
	 * and chosen algorithm SHA-256. Processing the data in update() Digest,
	 * calculate and define the password from user input.
	 * 
	 * In detail:
	 * 
	 * 0xff = 1 byte, 0-255, no negative values
	 * 
	 * 0x100 = adds and guarantee 3 digits value
	 * 
	 * 16 = hexadecimal system
	 * 
	 * substring(1) = removes the digit 1 in 0x100
	 * 
	 * sBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100,
	 * 16).substring(1));
	 * 
	 * @param theUser The User
	 * @return the hashed password
	 */
	private String setHashedPassword(User theUser) {
		String passwordToHash = theUser.getPassword();
		String hashedPassword = null;

		try {

			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

			messageDigest.update(passwordToHash.getBytes());

			byte[] bytes = messageDigest.digest();

			hashedPassword = ByteUtils.toHexString(bytes);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashedPassword;
	}

	private String getHashedPassword(String password) {
		String hashToPassword = password;

		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(hashToPassword.getBytes());
			byte[] bytes = messageDigest.digest();

			hashToPassword = ByteUtils.toHexString(bytes);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashToPassword;
	}
	
	// TODO add method for salt

	private void closeConnPstatRset(Connection connection, PreparedStatement pStatement, ResultSet resultSet)
			throws SQLException {
		closeConnPstat(connection, pStatement);
		resultSet.close();
	}

	private void closeConnPstat(Connection connection, PreparedStatement pStatement) throws SQLException {
		connection.close();
		pStatement.close();
	}

}
