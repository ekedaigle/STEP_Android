package com.tmm.android.rssreader;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ReadNewsArticleTask extends AsyncTask<String, Void, String>{
	RssReader r;
	View v;
	int idx;
	JSONObject item;
	ReadNewsArticleTask(RssReader r1, View v1, int idx1){
	this.r = r1;
	this.v = v1;
	this.idx = idx1;
	item = this.r.getJobs().get(this.idx);
	}
	@Override
	protected String doInBackground(String... arg0) {
		String fullBody = null;
		return fullBody;
	}
	protected void onPostExecute(String result)
	{
		//String images = null;
		
		
		try {
			
			
			//images = (String) item.get("imageLink");
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
