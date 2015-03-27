package com.studentconnect; 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerforNLP {

	public static void main(String[] args) throws IOException {
		
		List <String> urls = new ArrayList<String>();
		
		
		//JSONObject mainobject = new JSONObject();
		//JSONArray forumarray = new JSONArray();
 	    FileWriter writer = new FileWriter(new File("F:/crawl.txt"), true);
 	    
 	    //int numbers[] = new int[]{20,11,12,16,49,15,17,48,21,22,18,36,37,45,46,47};
 	    int numbers[] = new int[]{47};
 	    
		for (int kk = 0; kk < numbers.length; kk++) { 
		 
			int k = numbers[kk];
			String url = "http://www.msinus.com/f"+k+"/"; 
		
				//getUrlList(url,urls);
				
				JSONObject forumobject = new JSONObject();
				Document doc1 = null;
				try{
					 doc1 = Jsoup.connect(url).get();
				}catch(Exception e){
					continue;
				}
				System.out.println(url);
			    Elements  elements1 = doc1.getElementsByClass("title");
			    String forum_name = doc1.getElementsByTag("h1").get(0).text();
  
  			    forumobject.put("name", forum_name);
			    
			    for (int i = 0; i < elements1.size(); i++) {
			    	//if( !urls.contains( elements.get(i).attr("href") ))
			    	urls.add( elements1.get(i).attr("href") );
				}
		 		
			    try{
				   
			 		for (int i = 2; i <= 200 ; i++ ) {
			 			url = "http://www.msinus.com/forum"+k+"-"+i+".html";
			 		 	getUrlList(url,urls);
			 		}
			    }catch(Exception e){
			    	// do nothing
			    }
		 	    System.out.println( urls.size() );
 		 	    
			  JSONArray postarray = new JSONArray();
		
		 	    for (int i = 0; i <  urls.size() ; i++) {
		 	    	Document doc = null;
		 	    	try{
		 	    	  doc = Jsoup.connect(urls.get(i)).get();
		 	    	}catch(Exception e){
		 	    		continue;
		 	    	}
		 		    Elements  elements = doc.getElementsByClass("postcontent");
		 		    
			 		    JSONObject postobject = new JSONObject();
			
			 		    System.out.println(i);
			 		    //writer.write(  elements.get(0).text() +"\n");
			 			JSONArray answersarray = new JSONArray();
			 			 
			  		     for (int j = 1; j < elements.size(); j++) {
			  		    	answersarray.put(elements.get(j).text());// all replies
			 			} 
			  		   postobject.put("id", "");
			  		   postobject.put("head", "");
			  		   postobject.put("question", elements.get(0).text());
			  		   postobject.put("answers", answersarray);
			  		   postobject.put("replies", "");
					   postobject.put("views", "");
					   postobject.put("tag", forum_name);
					   postarray.put(postobject);
				
		 		}
		 	   writer.write(postarray+"\n");
			   writer.flush();
		 //	    forumobject.put("posts", postarray);
		 //	    forumarray.put(forumobject);
		 //	    mainobject.put("forums", forumarray);
		}
		
 	   // writer.write(mainobject+"\n");
  	    writer.close();
 	 }
	
	public static void getUrlList( String url, List<String> urls) throws IOException{
		Document doc = Jsoup.connect(url).get();
	    Elements  elements = doc.getElementsByClass("title");
	    for (int i = 0; i < elements.size(); i++) {
	    	//if( !urls.contains( elements.get(i).attr("href") ))
	    	urls.add( elements.get(i).attr("href") );
		}
	}
}
