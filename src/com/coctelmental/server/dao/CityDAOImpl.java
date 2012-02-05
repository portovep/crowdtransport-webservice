package com.coctelmental.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CityDAOImpl implements CityDAO {

	@Override
	public List<String> getCityNames() {
		ArrayList<String> cityNames = new ArrayList<String>();
		Connection con = null;
		DBConnectionHandler db = new DBConnectionHandlerImplMySQL();

		String query = "SELECT * FROM Cities;";

		try {
			con = db.setupConnection();
			ResultSet rs = db.executeQuery(con, query);
			while (rs.next()) {
				cityNames.add(rs.getString(1));
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
		return cityNames;
	}

}
