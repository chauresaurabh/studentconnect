package com.studentconnect; 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

public class NLPCrawler {

	public static void main(String[] args) throws IOException {

		List <String> urls = new ArrayList<String>();
		FileWriter writer = new FileWriter(new File("F:/crawlnlp.txt"));

		//String url = "http://www.edulix.com/unisearch/index.php?&ap=0&pr=1&ma=0&te=0&ye=0";
		

		BufferedReader reader = new BufferedReader(new FileReader("F://finallinks.txt"));
		String str = null;
		int cnt = 1;
		while ((str = reader.readLine()) != null) {
			String url = "http://www.edulix.com/unisearch/user.php?uid=" + str;
			
			System.out.println(cnt++);
			JSONObject mainobject = new JSONObject();
			Document doc1 = null;
			try{
				doc1 = Jsoup.connect(url).get();

				Element em = doc1.getElementById("page");
				Elements ems = em.getElementsByClass("tdborder");
				//System.out.println(em);
				Element em1 = ems.get(0);
				Elements rows = em1.getElementsByTag("tr");

				mainobject.put("pgm", rows.get(5).getElementsByTag("td").get(1).text());
				mainobject.put("mjr", rows.get(6).getElementsByTag("td").get(1).text());

				/*	System.out.println("Program : "+rows.get(5).getElementsByTag("td").get(1).text());
			System.out.println("Major : "+rows.get(6).getElementsByTag("td").get(1).text()); */
				if( rows.get(9).getElementsByTag("tbody").size()==0){
					continue;
				}
				Elements score = rows.get(9).getElementsByTag("tbody").get(0).getElementsByTag("tr");
				Elements grescore = score.get(0).getElementsByTag("td"); 
				/*	System.out.println("Quant : " + grescore.get(2).text());
			System.out.println("Verbal : " + grescore.get(4).text());
			System.out.println("AWA : " + grescore.get(6).text()); */

				mainobject.put("qnt", grescore.get(2).text());
				mainobject.put("vrb", grescore.get(4).text());
				mainobject.put("awa", grescore.get(6).text());

				/*grescore = score.get(1).getElementsByTag("td"); 	For MIS
			System.out.println("Quant : " + grescore.get(2).text());
			System.out.println("Verbal : " + grescore.get(4).text());
			System.out.println("AWA : " + grescore.get(6).text()); */
				grescore = score.get(2).getElementsByTag("td"); 
				/*System.out.println("Toefl : " + grescore.get(2).text());
			System.out.println("Grade : "+rows.get(17).getElementsByTag("td").get(1).text());
			System.out.println("Topper : "+rows.get(18).getElementsByTag("td").get(1).text());
			System.out.println("Scale : "+rows.get(19).getElementsByTag("td").get(1).text()); */

				mainobject.put("tof", grescore.get(2).text());
				mainobject.put("grd", rows.get(17).getElementsByTag("td").get(1).text());
				mainobject.put("top", rows.get(18).getElementsByTag("td").get(1).text());
				mainobject.put("scl", rows.get(19).getElementsByTag("td").get(1).text());

				JSONArray array = new JSONArray();
				Elements univs = ems.get(1).getElementsByTag("tbody").get(0).getElementsByTag("tr");
				 
				for(Element univ : univs){
					Elements entry = univ.getElementsByTag("td");
					if( entry.size() == 2 ){
						//System.out.println( "Univ : "+ entry.get(0).text() + " -- " + entry.get(1).text());
						JSONObject obj = new JSONObject();
						obj.put("nam", entry.get(0).text() );
						obj.put("stat", entry.get(1).text() );
						array.put(obj);
					}
				}
				if(array.length()==0)
						continue;
				mainobject.put("univs", array );
				//System.out.println(rows.get(6));
				writer.write(mainobject.toString()+"\n");
				writer.flush();

			}catch(Exception e){
				e.printStackTrace();
			}
			//writer.write(doc1.toString());
		}
			reader.close();
	}

}
