package com.coctelmental.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;



import com.coctelmental.server.model.User;


public class UserDAOImpl implements UserDAO{

	@Override
	public User getUserByID(String userID) {
		User user = null;
        Connection con=null;
        BDConnectionHandler bd=new BDConnectionHandlerImplMySQL();

		String query="";
		query="SELECT * FROM Usuario WHERE " +
	                "nickname='"+userID+"';";
        try
        {
            con=bd.setupConnection();
            ResultSet rs=bd.executeQuery(con, query);  
            
            if(rs.next())
            {
            	user = new User();
            	user.setUserName(rs.getString(1));
            	user.setName(rs.getString(2));
            	user.setPassword(rs.getString(3));
            	user.setEmail(rs.getString(4));
            	
            }
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally
        {
            if(con!=null)
            {
                try
                {
                    con.close();
                }catch(SQLException sqle)
                {
                    System.err.println("Error al intentar cerrar la conexión:" + sqle.getMessage());
                }
            }
        }
        return user;
	}

	@Override
	public int addUser(User user) {
		int result=-1;
        Connection con=null;
        BDConnectionHandler bd=new BDConnectionHandlerImplMySQL();

        String query="INSERT INTO Usuario values(" +
                "'"+user.getUserName()+"'," +
                "'"+user.getFullName()+"'," +
                "'"+user.getPassword()+"'," +
                "'"+user.getEmail()+"');";
        try
        {
            con=bd.setupConnection();
            result=bd.executeUpdate(con, query);
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally
        {
            if(con!=null)
            {
                try
                {
                    con.close();
                }catch(SQLException sqle)
                {
                    System.err.println("Error al intentar cerrar la conexión:" + sqle.getMessage());
                }
            }
        }			
		return result;
	}
}
