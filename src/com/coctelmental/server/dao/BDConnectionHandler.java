package com.coctelmental.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface BDConnectionHandler {
	
    public static final String HOST = "localhost";
    public static final int PORT = 3306;
    public static final String BD_NAME = "bd_tfg";
    public static final String BD_USER = "tfg";
    public static final String BD_USER_PASSWORD = "tfg";
	
    public Connection setupConnection() throws Exception;

    public ResultSet executeQuery(Connection con, String query) throws Exception;

    public ResultSet executeQuery(Connection con, PreparedStatement ps) throws Exception;

	public int executeUpdate(Connection con, String operation) throws Exception;

    public void executeUpdate(Connection con, PreparedStatement ps) throws Exception;
    
}
