package com.tmm.android.rssreader;

import java.util.ArrayList;
import org.json.JSONObject;

public interface RssReaderTaskCallback {

	void taskGotRssFeed(int pick, ArrayList<JSONObject> temp);
}