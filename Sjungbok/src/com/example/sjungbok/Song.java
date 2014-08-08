package com.example.sjungbok;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
//någon variabel för när sången senast blev uppdaterad 
	private String name;
	private String melody;
	private String text;
	
	public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {

		@Override
		public Song createFromParcel(Parcel source) {
			// Must read values in the same order as they were placed in
			String name = source.readString();
			String melody = source.readString();
			String text = source.readString();
			Song song = new Song(name, melody,text);
			return song;
		}

		@Override
		public Song[] newArray(int size) {
			return new Song[size];
		}

	};
	@Override
	public int describeContents() {
		
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(melody);
		dest.writeString(text);
	}
	
	
	
	
	
	
	
	
	
	
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
		result="<h4>"+replaceBackslashN(name)+"</h4>";
		result=result+"<i>"+replaceBackslashN(melody)+"</i><br><br>";
		result=result+replaceBackslashN(text)+"<br>";
		return result;
	}
	public String replaceBackslashN(String s){
		return s.replace("\n", "<br>");
	}
	public String toString(){
		return name;
	}
	public ArrayList<String> titleAsArrayList(){
		return getArrayListBySeperatingTextBySpace(name);
		
	}
	public ArrayList<String> melodyAsArrayList(){
		return getArrayListBySeperatingTextBySpace(melody);
		
	}
	public ArrayList<String> textAsArrayList(){
		return getArrayListBySeperatingTextBySpace(text);
		
	}
	private ArrayList<String> getArrayListBySeperatingTextBySpace(String text){
		String array[] = text.split(" ");
		ArrayList<String> result= new ArrayList<String>();
		for(int i=0;i<array.length;i++){
			result.add(array[i].toLowerCase());
		}
		return result;
		
	}
	
	
}
