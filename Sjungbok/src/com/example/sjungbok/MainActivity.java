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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

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
	static ArrayList<Song> allSongsList;
	//SongListWrapper songListWrapper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//textView = (TextView)findViewById(R.id.SongView);
		//		Scroller scroller = new Scroller(this);
		//		scroller.setFriction(1000);
		//		textView.setScroller(scroller);
		//		existingSongs = new HashSet<String>();
		//		listView = (ListView)findViewById(R.id.SongView);
		//
		//		if(!checkIfSongFileExists()){
		//			copyInitialSongFile();
		//
		//		}
		//		populateSongListFromFile();
		//		downloadSongs();		



	}
	protected void onResume()
	{
		super.onResume();

		existingSongs = new HashSet<String>();
		listView = (ListView)findViewById(R.id.SongView);
		Intent intent = getIntent();
		String title;
		String melody;
		String lyric;
		title=intent.getStringExtra("title");
		melody=intent.getStringExtra("melody");
		lyric=intent.getStringExtra("lyrics");
		if(title==null&&melody==null&&lyric==null){
			if(!checkIfSongFileExists()){
				copyInitialSongFile();

			}
			populateSongListFromFile();
			downloadSongs();
		}
		else{
			//genomför en sökning
			String searchString;
			boolean titleSearch=false;
			boolean lyricsSearch=false;
			if(title!=null){
				searchString=title;
				titleSearch=true;
			}
			else if(melody!=null){
				searchString=melody;
			}
			else{
				searchString=lyric;
				lyricsSearch=true;
			}
			System.out.println(searchString);
			String[] array=searchString.split(" ");
			ArrayList<String> searchStringSplitted=new ArrayList<String>(Arrays.asList(array));
			searchSongs2(searchStringSplitted,titleSearch,lyricsSearch);
			
			
			
			
			
			
			
			//populateListView();
		}
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
	public void searchSongs(View v) {
		Intent intent = new Intent(this, SearchActivity.class);		
		startActivity(intent);
		
		
		
		
		
	}
	public void searchSongs2(ArrayList<String> searchString,boolean titleSearch,boolean lyricsSearch) {			
		ArrayList<SongAndMatching> songWithMatching = new ArrayList<SongAndMatching>();
		ArrayList<SongAndMatching> songWithMatchingFullMatch = new ArrayList<SongAndMatching>();



		//bara lägga in de i sökresultaten om de matchar alla orden? (kanske bara lägga in de som matchar alla orden om det finns någon sån match? annars ta del matcher?)
		int score;
		boolean fullMatch=false;
		for(int i=0;i<allSongsList.size();i++){
			if(titleSearch){
				score=Search.SimpleSearchFuzzy(searchString, allSongsList.get(i).titleAsArrayList());
			}
			else if(lyricsSearch){
				score=Search.SimpleSearchFuzzy(searchString, allSongsList.get(i).textAsArrayList());
			}
			else{
				score=Search.SimpleSearchFuzzy(searchString, allSongsList.get(i).melodyAsArrayList());
			}
			
			
			if(score==allSongsList.get(i).titleAsArrayList().size()&&titleSearch&&allSongsList.get(i).titleAsArrayList().size()==searchString.size()){
				System.out.println(searchString.size());
				System.out.println(allSongsList.get(i).titleAsArrayList().size());
				Intent intent = new Intent(this, SongPane.class);
				intent.putExtra("Song", allSongsList.get(i));
				startActivity(intent);
				return;
			}
			if(score==searchString.size()){
				songWithMatchingFullMatch.add(new SongAndMatching(score,allSongsList.get(i)));

			}
			if(score!=0){
				songWithMatching.add(new SongAndMatching(score,allSongsList.get(i)));
			}
		}
		if(songWithMatchingFullMatch.size()!=0){
			songWithMatching=songWithMatchingFullMatch;
		}
		Collections.sort(songWithMatching, new Comparator<SongAndMatching>() {
			@Override
			public int compare(SongAndMatching  a, SongAndMatching  b)
			{

				if(a.matching==b.matching){
					return 0;
				}
				else if(a.matching<b.matching){
					return -1;
				}
				else {
					return 1;
				}


			}
		});
		songList= new ArrayList<Song>();
		for(int i=0;i<songWithMatching.size();i++){
//			System.out.println(songWithMatching.get(i).song.toString());
//			System.out.println(songWithMatching.get(i).matching);
			songList.add(songWithMatching.get(i).song);
		}
		Collections.reverse(songList);
		ArrayAdapter<Song> adapter= new ArrayAdapter<Song>(this,android.R.layout.simple_list_item_1,songList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Song clickedSong=songList.get(position);
				Intent intent = new Intent(view.getContext(), SongPane.class);
				intent.putExtra("Song", clickedSong);
				startActivity(intent);
			}
		}); 



	}
	private void populateSongListFromFile(){
		songList = new ArrayList<Song>();
		allSongsList= new ArrayList<Song>();
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
			allSongsList.add(new Song(titleList.get(i),melodyList.get(i),lyricList.get(i)));
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
				intent.putExtra("Song", clickedSong);
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
