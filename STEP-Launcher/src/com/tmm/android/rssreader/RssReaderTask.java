/**
 * 
 */
package com.tmm.android.rssreader;

import java.util.List;

import android.os.AsyncTask;

/**
 * @author rob
 * 
 */
public class RssReaderTask extends AsyncTask<String, Void, String> {
	RssReader r;
	//View v;
	int newspaper_id;
	

	RssReaderTask(RssReader r1, int feeder) {
		r = r1;
		//v = v1;
		newspaper_id = feeder;
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
		case 2: feed_change = "http://fulltextrssfeed.com/www.google.com/news/section?pz=1&cf=all&ned=us&topic=m&ict=ln";
		break;
		case 3: feed_change = "http://fulltextrssfeed.com/sports.yahoo.com/mlb/teams/bos/rss.xml";
		break;
		case 4: feed_change = "http://fulltextrssfeed.com/finance.yahoo.com/rss/topfinstories";;
		break;
		
		}
		
		
		
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
	protected void onPostExecute(String result) {

		//TextView con = (TextView) v;
		try
		{
			this.r.displayArticles();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		
		
		
		
		
		//this.r.displayArticles();

		
	}

}
