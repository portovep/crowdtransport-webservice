package com.coctelmental.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TaxiLicenceDAOImpl implements TaxiLicenceDAO {

	@Override
	public String getOwnerID(String licenceNumber) {
		String dni = null;
        Connection con=null;
        BDConnectionHandler bd=new BDConnectionHandlerImplMySQL();

		String query="SELECT dniTaxista FROM Licencia WHERE " +
	                "nLicencia='"+licenceNumber+"';";
        try
        {
            con=bd.setupConnection();
            ResultSet rs=bd.executeQuery(con, query);
                       
            if(rs.next())
            {
            	dni = rs.getString(1);   
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
        return dni;
	}

}
