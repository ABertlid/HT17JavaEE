package com.anneli.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
	
	@WebServlet("/TestServlet")
	public class TestServlet extends HttpServlet {
		private static final long serialVersionUID = -3175265306794129331L;
		
		@Resource(name="jdbc/serie_library")
		private DataSource dataSource;

		
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

			PrintWriter out = response.getWriter();
			response.setContentType("text/plain");
			
			Connection conn = null;
			Statement stat = null;
			ResultSet result = null;
			
			try {
				conn = dataSource.getConnection();
				String sql = "select * from series;";
				
				stat = conn.createStatement();
				result = stat.executeQuery(sql);
				
				while(result.next()) {
					String title = result.getString("title");
					out.println(title);
					System.out.println(title);
				}			
				
			}catch (Exception e) {
				e.printStackTrace();
				out.println(e.getMessage());
			}			
		}		
	}

