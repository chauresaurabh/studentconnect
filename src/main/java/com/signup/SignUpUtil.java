package com.signup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.JSONObject;

public class SignUpUtil {

 	public static JSONObject signup(
 			String firstname,
 			String lastname,
 			String email,
 			String password) {
 
		Connection conn = null;
		JSONObject object = new JSONObject();
		try{
 
		  Class.forName("com.mysql.jdbc.Driver");
 
		  conn = DriverManager.getConnection("jdbc:mysql://localhost/studentconnect_database?user=root");
 
		  PreparedStatement ps = null;
 		  
		  String insertSql = " insert into user(firstname, lastname, email, password) " +
		  					 " values(?,?,?,?)";
		  ps = conn.prepareStatement(insertSql);
		  ps.setString(1, firstname);	  ps.setString(2, lastname);
		  ps.setString(3, email);  ps.setString(4, password);
		  
		  ps.execute();
 		   
		  object.put("status", 1);
		  
		}catch(Exception e){
				e.printStackTrace();
				object.put("status", 0);
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
