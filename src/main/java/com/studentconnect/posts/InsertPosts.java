package com.studentconnect.posts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.studentconnect.datastore.DBAccess;


public class InsertPosts {
	List<QueryEntity> questions;
	List<ResponseEntity> responses;
	Map<String, List<String>> queryResponseMap;
	public static int query_id =  1;
	public static int response_id = 1;

	public static void main(String[] args) throws IOException{
		InsertPosts posts = new InsertPosts();
		String line = null;
		String input = "/home/niki/Desktop/Student Connect/Data Collection/stupidsid.txt";
		File src = new File(input);
		if(!src.exists())
			System.out.println("File not found");
		
		BufferedReader buf = new BufferedReader(new FileReader (src));
		while((line = buf.readLine())!= null){
			posts.parseRecord(line);
		}
		
		buf.close();
		
		DBAccess.getConnection();
		System.out.println(posts.questions.size());
		//DBAccess.saveQueryEntity(posts.questions);
		System.out.println(posts.responses.size());
		DBAccess.saveResponseEntity(posts.responses);
		System.out.println(posts.queryResponseMap.size());
		DBAccess.saveQAEntity(posts.queryResponseMap);
		DBAccess.destroyConnection();
	}
	
	
	public InsertPosts(){
		questions = new ArrayList<QueryEntity>();
		responses = new ArrayList<ResponseEntity>();
		queryResponseMap = new HashMap<String, List<String>>();
	} 
	
	
	public void parseRecord(String postJson){
		try{
			int replies = 0, i;
			ResponseEntity response = null;
			QueryEntity query = null;
			JSONArray answers = null;
			JSONObject temp = null;
			Date posted_date;
			DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH);
			
			List<String> answersMap = new ArrayList<String>();
			//System.out.println(postJson);
			JSONObject post = new JSONObject(postJson);
			
			query = new QueryEntity(String.valueOf(query_id), post.getString("ques"));
			query.setViews(post.getInt("views"));
			replies = post.getInt("replies");
			query.setReplies(replies);
			String date = post.getString("time");
			
			posted_date = df.parse(date);
			query.setPosted_date(posted_date);
			query.setTag(post.getString("tag"));
			
			
			answers = post.getJSONArray("ans");
			//System.out.println(post.toString());
			for(i=0;i<answers.length();i++){
				response = new ResponseEntity(String.valueOf(response_id), answers.getString(i));
				response.setPostedDate(posted_date);
				responses.add(response);
				answersMap.add(String.valueOf(response_id));
				response_id++;
			}
			queryResponseMap.put(String.valueOf(query_id), answersMap);
			questions.add(query);
		
			query_id++;
			
		}catch(Exception e){
			System.out.println("Exception in aprsing json " + e);
		}
	}
}
