package com.tmm.android.rssreader;

import java.util.ArrayList;
import org.json.JSONObject;
public class RssReaderCopy {
	
		
	private ArrayList<JSONObject> jobs;
	public ArrayList<JSONObject> getJobs(){
		return jobs;
	}
	public void setJobs(ArrayList<JSONObject> job){
		jobs = job;
	}
	RssReaderCopy(){
		jobs = new ArrayList<JSONObject>();
	}
}