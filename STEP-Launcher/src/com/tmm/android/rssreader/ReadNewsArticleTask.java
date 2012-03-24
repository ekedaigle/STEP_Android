package com.tmm.android.rssreader;

import android.os.AsyncTask;
import android.view.View;

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
		// TODO Auto-generated method stub
		
		
		return null;
	}

}
