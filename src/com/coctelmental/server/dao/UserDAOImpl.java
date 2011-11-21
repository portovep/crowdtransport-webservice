package com.coctelmental.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.coctelmental.server.model.User;

public class UserDAOImpl implements UserDAO {

	@Override
	public User getUserByID(String id) {
		User user = null;
		Connection con = null;
		DBConnectionHandler db = new DBConnectionHandlerImplMySQL();

		String query = "";
		query = "SELECT * FROM Users WHERE " + "nickname='" + id + "';";
		try {
			con = db.setupConnection();
			ResultSet rs = db.executeQuery(con, query);

			if (rs.next()) {
				user = new User();
				user.setUserName(rs.getString(1));
				user.setName(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setEmail(rs.getString(4));
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
		return user;
	}

	@Override
	public int addUser(User user) {
		int result = -1;
		Connection con = null;
		DBConnectionHandler db = new DBConnectionHandlerImplMySQL();

        String query = "INSERT INTO Users values(" +
		        "'"+user.getUserName()+"'," +
		        "'"+user.getFullName()+"'," +
		        "'"+user.getPassword()+"'," +
		        "'"+user.getEmail()+"');";
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
