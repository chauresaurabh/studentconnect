package com.studentconnect.posts;

import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;

import facebook4j.internal.org.json.JSONException;

public class insertPosts {
	List<QueryEntity> questions;
	List<ResponseEntity> answers;
	public static int query_id =  1;
	public static int response_id = 1;
	
	public insertPosts(){
		questions = new ArrayList<QueryEntity>();
		answers = new ArrayList<ResponseEntity>();
	} 
	
	
	public void parseRecord(String postJson){
		try{
			JSONObject post = new JSONObject(postJson);
			QueryEntity query = new QueryEntity(post.getString("id"), post.getString("question"));
			
			ResponseEntity response = new ResponseEntity(post.getString("id"), post.getString("ans"));
			
		}catch(Exception e){
			
		}
	}
}
