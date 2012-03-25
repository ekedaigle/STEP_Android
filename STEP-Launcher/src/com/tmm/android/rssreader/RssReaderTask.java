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

	RssReaderTask(RssReader r1, View v1) {
		r = r1;
		v = v1;
	}

	@Override
	protected String doInBackground(String... strings) {

		// String feed ="http://fulltextrssfeed.com/news.google.com/news?ned=us&topic=h&output=rss";
		// finance = "http://fulltextrssfeed.com/finance.yahoo.com/rss/topfinstories";
		// government = "http://fulltextrssfeed.com/www.google.com/news/section?pz=1&cf=all&ned=us&topic=m&ict=ln"
		
		//health = "http://fulltextrssfeed.com/www.google.com/news/section?pz=1&cf=all&ned=us&topic=m&ict=ln"
		String feed = "http://fulltextrssfeed.com/www.google.com/news/section?pz=1&cf=all&ned=us&topic=m&ict=ln";
		// String feed ="http://fulltextrssfeed.com/rss.cnn.com/rss/cnn_topstories.rss";

		RSSHandler rh = new RSSHandler();

		List<Article> articles = rh.getLatestArticles(feed);

		r.fillData(articles);

		return "";

	}

	@Override
	protected void onPostExecute(String result) {

		TextView con = (TextView) v;
		this.r.displayArticles();

	}

}
