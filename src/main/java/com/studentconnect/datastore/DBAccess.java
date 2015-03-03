package com.studentconnect.datastore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.studentconnect.posts.QueryEntity;
import com.studentconnect.posts.ResponseEntity;




public class DBAccess {
	public static Connection conn = null;
	public static final int BATCH_SIZE = 100;
	public static final String username = "root";
	public static final String password = "niki";

	private final static String INSERT_QUERY_ENTITY = "insert into query_entity (post_id, question, no_replies, no_views, posted_date) values (?,?,?,?,?);";
	private final static String INSERT_RESPONSE_ENTITY = "insert into response_entity (response_id, response, upvotes, downvotes,posted_date) values (?,?,?,?,?);";
	private final static String INSERT_QUERY_RESPONSE_ENTITY = "insert into query_response_entity (post_id, response_id) values (?,?);";
	
	private final static String SELECT_QUERY_ENTITY = "SELECT * from query_entity where post_id in (";
	private final static String SELECT_RESPONSE_ENTITY = "SELECT * from response_entity where response_id in (";
	private final static String SELECT_QUERY_RESPONSE_ENTITY = "SELECT response_id from query_response_entity where post_id in (";
	
	public static void main(String[] args){
		getConnection();
		
	}

	public static void getConnection() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/student_connect", username, password);
			System.out.println("Connection Eshtablished");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("Connection Failed!" + e);
		}

	}

	public static void destroyConnection(){
		try {
			if (conn != null)
				conn.close();
			System.out.println("Connection closed !!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public static boolean saveQueryEntity(List<QueryEntity> records) {
		int i, j, len = records.size(), count = 0;
		int no_batches = (len / BATCH_SIZE);
		QueryEntity query = null;

		if (len % BATCH_SIZE > 0)
			no_batches++;

		if (conn == null)
			return false;
		System.out.println("Save to DB");
		PreparedStatement ps = null;

		try {
			for (i = 0; i < no_batches; i++) {
				ps = conn.prepareStatement(INSERT_QUERY_ENTITY);
				for (j = count; j < count + BATCH_SIZE && j < len; j++){
					query = records.get(j);
					ps.setString(1, query.getPostId());
					ps.setString(2, query.getQuestion());
					ps.setInt(3, query.getReplies());
					ps.setInt(4, query.getViews());
					ps.setDate(5, new java.sql.Date(query.getPosted_date().getTime()));
					ps.addBatch();


				}
				ps.executeBatch();
				ps.close();
				System.out.println("Query Enitity batch " + i + " successfully executed");
				count = j;
			}
		} catch (Exception e) {
			System.out.println("Exception occurred in saving query entity" + e);
		}

		return true;
	}

	public static boolean saveResponseEntity(List<ResponseEntity> records) {
		int i, j, len = records.size(), count = 0;
		int no_batches = (len / BATCH_SIZE);
		ResponseEntity query = null;

		if (len % BATCH_SIZE > 0)
			no_batches++;

		if (conn == null)
			return false;
		System.out.println("Save to DB");
		PreparedStatement ps = null;

		try {
			for (i = 0; i < no_batches; i++) {
				ps = conn.prepareStatement(INSERT_RESPONSE_ENTITY);
				for (j = count; j < count + BATCH_SIZE && j < len; j++){
					query = records.get(j);
					ps.setString(1, query.getResponseId());
					ps.setString(2, query.getResponse());
					ps.setInt(3, query.getUpVotes());
					ps.setInt(4, query.getDownVotes());
					ps.setDate(5, new java.sql.Date(query.getPostedDate().getTime()));
					ps.addBatch();


				}
				ps.executeBatch();
				ps.close();
				System.out.println("Response Enitity batch " + i + " successfully executed");
				count = j;
			}
		} catch (Exception e) {
			System.out.println("Exception occurred in saving response entity" + e);
		}

		return true;
	}

	public static boolean saveQAEntity(Map<String, List<String>> queryResponseMap) {
		int i, j=1, len = queryResponseMap.size(), count = 0;
		int no_batches = (len / BATCH_SIZE);
		List<String> query;
		Set<String> queryKeys = queryResponseMap.keySet();


		if (conn == null)
			return false;
		System.out.println("Save to DB");
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(INSERT_QUERY_RESPONSE_ENTITY);
			for (String key: queryKeys) {

				List<String> answers = queryResponseMap.get(key);
				if( (j%BATCH_SIZE) == 0 || j == len){
					ps.executeBatch();
					ps.close();
					ps = conn.prepareStatement(INSERT_QUERY_RESPONSE_ENTITY);
				}
				for(String ans: answers){	
					ps.setString(1, key);
					ps.setString(2, ans);

					ps.addBatch();
				}
				j++;


			}

			System.out.println("Query Response batch successfully executed");

		} catch (Exception e) {
			System.out.println("Exception occurred in saving response entity" + e);
		}

		return true;
	}

	public static List<QueryEntity> getQueryEntity(List<String> postIds){
		List<QueryEntity> posts = new ArrayList<QueryEntity> ();
		QueryEntity post = null;
		
		if(conn == null)
			return posts;
		
		StringBuilder selectIds = new StringBuilder();
		for(String id: postIds){
			selectIds.append(id);
			selectIds.append(',');
		}
		selectIds.deleteCharAt(selectIds.length()-1);
		selectIds.append(");");
		
		try {
			 Statement st = conn.createStatement();
			 
			 ResultSet rs = st.executeQuery(SELECT_QUERY_ENTITY + selectIds.toString());
			 while(rs.next()){
				 
				 post = new QueryEntity(rs.getString(0), rs.getString(1));
				 post.setReplies(rs.getInt(3));
				 post.setTag(rs.getString(6));
				 post.setViews(rs.getInt(4));
				 post.setPosted_date(rs.getDate(5));
				 posts.add(post);
			 }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return posts;
		
	}
	
	public static List<ResponseEntity> getResponseEntity(List<String> answerIds){
		List<ResponseEntity> responses = new ArrayList<ResponseEntity> ();
		ResponseEntity response = null;
		

		StringBuilder selectIds = new StringBuilder();
		for(String id: answerIds){
			selectIds.append(id);
			selectIds.append(',');
		}
		selectIds.deleteCharAt(selectIds.length()-1);
		selectIds.append(");");
		
		if(conn == null)
			return responses;
		
		try {
			 Statement st = conn.createStatement();
			 
			 ResultSet rs = st.executeQuery(SELECT_RESPONSE_ENTITY);
			 while(rs.next()){
				 
				 response = new ResponseEntity(rs.getString(0), rs.getString(1));
				 response.setUpVotes(rs.getInt(3));
				 response.setDownVotes(rs.getInt(4));
				 response.setPostedDate(rs.getDate(5));
				 responses.add(response);
			 }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return responses;
		
	}
	
	public static List<String> getQueryResponseEntity(List<String> postIds){
		List<String> records = new ArrayList<String> ();
		
		if(conn == null)
			return records;
		
		StringBuilder selectIds = new StringBuilder();
		for(String id: postIds){
			selectIds.append(id);
			selectIds.append(',');
		}
		selectIds.deleteCharAt(selectIds.length()-1);
		selectIds.append(");");
		
		try {
			 Statement st = conn.createStatement();
			 
			 ResultSet rs = st.executeQuery(SELECT_QUERY_RESPONSE_ENTITY + selectIds.toString());
			 while(rs.next()){
				 	records.add(rs.getString(0));
			 }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return records;
		
	}
	

}
