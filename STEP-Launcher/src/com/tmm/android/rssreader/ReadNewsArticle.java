package com.tmm.android.rssreader;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ReadNewsArticle{
	RssReader r;
	View textView;
	View textHeader;
	int idx;
	JSONObject item;
	ReadNewsArticle(RssReader r1, View v1, View v2, int idx1){
	this.r = r1;
	this.textView = v1;
	this.textHeader = v2;
	this.idx = idx1;
	item = this.r.getJobs().get(this.idx);
	}
	protected void getArticle(String result)
	{
		//String images = null;
		String header = null;
	
		try {
			
			
			//images = (String) item.get("imageLink");
			header = (String) item.get("header").toString();
			result = (String) item.get("text").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	TextView t = (TextView) this.textView;
    	TextView th = (TextView) this.textHeader;
    	//Log.e("the image links are "," the images links are " + images);
    	//Log.e("the image links are "," the images count is " + images.length());
    	t.setText(result);
    	th.setText(header);
    	
	}
	protected void getTitle(String result)
	{
		//String images = null;
		
	
		try {
			
			
			//images = (String) item.get("imageLink");
			
			result = (String) item.get("header").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	TextView t = (TextView) this.textView;
    	//Log.e("the image links are "," the images links are " + images);
    	//Log.e("the image links are "," the images count is " + images.length());
    	t.setText(result);
    	
	}
	
}
