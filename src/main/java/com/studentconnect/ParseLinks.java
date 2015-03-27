package com.studentconnect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ParseLinks {

	public static void main(String[] args) throws Exception {
		
		BufferedReader reader = new BufferedReader(new FileReader("F://samplelinks.txt"));
		FileWriter writer = new FileWriter(new File("F:/finallinks.txt"));

		String str = null;
		int cnt =1;
		while ((str = reader.readLine()) != null) {
			//System.out.println(cnt++);
			 
			writer.write(str.substring(str.indexOf("uid=")+4,
					str.indexOf("\"", str.indexOf("uid="))) +"\n");  
			writer.flush();
 		}
	}
	
}
