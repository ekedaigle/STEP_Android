/**
 * 
 */
package com.tmm.android.rssreader.reader;

import java.util.ArrayList;
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
public class RssReader {
	
	private final static String BOLD_OPEN = "<B>";
	private final static String BOLD_CLOSE = "</B>";
	private final static String BREAK = "<BR>";
	private final static String ITALIC_OPEN = "<I>";
	private final static String ITALIC_CLOSE = "</I>";
	private final static String SMALL_OPEN = "<SMALL>";
	private final static String SMALL_CLOSE = "</SMALL>";

	/**
	 * This method defines a feed URL and then calles our SAX Handler to read the article list
	 * from the stream
	 * 
	 * @return List<JSONObject> - suitable for the List View activity
	 */
	public static List<JSONObject> getLatestRssFeed(){
		//String feed = "http://globoesporte.globo.com/dynamo/futebol/times/vasco/rss2.xml";
		//String feed = "http://news.yahoo.com/rss/personal-finance";
		String feed ="http://fulltextrssfeed.com/news.google.com/news?ned=us&topic=h&output=rss";
		//http://fulltextrssfeed.com/rss.cnn.com/rss/cnn_topstories.rss
		//String feed ="http://finance.yahoo.com/rss/retirement";
		
		
		RSSHandler rh = new RSSHandler();
		
		List<Article> articles =  rh.getLatestArticles(feed);
		
		return fillData(articles);
	}
	
	
	/**
	 * This method takes a list of Article objects and converts them in to the 
	 * correct JSON format so the info can be processed by our list view
	 * 
	 * @param articles - list<Article>
	 * @return List<JSONObject> - suitable for the List View activity
	 */
	private static List<JSONObject> fillData(List<Article> articles) {

        List<JSONObject> items = new ArrayList<JSONObject>();
        Log.v("fillData","Array created!!!");
        for (Article article : articles) {
            JSONObject current = new JSONObject();
            try {
            	buildJsonObject(article, current);
            	Log.v("fillData","built Json Object!!!");
			} catch (JSONException e) {
				Log.e("RSS ERROR", "Error creating JSON Object from RSS feed");
			}
            Log.v("fillData","Trying to add object" + current);
			items.add(current);
			
        }
        Log.v("nimits stuff","We have left loop finally");
        return items;
	}


	/**
	 * This method takes a single Article Object and converts it in to a single JSON object
	 * including some additional HTML formating so they can be displayed nicely
	 * 
	 * @param article
	 * @param current
	 * @throws JSONException
	 */
	private static void buildJsonObject(Article article, JSONObject current) throws JSONException {
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
		current.put("imageLink", imgLink);
	}
}