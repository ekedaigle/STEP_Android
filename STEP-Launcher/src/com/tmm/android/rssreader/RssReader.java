package com.tmm.android.rssreader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.step.launcher.R;

import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.widget.ListView;

public class RssReader {
	
	private final static String BOLD_OPEN = "<B>";
	private final static String BOLD_CLOSE = "</B>";
	private final static String BREAK = "<BR>";
	private final static String ITALIC_OPEN = "<I>";
	private final static String ITALIC_CLOSE = "</I>";
	private final static String SMALL_OPEN = "<SMALL>";
	private final static String SMALL_CLOSE = "</SMALL>";
	
	private ArrayList<JSONObject> jobs;
	private Activity activity; 
	private ListView newspaper_listView;
	
	public ArrayList<JSONObject> getJobs(){
		return this.jobs;
	}
	
	RssReader(Activity a, ListView lv){
		this.jobs = new ArrayList<JSONObject>();
		this.activity = a;
		this.newspaper_listView = lv;
	}
	
	public ArrayList<JSONObject> getLatestRssFeed(){
		//String feed = "http://fulltextrssfeed.com/news.google.com/news?ned=us&topic=h&output=rss";
		String feed = "http://fulltextrssfeed.com/rss.cnn.com/rss/cnn_topstories.rss";
		
		RSSHandler rh = new RSSHandler();
		List<Article> articles =  rh.getLatestArticles(feed);
		
		fillData(articles);
		return this.getJobs();
	}
	
	
	/**
	 * This method takes a list of Article objects and converts them in to the 
	 * correct JSON format so the info can be processed by our list view
	 * 
	 * @param articles - list<Article>
	 * @return List<JSONObject> - suitable for the List View activity
	 */
	public void fillData(List<Article> articles) {

        for (Article article : articles) {
            JSONObject current = new JSONObject();
            try {
            	buildJsonObject(article, current);
			} catch (JSONException e) {
				Log.e("RSS ERROR", "Error creating JSON Object from RSS feed");
			}
			this.jobs.add(current);
        }

	}
	
	public void displayArticles(){
		RssListAdapter adapter = new RssListAdapter(this.activity, R.layout.news_list_element, this.jobs);
		 
		this.newspaper_listView.setAdapter(adapter);
	}


	/**
	 * This method takes a single Article Object and converts it in to a single JSON object
	 * including some additional HTML formating so they can be displayed nicely
	 * 
	 * @param article
	 * @param current
	 * @throws JSONException
	 */
	private void buildJsonObject(Article article, JSONObject current) throws JSONException {
		String title = article.getTitle();
		String description = article.getDescription();
		String date = article.getPubDate();
		String imgLink = article.getImgLink();
		
		StringBuffer sb = new StringBuffer();
		sb.append(BOLD_OPEN).append(title).append(BOLD_CLOSE);
		sb.append(BREAK);
		sb.append(description);
		sb.append(BREAK);
		sb.append(SMALL_OPEN).append(ITALIC_OPEN).append(date).append(ITALIC_CLOSE).append(SMALL_CLOSE);
		
		current.put("text", Html.fromHtml(sb.toString()));
		//Log.e("Nimit see html","the html is" + Html.fromHtml(sb.toString()));
		current.put("imageLink", imgLink);
	}
}
