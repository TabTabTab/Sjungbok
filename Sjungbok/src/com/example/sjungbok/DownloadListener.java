package com.example.sjungbok;

import android.content.Context;
import android.widget.TextView;





public class DownloadListener implements AsyncTaskCompleteListener<String>{
	Downloader downloader;
	TextView textView;
	DownloadListener(TextView textView, Context context){
		this.textView=textView;
		downloader = new Downloader(context,this);
		downloader.execute("");
	}
	@Override
	public void onTaskComplete(String result) {
		textView.setText(result);
		
	}

}
