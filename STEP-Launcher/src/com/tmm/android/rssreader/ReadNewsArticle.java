package com.tmm.android.rssreader;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ReadNewsArticle{
	RssReader r;
	View v;
	int idx;
	JSONObject item;
	ReadNewsArticle(RssReader r1, View v1, int idx1){
	this.r = r1;
	this.v = v1;
	this.idx = idx1;
	item = this.r.getJobs().get(this.idx);
	}
	protected void getArticle(String result)
	{
		//String images = null;
		
		String header;
		try {
			
			
			//images = (String) item.get("imageLink");
			header = (String) item.get("header").toString();
			result = (String) item.get("text").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	TextView t = (TextView) this.v;
    	//Log.e("the image links are "," the images links are " + images);
    	//Log.e("the image links are "," the images count is " + images.length());
    	t.setText(result);
    	
	}
	
}
