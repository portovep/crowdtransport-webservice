package com.coctelmental.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.coctelmental.server.model.TaxiDriver;

public class TaxiDAOImpl implements TaxiDAO {

	@Override
	public TaxiDriver getTaxiDriverByID(String id) {
		TaxiDriver taxiDriver = null;
		Connection con = null;
		DBConnectionHandler db = new DBConnectionHandlerImplMySQL();

		String query = "SELECT * FROM TaxiDrivers WHERE " + "DNI='" + id + "';";
		try {
			con = db.setupConnection();
			ResultSet rs = db.executeQuery(con, query);

			if (rs.next()) {
				taxiDriver = new TaxiDriver();
				taxiDriver.setDni(rs.getString(1));
				taxiDriver.setFullName(rs.getString(2));
				taxiDriver.setPassword(rs.getString(3));
				taxiDriver.setEmail(rs.getString(4));
				taxiDriver.setLicenceNumber(rs.getString(5));
				taxiDriver.setCarTradeMark(rs.getString(6));
				taxiDriver.setCarModel(rs.getString(7));
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
							.println("Error while trying to close the connection: "
									+ sqle.getMessage());
				}
			}
		}
		return taxiDriver;
	}

	@Override
	public int addTaxiDriver(TaxiDriver taxiDriver) {
		int result = -1;
		Connection con = null;
		DBConnectionHandler db = new DBConnectionHandlerImplMySQL();

		String query = "INSERT INTO TaxiDrivers values(" + "'"
				+ taxiDriver.getDni() + "'," + "'" + taxiDriver.getFullName()
				+ "'," + "'" + taxiDriver.getPassword() + "'," + "'"
				+ taxiDriver.getEmail() + "'," + "'"
				+ taxiDriver.getLicenceNumber() + "'," + "'"
				+ taxiDriver.getCarTradeMark() + "'," + "'"
				+ taxiDriver.getCarModel() + "');";

		try {
			con = db.setupConnection();
			result = db.executeUpdate(con, query);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException sqle) {
					System.err
							.println("Error while trying to close the connection: "
									+ sqle.getMessage());
				}
			}
		}
		return result;
	}
}
