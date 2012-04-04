/**
 * 
 */
package com.tmm.android.rssreader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;

import com.step.launcher.R;
import com.tmm.android.rssreader.RssListAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class NewspaperFragment extends Fragment {
	//RssActivity rssActivity;
	private RssListAdapter adapter;
	RssReaderTask rssReaderTask;
	RssReader rssReader;
	View test;
	int position_hold;
	int max = 4;//the highest number of articles we will have is 5
	List<JSONObject> rssReaderList = new ArrayList<JSONObject>();

	
	private OnItemClickListener listItemSelectListener = new OnItemClickListener() {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    	{
    		
    		max = parent.getCount()-1;
    		if(position == 0){
    			getActivity().findViewById(R.id.btnPrevious).setVisibility(View.GONE);
    			getActivity().findViewById(R.id.btnNext).setVisibility(View.VISIBLE);
    		}
    		else if(position == max)
    		{
    			getActivity().findViewById(R.id.btnPrevious).setVisibility(View.VISIBLE);
    			getActivity().findViewById(R.id.btnNext).setVisibility(View.GONE);
    		}
    		getActivity().findViewById(R.id.newsFrag_listview).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlReadArticle).setVisibility(View.VISIBLE);
    		
    		
    		
    		try
        	{
    			//EmailFragment.this.mail.readEmail(position, getActivity().findViewById(R.id.txtReadEmail));
    			NewspaperFragment.this.rssReader.readArticle(position, getActivity().findViewById(R.id.txtReadArticle));
        		position_hold = position;
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
        		
    	}
    };
    private OnClickListener btnPreviousListener = new OnClickListener() {

		public void onClick(View V) {
			try
        	{
    			//EmailFragment.this.mail.readEmail(position, getActivity().findViewById(R.id.txtReadEmail));
				position_hold = position_hold - 1;
				if(position_hold ==0)
				{
					getActivity().findViewById(R.id.btnPrevious).setVisibility(View.GONE);
				}
				getActivity().findViewById(R.id.btnNext).setVisibility(View.VISIBLE);
    			NewspaperFragment.this.rssReader.readArticle(position_hold, getActivity().findViewById(R.id.txtReadArticle));
        		
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
			
		}
    };
    
    
    
    private OnClickListener btnNextListener = new OnClickListener() {

		public void onClick(View V) {
			try
        	{
				
				
    			//EmailFragment.this.mail.readEmail(position, getActivity().findViewById(R.id.txtReadEmail));
				position_hold = position_hold + 1;
				getActivity().findViewById(R.id.btnPrevious).setVisibility(View.VISIBLE);
    			NewspaperFragment.this.rssReader.readArticle(position_hold, getActivity().findViewById(R.id.txtReadArticle));
    			if(position_hold == max)
				{
					getActivity().findViewById(R.id.btnNext).setVisibility(View.GONE);
				}
        		
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
			
		}
    };
    
    
    
    
    private OnClickListener btnWorldListener = new OnClickListener() {

		public void onClick(View V) {
			
			// TODO Auto-generated method stub
			rssReaderTask = new RssReaderTask(NewspaperFragment.this.rssReader,getActivity().findViewById(R.id.txtReadArticle),1);
			rssReaderTask.execute();
			getActivity().findViewById(R.id.newsFrag_listview).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlReadArticle).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.btnPrevious).setVisibility(View.GONE);
			
			
		}
    	
    };
private OnClickListener btnGovernmentListener = new OnClickListener() {

	public void onClick(View V) {
		
		// TODO Auto-generated method stub
		rssReaderTask = new RssReaderTask(NewspaperFragment.this.rssReader,getActivity().findViewById(R.id.txtReadArticle),2);
		rssReaderTask.execute();
		getActivity().findViewById(R.id.newsFrag_listview).setVisibility(View.GONE);
		getActivity().findViewById(R.id.scrlReadArticle).setVisibility(View.VISIBLE);
		getActivity().findViewById(R.id.btnPrevious).setVisibility(View.GONE);
	}
    	
    };
private OnClickListener btnSportsListener = new OnClickListener() {

	public void onClick(View V) {
		
		// TODO Auto-generated method stub
		rssReaderTask = new RssReaderTask(NewspaperFragment.this.rssReader,getActivity().findViewById(R.id.txtReadArticle),3);
		rssReaderTask.execute();
		getActivity().findViewById(R.id.newsFrag_listview).setVisibility(View.GONE);
		getActivity().findViewById(R.id.scrlReadArticle).setVisibility(View.VISIBLE);
		getActivity().findViewById(R.id.btnPrevious).setVisibility(View.GONE);
	}
    	
    };
private OnClickListener btnFinanceListener = new OnClickListener() {

	public void onClick(View V) {
		
		// TODO Auto-generated method stub
		rssReaderTask = new RssReaderTask(NewspaperFragment.this.rssReader,getActivity().findViewById(R.id.txtReadArticle),4);
		rssReaderTask.execute();
		getActivity().findViewById(R.id.newsFrag_listview).setVisibility(View.GONE);
		getActivity().findViewById(R.id.scrlReadArticle).setVisibility(View.VISIBLE);
		getActivity().findViewById(R.id.btnPrevious).setVisibility(View.GONE);
	}
    	
    };
    
    
    
    
    
    
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			final Bundle savedInstanceState) {

				
		final View V = inflater.inflate(R.layout.newspaper_fragment, container, false);
		
		
		V.findViewById(R.id.btnNext).setOnClickListener(btnNextListener);
		V.findViewById(R.id.btnPrevious).setOnClickListener(btnPreviousListener);
		V.findViewById(R.id.btnGovernment).setOnClickListener(btnGovernmentListener);
		V.findViewById(R.id.btnSports).setOnClickListener(btnSportsListener);
		V.findViewById(R.id.btnFinance).setOnClickListener(btnFinanceListener);
		V.findViewById(R.id.btnWorld).setOnClickListener(btnWorldListener);
		
		final ListView list = (ListView) V.findViewById(R.id.newsFrag_listview);
		list.setTextFilterEnabled(true);
		list.setOnItemClickListener(listItemSelectListener);
	    this.rssReader = new RssReader(getActivity(), list);
		try {
			
			ArrayList<JSONObject> jobs = new ArrayList<JSONObject>();
			rssReaderTask = new RssReaderTask(this.rssReader,getActivity().findViewById(R.id.txtReadArticle),0);
			rssReaderTask.execute();
			
			
			
	
		} catch (Exception e) {
			Log.e("RSS ERROR", "Error loading RSS Feed Stream >> " + e.getMessage() + " //" + e.toString());
		}
		
				  

		return V;
				
	}
	
	
	
	
}