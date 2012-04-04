/**
 * 
 */
package com.tmm.android.rssreader;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * @author rob
 * 
 */
public class RssReaderTask extends AsyncTask<String, Void, String> {
	RssReader r;
	View v;
	int newspaper_id;
	JSONObject item;

	RssReaderTask(RssReader r1, View v1, int feeder) {
		this.r = r1;
		this.v = v1;
		this.newspaper_id = feeder;
	}

	@Override
	protected String doInBackground(String... strings) {

		String feed_change = null;
		
		switch(newspaper_id)
		{
		case 0: feed_change ="http://fulltextrssfeed.com/news.google.com/news?ned=us&topic=h&output=rss";
		
		break;
		case 1: feed_change = "http://fulltextrssfeed.com/news.google.com/news?ned=us&topic=w&output=rss";
		break;
		case 2: feed_change = "http://fulltextrssfeed.com/dir.yahoo.com/rss/dir/getrss.php?gov";
		break;
		case 3: feed_change = "http://fulltextrssfeed.com/sports.yahoo.com/mlb/teams/bos/rss.xml";
		break;
		case 4: feed_change = "http://fulltextrssfeed.com/finance.yahoo.com/rss/topfinstories";;
		break;
		
		}
		
		
		
		
		/*
		switch(newspaper_id)
		{
		case 0: feed_change ="http://www.rssez.com/get/news.google.com/news?ned=us&topic=h&output=rss";
		
		break;
		case 1: feed_change = "http://www.rssez.com/get/news.google.com/news?ned=us&topic=w&output=rss";
		break;
		case 2: feed_change = "http://www.rssez.com/get/dir.yahoo.com/rss/dir/getrss.php?gov";
		break;
		case 3: feed_change = "http://www.rssez.com/get/sports.yahoo.com/mlb/teams/bos/rss.xml";
		break;
		case 4: feed_change = "http://www.rssez.com/get/finance.yahoo.com/rss/topfinstories";;
		break;
		
		}
		
		*/
		
		
		
		
		
		
		
		//case 0: feed_change = "http://www.rssez.com/get/news.google.com/news?ned=us&topic=h&output=rss";
		
		// String feed ="http://fulltextrssfeed.com/news.google.com/news?ned=us&topic=h&output=rss";
		// finance = "http://fulltextrssfeed.com/finance.yahoo.com/rss/topfinstories";
		// government = "http://fulltextrssfeed.com/www.google.com/news/section?pz=1&cf=all&ned=us&topic=m&ict=ln"
		//sports = "http://fulltextrssfeed.com/sports.yahoo.com/mlb/teams/bos/rss.xml"
		//health = "http://fulltextrssfeed.com/www.google.com/news/section?pz=1&cf=all&ned=us&topic=m&ict=ln"
		// String feed ="http://fulltextrssfeed.com/rss.cnn.com/rss/cnn_topstories.rss";

		
		RSSHandler rh = new RSSHandler();

		List<Article> articles = rh.getLatestArticles(feed_change);

		
		
		r.fillData(articles);
		
		return "";

	}

	@Override
	protected void onPostExecute(String result){
		
		try {
			
			this.r.readArticle(0, v);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
