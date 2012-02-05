package com.coctelmental.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LineDAOImpl implements LineDAO {

	@Override
	public List<String> getLineNames(String cityID) {
		ArrayList<String> lineNames = new ArrayList<String>();
		Connection con = null;
		DBConnectionHandler db = new DBConnectionHandlerImplMySQL();

		// Note: Lines is quoted because it's a reserved keyword in MySQL
		String query = "SELECT * FROM `Lines` WHERE city_name='"
				+ cityID + "';";

		try {
			con = db.setupConnection();
			ResultSet rs = db.executeQuery(con, query);
			while (rs.next()) {
				lineNames.add(rs.getString(2));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException sqle) {
					System.err
							.println("Error trying to close the connection: "
									+ sqle.getMessage());
				}
			}
		}
		return lineNames;
	}
}
