package com.coctelmental.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaxiLicenceDAOImpl implements TaxiLicenceDAO {

	@Override
	public String getOwnerID(String licenceNumber) {
		String ownerID = null;
		Connection con = null;
		DBConnectionHandler db = new DBConnectionHandlerImplMySQL();

		String query = "SELECT assigned_DNI FROM TaxiLicences WHERE " + "licence_number='"
				+ licenceNumber + "';";
		try {
			con = db.setupConnection();
			ResultSet rs = db.executeQuery(con, query);

			if (rs.next()) {
				ownerID = rs.getString(1);
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
		return ownerID;
	}

}
