/**
 * 
 */
package com.tmm.android.rssreader.reader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;



/**
 * @author rob
 *
 */

import com.step.launcher.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewspaperFragment extends Fragment {
	RssReader reader;
	
	List<JSONObject> rssreader = new ArrayList<JSONObject>();

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			final Bundle savedInstanceState) {

		View V = inflater.inflate(R.layout.newspaper_fragment, container, false);
		V.findViewById(R.id.job_text);

		
		Log.v("somethign worked","finally");

		
		Runnable r1= new Runnable() {
			  public void run() {
				  rssreader = RssReader.getLatestRssFeed();

			  }
			};
			Thread thr1 = new Thread(r1);
			thr1.start();
			
			
/*
		(new Thread(new Runnable() {
			public void run(){
				
				
				
				

		
		}
	})).start();
	*/	
		
	
		

		/*
		 * (new Thread(new Runnable() {
		 * 
		 * @Override public void run() { fetchPage(); } })).start();
		 */

		/*
		 * List<JSONObject> testing = new ArrayList<JSONObject>();
		 * 
		 * (new Thread(new Runnable() {
		 * 
		 * public void run() {
		 * 
		 * 
		 * 
		 * activity1.test();
		 * 
		 * } })).start();
		 */
		/*
		 * (new Thread(new Runnable() { public void run() {
		 * 
		 * 
		 * List<JSONObject> test123 = new ArrayList<JSONObject>(); RssReader
		 * reader = new RssReader(); test123 = reader.getLatestRssFeed();
		 * Iterator<JSONObject> iterator = test123.iterator(); while
		 * (iterator.hasNext()) { System.out.println(iterator.next());
		 * 
		 * } System.out.println("test"); } })).start();
		 */
		return inflater.inflate(R.layout.newspaper_fragment, container, false);
	}
}