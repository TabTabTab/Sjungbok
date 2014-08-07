package com.example.sjungbok;

import java.io.Serializable;

public class Song implements Serializable{
//någon variabel för när sånen senast blev uppdaterad 
	private String name;
	private String melody;
	private String text;
	
	public Song(String name,String melody,String text){
		this.name=name;
		this.melody=melody;
		this.text=text;
	}
	public String writeToFileFormat(){
		String result ="";
		result="<title>"+name+"</title>\n";
		result=result+"<melody>"+melody+"</melody>\n";
		result=result+"<lyrics>"+text+"</lyrics>\n";
		return result;
	}
	public String songToString(){
		String result ="";
		result=name+"\n\n";
		result=result+melody+"\n\n";
		result=result+text+"\n";
		return result;
	}
	public String toString(){
		return name;
	}
}
