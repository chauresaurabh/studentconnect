package com.studentconnect.posts;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


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
			int replies = 0, i;
			ResponseEntity response = null;
			QueryEntity query = null;
			JSONArray answers = null;
			JSONObject temp = null;
			Map<String, List<String>> queryResponseMap = new HashMap<String, List<String>>();
			List<String> answersMap = new ArrayList<String>();
			
			JSONObject post = new JSONObject(postJson);
			query = new QueryEntity(String.valueOf(query_id), post.getString("question"));
			query.setViews(post.getInt("views"));
			replies = post.getInt("replies");
			query.setReplies(replies);
			query.setPosted_date(new Date((long) post.get("time")));
			query.setTag(post.getString("tag"));
			
			
			answers = post.getJSONArray("ans");
			
			for(i=0;i<replies;i++){
				response = new ResponseEntity(String.valueOf(response_id), answers.getString(i));
				answersMap.add(String.valueOf(response_id));
				response_id++;
			}
			queryResponseMap.put(String.valueOf(query_id), answersMap);
			query_id++;
			
		}catch(Exception e){
			
		}
	}
}
