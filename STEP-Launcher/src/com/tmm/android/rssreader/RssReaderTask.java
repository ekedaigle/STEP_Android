/**
 * 
 */
package com.tmm.android.rssreader;

import java.util.List;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

/**
 * @author rob
 * 
 */
public class RssReaderTask extends AsyncTask<String, Void, String> {
	RssReader r;
	View v;
	String feed_change;

	RssReaderTask(RssReader r1, View v1, String feeder) {
		r = r1;
		v = v1;
		feed_change = feeder;
	}

	@Override
	protected String doInBackground(String... strings) {

		if(feed_change == null){
		// String feed ="http://fulltextrssfeed.com/news.google.com/news?ned=us&topic=h&output=rss";
		// finance = "http://fulltextrssfeed.com/finance.yahoo.com/rss/topfinstories";
		// government = "http://fulltextrssfeed.com/www.google.com/news/section?pz=1&cf=all&ned=us&topic=m&ict=ln"
		//sports = "http://fulltextrssfeed.com/sports.yahoo.com/mlb/teams/bos/rss.xml"
		//health = "http://fulltextrssfeed.com/www.google.com/news/section?pz=1&cf=all&ned=us&topic=m&ict=ln"
		feed_change = "http://fulltextrssfeed.com/news.google.com/news?ned=us&topic=h&output=rss";
		// String feed ="http://fulltextrssfeed.com/rss.cnn.com/rss/cnn_topstories.rss";

		}
		RSSHandler rh = new RSSHandler();

		List<Article> articles = rh.getLatestArticles(feed_change);

		r.fillData(articles);

		return "";

	}

	@Override
	protected void onPostExecute(String result) {

		TextView con = (TextView) v;
		this.r.displayArticles();

	}

}
