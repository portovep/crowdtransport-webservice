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
        BDConnectionHandler bd = new BDConnectionHandlerImplMySQL();

		String query="SELECT * FROM ConductoresAutobus WHERE " +
	                "dni='"+id+"';";
        try {
            con=bd.setupConnection();
            ResultSet rs=bd.executeQuery(con, query);
                       
            if(rs.next()) {
            	busDriver = new BusDriver();
            	busDriver.setDni(rs.getString(1));
            	busDriver.setCompanyCIF(rs.getString(2));
            	busDriver.setFullName(rs.getString(3));
            	busDriver.setPassword(rs.getString(4));
            	busDriver.setEmail(rs.getString(5));    
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
        return busDriver;
	}

	@Override
	public int addBusDriver(BusDriver busDriver) {
		int result = -1;
        Connection con = null;
        BDConnectionHandler bd=new BDConnectionHandlerImplMySQL();

        String query="INSERT INTO ConductoresAutobus values(" +
                "'"+busDriver.getDni()+"'," +
                "'"+busDriver.getCompanyCIF()+"'," +
                "'"+busDriver.getFullName()+"'," +
                "'"+busDriver.getPassword()+"'," +
                "'"+busDriver.getEmail()+"');";
        
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
