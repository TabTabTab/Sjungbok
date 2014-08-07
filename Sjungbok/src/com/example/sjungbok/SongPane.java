package com.example.sjungbok;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class SongPane extends Activity {
	TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_song_pane);
		Intent intent = getIntent();
		textView = (TextView)findViewById(R.id.songTextView);
		Song song=(Song) intent.getSerializableExtra("song");
		textView.setText(song.songToString());
		//textView.setMovementMethod(new ScrollingMovementMethod());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.song_pane, menu);
		return true;
	}

}
