package com.example.sjungbok;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class SearchActivity extends Activity {
	
	

	EditText titleSearchString;
	EditText melodySearchString;
	EditText lyricSearchString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search);
		
		
		//allSongs=songListWrapper.getSongs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	public void searchForSongs(View v){
		titleSearchString   = (EditText)findViewById(R.id.editText1);
		melodySearchString   = (EditText)findViewById(R.id.editText2);
		lyricSearchString   = (EditText)findViewById(R.id.editText3);
		if(titleSearchString.getText().length()==0&&melodySearchString.getText().length()==0&&lyricSearchString.getText().length()==0){
			System.out.println("skriv in ett värde");
		}
		else if(titleSearchString.getText().length()!=0&&melodySearchString.getText().length()!=0){
			System.out.println("fyll bara i ett av fälten");
		}
		else if(titleSearchString.getText().length()!=0&&lyricSearchString.getText().length()!=0){
			System.out.println("fyll bara i ett av fälten");
		}
		else if(melodySearchString.getText().length()!=0&&lyricSearchString.getText().length()!=0){
			System.out.println("fyll bara i ett av fälten");
		}
		else{
			System.out.println("korrekt ifyllda fält");
			Intent intent = new Intent(this, MainActivity.class);
			if(titleSearchString.getText().length()!=0){
				intent.putExtra("title", titleSearchString.getText().toString().toLowerCase().trim());
			}
			if(melodySearchString.getText().length()!=0){
				intent.putExtra("melody", melodySearchString.getText().toString().toLowerCase().trim());
			}
			if(lyricSearchString.getText().length()!=0){
				intent.putExtra("lyrics", lyricSearchString.getText().toString().toLowerCase().trim());
			}
			startActivity(intent);
			
			
		}

	}
















}
