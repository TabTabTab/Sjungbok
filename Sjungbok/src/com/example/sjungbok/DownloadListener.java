package com.example.sjungbok;

import java.util.ArrayList;

import android.content.Context;
import android.widget.TextView;





public class DownloadListener implements AsyncTaskCompleteListener<String>{
	Downloader downloader;
	TextView textView;
	ArrayList<String> titleList;
	DownloadListener(TextView textView, Context context){
		this.textView=textView;
		downloader = new Downloader(context,this);
		downloader.execute();
	}
	@Override
	public void onTaskComplete(String result) {
		titleList=new ArrayList();
		String text="";
		String[] splitted=result.split("\n");
		for(int i=0;i<splitted.length;i++){
			if(splitted[i].contains("<title>")){
				String title =splitted[i].substring(7, splitted[i].indexOf("</title>"));
				title=title+"\n";
				text=text+title;
			}
		}
		
		textView.setText(text);
		
	}
	

}
