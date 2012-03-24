/**
 * 
 */
package com.tmm.android.rssreader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import step.email.Mail;
import step.email.UpdateInboxTask;

import android.os.AsyncTask;
import android.text.Html;
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
	RssReaderTask(RssReader r1, View v1){
		r = r1;
		v = v1;
	}

	@Override
	protected String doInBackground(String...strings){

		
		String feed = "http://fulltextrssfeed.com/rss.cnn.com/rss/cnn_topstories.rss";

		RSSHandler rh = new RSSHandler();
		
		List<Article> articles =  rh.getLatestArticles(feed);
		
		
		r.fillData(articles);
		
		
		return "";
		
	}
	
	
	@Override
	protected void onPostExecute(String result)
	{
		
				
		TextView con = (TextView) v;
		this.r.displayArticles();
		
	}
	
	
	
	
	
	
	
	
	
	
}
