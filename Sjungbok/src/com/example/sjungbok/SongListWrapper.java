package com.example.sjungbok;

import java.io.Serializable;
import java.util.ArrayList;

public class SongListWrapper implements Serializable {

	   private ArrayList<Song> songs;

	   public SongListWrapper(ArrayList<Song> songs) {
	      this.songs = songs;
	   }

	   public ArrayList<Song> getSongs() {
	      return this.songs;
	   }

	}