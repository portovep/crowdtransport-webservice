package com.coctelmental.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BusCompanyDAOImpl implements BusCompanyDAO {

	@Override
	public String getCompanyAuthCode(String companyCIF) {
		String authCode = null;
        Connection con = null;
        BDConnectionHandler bd = new BDConnectionHandlerImplMySQL();

		String query="SELECT codigoAutorizacion FROM CompAutobuses WHERE " +
	                "cif='"+companyCIF+"';";
        try
        {
            con=bd.setupConnection();
            ResultSet rs=bd.executeQuery(con, query);
                       
            if(rs.next())
            {
            	authCode = rs.getString(1);   
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
        return authCode;
	}

}
