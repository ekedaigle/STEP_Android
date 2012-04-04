package com.tmm.android.rssreader;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import com.step.launcher.R;

import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import java.util.regex.Pattern;
public class RssReader {
	
	private final static String BREAK = "<BR>";
	private final static String ITALIC_OPEN = "<I>";
	private final static String ITALIC_CLOSE = "</I>";
	private final static String SMALL_OPEN = "<SMALL>";
	private final static String SMALL_CLOSE = "</SMALL>";
	
	private ArrayList<JSONObject> jobs;
	public ArrayList<JSONObject> getJobs(){
		return this.jobs;
	}
	
	RssReader(Activity a){
		this.jobs = new ArrayList<JSONObject>();
	}
	
	public void readArticle(int idx, View v) throws Exception{
    	ReadNewsArticle task = new ReadNewsArticle(this, v, idx);
    	String k = "";
    	task.getArticle(k);
    }
	
	public ArrayList<JSONObject> getLatestRssFeed(){
		//String feed = "http://fulltextrssfeed.com/news.google.com/news?ned=us&topic=h&output=rss";
		//finance = "http://fulltextrssfeed.com/finance.yahoo.com/";
		//government = "http://fulltextrssfeed.com/dir.yahoo.com/rss/dir/getrss.php?gov"
		String feed = "http://fulltextrssfeed.com/finance.yahoo.com/";
		//String feed = "http://fulltextrssfeed.com/rss.cnn.com/rss/cnn_topstories.rss";
		
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

		this.jobs.clear();
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
		String date_of_article = article.getPubDate();
		//String imgLink = article.getImgLink();
		String delims = "[-]";
		String[] dates = date_of_article.split(delims);
		
		String pattern = "<img src(.*)>";
		String updated = description.replaceAll(pattern, "");
		description = updated;
		
		StringBuffer header = new StringBuffer();
		header.append(title);
		//header.append(" ");
		//header.append(dates[0]);
		
		
		
		StringBuffer body = new StringBuffer();
		//body.append(BOLD_OPEN).append(title).append(BOLD_CLOSE);
		//body.append(BREAK);
		body.append(description);
		body.append(BREAK);
		body.append(SMALL_OPEN).append(ITALIC_OPEN).append(dates[0]).append(ITALIC_CLOSE).append(SMALL_CLOSE);
		
		
		current.put("header", Html.fromHtml(header.toString()));
		current.put("text", Html.fromHtml(body.toString()));
		//Log.e("Nimit see html","the html is" + Html.fromHtml(sb.toString()));
		//current.put("imageLink", imgLink);
	}
}
