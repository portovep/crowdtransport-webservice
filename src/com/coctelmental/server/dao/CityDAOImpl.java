package com.coctelmental.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class CityDAOImpl implements CityDAO {

	@Override
	public List<String> getCityNames() {
        ArrayList<String> ciudades=new ArrayList<String>();
        Connection con=null;
        BDConnectionHandler bd=new BDConnectionHandlerImplMySQL();

        String consulta="SELECT * FROM ciudades;";
        
        try
        {
            con=bd.setupConnection();
            ResultSet rs=bd.executeQuery(con, consulta);
            while(rs.next())
            {           	
            	ciudades.add(rs.getString(1));
            }
            rs.close();
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
		return ciudades;
	}

}
