package com.example.sjungbok;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class Search {
//	public static double fuzzyMatchResult(ArrayList<String> searchWords,ArrayList<String> textToSearch){
//		ArrayList<Double> matches = new ArrayList<Double>();
//
//		for(int i=0;i<searchWords.size();i++){
//			matches.add(Double.MIN_VALUE);
//			for(int j=0;j<textToSearch.size();j++){
//				double match=StringUtils.getJaroWinklerDistance(searchWords.get(i), textToSearch.get(j));
//				if(match>matches.get(matches.size()-1)){
//					matches.set(matches.size()-1, match);
//				}
//			}
//
//		}
//		double average = 0;
//		for(int i=0;i<matches.size();i++){
//			average+=matches.get(i);
//		}
//		average=average/matches.size();
//		return average;
//
//	}

	//lägga till en metod som denna som matchar om de är 75% eller 80% matchning?
	public static int SimpleSearch(ArrayList<String> searchWords,ArrayList<String> textToSearch){
		int score=0;
		for(int i=0;i<searchWords.size();i++){
			for(int j=0;j<textToSearch.size();j++){
				if(searchWords.get(i).equals(textToSearch.get(j))){
					score++;
					break;
				}
			}

		}
		return score;
	}

	public static int SimpleSearchFuzzy(ArrayList<String> searchWords,ArrayList<String> textToSearch){
		int score=0;
		for(int i=0;i<searchWords.size();i++){
			for(int j=0;j<textToSearch.size();j++){
				if(StringUtils.getJaroWinklerDistance(searchWords.get(i), textToSearch.get(j))>=0.85){
					System.out.println(StringUtils.getJaroWinklerDistance(searchWords.get(i), textToSearch.get(j)));
					score++;
					break;
				}

			}
		}
		return score;
	}
}
