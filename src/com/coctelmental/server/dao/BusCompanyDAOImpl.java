package com.coctelmental.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BusCompanyDAOImpl implements BusCompanyDAO {

	@Override
	public String getCompanyAuthCode(String id) {
		String authCode = null;
		Connection con = null;
		DBConnectionHandler bd = new DBConnectionHandlerImplMySQL();

		String query = "SELECT auth_code FROM BusCompanies WHERE " + "CIF='"
				+ id + "';";
		try {
			con = bd.setupConnection();
			ResultSet rs = bd.executeQuery(con, query);

			if (rs.next()) {
				authCode = rs.getString(1);
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
		return authCode;
	}

}
