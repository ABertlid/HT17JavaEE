package com.anneli.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.anneli.user.User;

public class Login extends DatabaseConnection {

	private static final String USER_PW_CHECK = "SELECT * FROM login WHERE username=? AND password=?";
	private static final String REG_NEW_USER = "INSERT INTO login (username, password) VALUES (?,?) ";

	public Login() throws Exception {
		DatabaseConnection.getInitialize();
	}

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
				System.out.println("login sucess");
				return true;
			} else {
				System.out.println("login failed");
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		finally {
			closeConnPstatRset(connection, pStatement, resultSet);
		}
		return false;

	}

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

	private String setHashedPassword(User theUser) {
		String passwordToHash = theUser.getPassword();
		String hashedPassword = null;
		
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(passwordToHash.getBytes());
			byte[] bytes = messageDigest.digest();
			
			StringBuilder sBuilder = new StringBuilder();
			for(int i = 0; i < bytes.length; i++) {
				sBuilder.append(Integer.toString((bytes[i] & 0xff)
						+ 0x100,16).substring(1));
				
				hashedPassword = sBuilder.toString();
						
			}
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
			
			StringBuilder sBuilder = new StringBuilder();
			for(int i = 0; i < bytes.length; i++) {
				sBuilder.append(Integer.toString((bytes[i] & 0xff)
						+ 0x100,16).substring(1));
				
				hashToPassword = sBuilder.toString();
						
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashToPassword;
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

}
