package com.studentconnect.posts;

import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import snowballstemmer.EnglishStemmer;
import snowballstemmer.SnowballStemmer;

public class ProcessPosts {
	
	Map<String, Map<String, Integer>> invertedIndex;
	private String delimeters = "[.,;\"!-()\\[\\]{}:?'/\\`~$%#@&*_=+]";
	private HashSet<String> stopWordsSet = new HashSet<String>();
	SnowballStemmer stemmer= new EnglishStemmer();
	
	public ProcessPosts(){
		
	}
	
	public List<String> removeStopWordsStem(String currentLine){
		List<String> tokens = new ArrayList<String>();
		String[] wordArray = currentLine.split(" ");
		String stemmedWord;
		for (String word : wordArray){
			word = word.replaceAll("<[^>]+>", "");
			word = word.replaceAll(delimeters, "");
			if(word.equals(""))
				continue;
			stemmedWord = "";
			if (!stopWordsSet.contains(word.toLowerCase())){
				stemmer.setCurrent(word);
				if(stemmer.stem())
					 stemmedWord = stemmer.getCurrent();
				if (!stemmedWord.equals(""))
					word = stemmedWord;
				tokens.add(word);
			}
		}
		return tokens;
	}
}
