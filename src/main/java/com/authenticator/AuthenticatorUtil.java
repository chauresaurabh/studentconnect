package com.authenticator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public class AuthenticatorUtil {

	public static JSONObject authenticate( String email, String password) {

		Connection conn = null;
		JSONObject object = new JSONObject();
		try{
 
		  Class.forName("com.mysql.jdbc.Driver");
 
		  conn = DriverManager.getConnection("jdbc:mysql://localhost/studentconnect_database?user=root");
 
		  PreparedStatement ps = null;
		  ResultSet res = null;
		  
		  ps = conn.prepareStatement("select count(*) as count from LOGIN where email=? and password=?");
		  ps.setString(1, email);
		  ps.setString(2, password);
		  
		  res = ps.executeQuery();
 		  while(res.next())
		  {
			 if( res.getInt("count") == 0 )
				 object.put("status", 0);
			 else
				 object.put("status", 1);
		  }
		}catch(Exception e){
				e.printStackTrace();
		}
		finally{
			 if(conn!=null)
			 {
				 try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		}
		
		return object;
	
	}
	 
}
