package com.example.sjungbok;




import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class Downloader extends AsyncTask<String, Void, String>{
	ProgressDialog progressDialog;
	Context context;
	private AsyncTaskCompleteListener<String> callback;
	public Downloader(Context context,AsyncTaskCompleteListener<String> callback){
		this.context=context;
		this.callback=callback;
	}


	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		progressDialog = ProgressDialog.show(context, "Hämtar Musiken","", true);          
	}; 

	@Override
	protected String doInBackground(String... params) {
		
		
		
		String titel = "";
		String melodi1="";
		String text="";
		boolean firstTitle=true;
		boolean firstMelody=true;
		boolean firstLyrics=true;
		
		ArrayList<String> songLinkList= new ArrayList();
		Document doc = null;
		Connection.Response res = null;
		try {
			res = Jsoup.connect("http://www.dsek.se/navigation/login.php")
					.data("login", "dat11ffo", "password", "folke123")
					.method(Method.POST)
					.execute();
		} catch (IOException e1) {
			e1.printStackTrace();
		}




		try {		
			Map<String, String> cookies = res.cookies();
			Connection connection = Jsoup.connect("http://www.dsek.se/arkiv/sanger/index.php?showAll");
			for (Map.Entry<String, String> cookie : cookies.entrySet()) {
				connection.cookie(cookie.getKey(), cookie.getValue());     
			}
			doc = connection.get();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element tableWithSongs=doc.getElementById("innerMainTable");
		Elements links = tableWithSongs.select("a[href]");


		Iterator<Element> itr = links.iterator();
		while(itr.hasNext()){
			Element element = itr.next();
			songLinkList.add(element.attr("abs:href"));
			String title=element.html();
			title=title.replace("&aring;", "å");
			title=title.replace("&auml;", "ä");
			title=title.replace("&ouml;", "ö");
			if(firstTitle){
				titel=title;
				firstTitle=false;
			}
		}


		for(int i=0; i<songLinkList.size();i++){
			try {
				Map<String, String> cookies = res.cookies();
				Connection connection = Jsoup.connect(songLinkList.get(i));
				for (Map.Entry<String, String> cookie : cookies.entrySet()) {
					connection.cookie(cookie.getKey(), cookie.getValue());     
				}
				doc = connection.get();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Elements melodier=doc.getElementsContainingOwnText("Melodi");
			Element melodi=melodier.get(0);
			Element parent = melodi.parent();
			Element child=parent.child(1);
			String melodiString="";

			child.html(child.html().replaceAll("(?i)<br[^>]*>", "br2n"));
			if(child.text().contains("Direktlänk till ljudfilen")){
				melodiString = child.text().substring(0, child.text().indexOf("Direktlänk till ljudfilen."));	
			}
			else{
				melodiString = child.text();
			}
			if(firstMelody){
				melodi1=melodiString;
				firstMelody=false;
			}
			
			Element lyrics=doc.getElementById("lyrics");
			lyrics.html(lyrics.html().replaceAll("(?i)<br[^>]*>", "br2n"));

			//hämta som innerhtml istället för att få med formatering?
			String lyricText=lyrics.text().replaceAll("br2n", "\n");

			String[] lyricsLines=lyricText.split("\n");
			lyricText="";
			for(int j=0;j<lyricsLines.length;j++){
				lyricText=lyricText+lyricsLines[j].trim()+"\n";
			}
			if(firstLyrics){
				firstLyrics=false;
				text=lyricText;
			}
			String result=titel+"\n\n"+melodi1+"\n\n"+text;
			return result;
		}
		
		
		
		
		
		
		
		
		

		return null;
	}
	@Override
	protected void onPostExecute(String song) {
		 progressDialog.dismiss();
		 callback.onTaskComplete(song);
	}

}
