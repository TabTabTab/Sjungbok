package com.example.sjungbok;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SongParser {
	ArrayList<String> songLinkList= new ArrayList();

	public SongParser(){
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
			System.out.println(melodiString);
			
			Element lyrics=doc.getElementById("lyrics");
			lyrics.html(lyrics.html().replaceAll("(?i)<br[^>]*>", "br2n"));

			//hämta som innerhtml istället för att få med formatering?
			String lyricText=lyrics.text().replaceAll("br2n", "\n");

			String[] lyricsLines=lyricText.split("\n");
			lyricText="";
			for(int j=0;j<lyricsLines.length;j++){
				lyricText=lyricText+lyricsLines[j].trim()+"\n";
			}
			System.out.println(lyricText);
		}
	}


}
