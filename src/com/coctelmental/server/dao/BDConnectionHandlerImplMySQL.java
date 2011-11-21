package com.coctelmental.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BDConnectionHandlerImplMySQL implements BDConnectionHandler {

	public Connection setupConnection() throws Exception {
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
		} catch (java.lang.ClassNotFoundException e) {
			System.err.println("MySQL JDBC Driver not found..");
			throw (e);
		}

		String url = "";
		try {
			url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + BD_NAME;
			Connection con = DriverManager.getConnection(url, BD_USER,
					BD_USER_PASSWORD);
			return con;
		} catch (java.sql.SQLException e) {
			System.err.println("Error connecting with " + url);
			throw (e);
		}
	}

	public ResultSet executeQuery(Connection con, String query)
			throws Exception {
		try {
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(query);
			s.close();

			return rs;
		} catch (SQLException e) {
			System.err.println("Error executing this SQL query: " + query);
			throw (e);
		}
	}

	public ResultSet executeQuery(Connection con, PreparedStatement ps)
			throws Exception {
		try {
			ResultSet rs = ps.executeQuery();

			return rs;
		} catch (SQLException e) {
			System.err.println("Error executing this SQL query: "
					+ ps.toString());
			throw (e);
		}
	}

	public int executeUpdate(Connection con, String operation) throws Exception {
		Statement s = null;
		int result = -1;

		try {
			s = con.createStatement();
			result = s.executeUpdate(operation);
			s.close();

			return result;
		} catch (SQLException e) {
			System.err.println("Error executing this SQL sentence: "
					+ operation);
			throw (e);
		}
	}

	public void executeUpdate(Connection con, PreparedStatement ps)
			throws Exception {
		try {
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error executing this SQL sentence: "
					+ ps.toString());
			throw (e);
		}
	}
}
