package com.tmm.android.rssreader;

import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

public class ReadNewsArticleTask extends AsyncTask<String, Void, String>{
	RssReader r;
	View v;
	int idx;
	ReadNewsArticleTask(RssReader r1, View v1, int idx1){
	this.r = r1;
	this.v = v1;
	this.idx = idx1;	
	}
	@Override
	protected String doInBackground(String... arg0) {
		String fullBody = null;
		try
		{
			ArrayList<JSONObject> article_items;
			JSONObject item;
			article_items = this.r.getJobs();
			item = article_items.get(this.idx);
			fullBody = (String) item.get("text").toString();
			//Spanned text = (Spanned) item.get("header");
			return fullBody;
           }
		
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		return "Failed";
	}
	protected void onPostExecute(String result)
	{
    	TextView t = (TextView) this.v;
    	t.setText(result);
	}
	
	
	
}
