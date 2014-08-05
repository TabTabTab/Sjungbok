package com.example.sjungbok;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;





public class DownloadListener implements AsyncTaskCompleteListener<String>{
	Downloader downloader;
	HashSet existingSongs;
	ArrayList<String> titleList;
	Context context;
	DownloadListener(HashSet existingSongs, Context context){
		this.existingSongs = existingSongs;
		this.context=context;
		downloader = new Downloader(existingSongs,context,this);
		downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	@Override
	public void onTaskComplete(String result) {
		BufferedWriter bufferedWriter;
		try {
			//bufferedWriter = new BufferedWriter(new FileWriter(new File(context.getFilesDir()+File.separator+"Songs.txt"),true));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(context.getFilesDir()+File.separator+"Songs.txt",true),"UTF-8"));
			;
			bufferedWriter.write(result);
			bufferedWriter.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		

	}


}
