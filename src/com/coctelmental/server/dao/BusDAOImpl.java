package com.coctelmental.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.coctelmental.server.model.BusDriver;

public class BusDAOImpl implements BusDAO {

	@Override
	public BusDriver getBusDriverByID(String id) {
		BusDriver busDriver = null;
		Connection con = null;
		DBConnectionHandler db = new DBConnectionHandlerImplMySQL();

		String query = "SELECT * FROM BusDrivers WHERE " + "DNI='" + id + "';";
		try {
			con = db.setupConnection();
			ResultSet rs = db.executeQuery(con, query);

			if (rs.next()) {
				busDriver = new BusDriver();
				busDriver.setDni(rs.getString(1));
				busDriver.setCompanyCIF(rs.getString(2));
				busDriver.setFullName(rs.getString(3));
				busDriver.setPassword(rs.getString(4));
				busDriver.setEmail(rs.getString(5));
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
		return busDriver;
	}

	@Override
	public int addBusDriver(BusDriver busDriver) {
		int result = -1;
		Connection con = null;
		DBConnectionHandler db = new DBConnectionHandlerImplMySQL();

        String query = "INSERT INTO BusDrivers values(" +
        "'"+busDriver.getDni()+"'," +
        "'"+busDriver.getCompanyCIF()+"'," +
        "'"+busDriver.getFullName()+"'," +
        "'"+busDriver.getPassword()+"'," +
        "'"+busDriver.getEmail()+"');";

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
							.println("Error trying to close the connection: "
									+ sqle.getMessage());
				}
			}
		}
		return result;
	}

}
