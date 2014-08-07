package com.example.sjungbok;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	DownloadListener downloadListener;
	//TextView textView;
	ListView listView;
	HashSet<String> existingSongs;
	ArrayList<Song> songList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//textView = (TextView)findViewById(R.id.SongView);
//		Scroller scroller = new Scroller(this);
//		scroller.setFriction(1000);
//		textView.setScroller(scroller);
		existingSongs = new HashSet<String>();
		listView = (ListView)findViewById(R.id.SongView);

		if(!checkIfSongFileExists()){
			copyInitialSongFile();

		}
		populateSongListFromFile();
		downloadSongs();		



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private boolean checkIfSongFileExists(){
		File songFile = new File(this.getFilesDir()+File.separator+"Songs.txt");
		return songFile.exists();

	}
	private void copyInitialSongFile(){

		System.out.println(this.getFilesDir()+File.separator+"Songs.txt");

		BufferedWriter bufferedWriter;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.songlist),"UTF-8"));
			//bufferedWriter = new BufferedWriter(new FileWriter(new File(this.getFilesDir()+File.separator+"Songs.txt")));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getFilesDir()+File.separator+"Songs.txt"),"UTF-8"));
			String line=reader.readLine();
			while(line!=null){

				bufferedWriter.write(line+"\n");
				line=reader.readLine();
			}

			bufferedWriter.close();
			reader.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public void updateSongs(View v) {
		populateSongListFromFile();
	}
	private void populateSongListFromFile(){
		songList = new ArrayList<Song>();
		ArrayList<String> titleList= new ArrayList<String>();
		ArrayList<String> melodyList= new ArrayList<String>();
		ArrayList<String> lyricList= new ArrayList<String>();
		StringBuilder sb = new StringBuilder();

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(this.getFilesDir()+File.separator+"Songs.txt")),"UTF-8"));
			String line=reader.readLine();

			while(line!=null){


				if(line.equals("<title>")){
					line=reader.readLine();
					titleList.add(line);
					existingSongs.add(line);
					reader.readLine();
				}
				else if(line.equals("<melody>")){
					melodyList.add(reader.readLine());
					reader.readLine();
				}
				else if(line.equals("<lyrics>")){
					line=reader.readLine();

					sb = new StringBuilder();
					while(!line.equals("</lyrics>")){

						sb.append(line+"\n");
						line=reader.readLine();
					}
					lyricList.add(sb.toString());

				}

				line=reader.readLine();
			}
			reader.close();

		}  catch (Exception e) {
			e.printStackTrace();
			System.out.println("fel här?");
		}
		for(int i=0;i<titleList.size();i++){
			songList.add(new Song(titleList.get(i),melodyList.get(i),lyricList.get(i)));
		}
		populateListView();

		
	}

	private void populateListView(){
		ArrayAdapter<Song> adapter= new ArrayAdapter<Song>(this,android.R.layout.simple_list_item_1,songList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              Song clickedSong=songList.get(position);
              Intent intent = new Intent(view.getContext(), SongPane.class);
              intent.putExtra("song", clickedSong);
              startActivity(intent);
            }
    }); 
	}
	private void downloadSongs(){
		if(isNetworkAvailable()){
			downloadListener = new DownloadListener(existingSongs,this);
		}
		else{
			Toast toast = Toast.makeText(this, "Aktivera Internet för att hämta sångerna", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

}
