/**
 * 
 */
package com.tmm.android.rssreader.reader;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;
import android.util.Log;

import com.tmm.android.rssreader.util.Article;
import com.tmm.android.rssreader.util.RSSHandler;

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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/*
		 * (new Thread(new Runnable() {
		 * 
		 * @Override public void run() { fetchPage(); } })).start();
		 */
		
		List<JSONObject> testing = new ArrayList<JSONObject>();

		
		
		(new Thread(new Runnable() {
			public void run() {
				List<JSONObject> test123 = new ArrayList<JSONObject>();
				RssReader reader = new RssReader();
				test123 = reader.getLatestRssFeed();
				Iterator<JSONObject> iterator = test123.iterator();
				while (iterator.hasNext()) {
					System.out.println(iterator.next());
					
				}
				System.out.println("test");
			}
		})).start();
		
		return inflater.inflate(R.layout.newspaper_fragment, container, false);
	}
}