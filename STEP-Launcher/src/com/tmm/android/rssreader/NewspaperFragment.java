/**
 * 
 */
package com.tmm.android.rssreader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import step.music.Genre;
import step.music.MusicAsyncTaskCallback;

import com.step.launcher.R;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class NewspaperFragment extends Fragment implements RssReaderTaskCallback{
	RssReaderTask rssReaderTask;
	RssReader rssReader;
	int position_hold;
	
	RssReaderCopy jobs_World;
	RssReaderCopy jobs_Sports;
	RssReaderCopy jobs_Government;
	RssReaderCopy jobs_Finance;
	RssReaderCopy jobs_Health;
	

	int max = 4;// the highest number of articles we will have is 5
	List<JSONObject> rssReaderList = new ArrayList<JSONObject>();

	private OnClickListener btnPreviousListener = new OnClickListener() {

		public void onClick(View V) {
			max = NewspaperFragment.this.rssReader.getJobs().size()-1;
			
			try {
				// EmailFragment.this.mail.readEmail(position,
				// getActivity().findViewById(R.id.txtReadEmail));
				position_hold = position_hold - 1;
				if (position_hold <= 0) {
					position_hold = 0;
					getActivity().findViewById(R.id.btnPrevious).setVisibility(
							View.VISIBLE);
				}
				getActivity().findViewById(R.id.btnNext).setVisibility(
						View.VISIBLE);
				getActivity().findViewById(R.id.scrlReadArticle).scrollTo(0, 0);//moves to top of view
				NewspaperFragment.this.rssReader.readArticle(position_hold,
						getActivity().findViewById(R.id.txtReadArticle),
						getActivity().findViewById(R.id.txtReadTitle));

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	
	

	private OnClickListener btnNextListener = new OnClickListener() {

		public void onClick(View V) {
			max = NewspaperFragment.this.rssReader.getJobs().size()-1;
			try {
				

				// EmailFragment.this.mail.readEmail(position,
				// getActivity().findViewById(R.id.txtReadEmail));
				position_hold = position_hold + 1;
				if (position_hold >= max) {
					position_hold = max;
					
				}
				getActivity().findViewById(R.id.btnPrevious).setVisibility(
						View.VISIBLE);
				NewspaperFragment.this.rssReader.readArticle(position_hold,
						getActivity().findViewById(R.id.txtReadArticle),
						getActivity().findViewById(R.id.txtReadTitle));
				getActivity().findViewById(R.id.btnNext).setVisibility(
						View.VISIBLE);
				getActivity().findViewById(R.id.scrlReadArticle).scrollTo(0, 0);//moves to top of view

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};
	
	public void taskGotRssFeed(int pick, ArrayList<JSONObject> store)
	{
		if (pick ==1)
		{
			
			jobs_World.setJobs(store);
			Log.e("world is","world is" + jobs_World.getJobs().get(0).toString());
			
			
			
			
		}
		else if (pick==2)
		{
			jobs_Government.setJobs(store);
		}
		else if (pick==3)
		{
			jobs_Sports.setJobs(store);
		}
		else if (pick==4)
		{
			
			jobs_Finance.setJobs(store);
			Log.e("world is","world is" + jobs_World.getJobs().get(0).toString());
			
		}
		else if (pick==5)
		{
			jobs_Health.setJobs(store);
		}
		
		
	}
	
	

	private OnClickListener btnWorldListener = new OnClickListener() {

		public void onClick(View V) {

			position_hold = 0;
			
				// TODO Auto-generated method stub
			
			getActivity().findViewById(R.id.scrlReadArticle).scrollTo(0, 0);//moves to top of view
			//if(jobs_World == null){
				jobs_World = new RssReaderCopy();
				rssReaderTask = new RssReaderTask(
						NewspaperFragment.this.rssReader, getActivity()
								.findViewById(R.id.txtReadArticle),
						getActivity().findViewById(R.id.txtReadTitle), 1);
				rssReaderTask.setCallback(NewspaperFragment.this);
				rssReaderTask.execute();
				
			//}
				/*
			else
			{
				
				
				
				//rssReader = jobs_World;
				try {
					NewspaperFragment.this.rssReader.readArticle(position_hold,
							getActivity().findViewById(R.id.txtReadArticle),
							getActivity().findViewById(R.id.txtReadTitle));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			*/
			
				
				
			
			// getActivity().findViewById(R.id.newsFrag_listview).setVisibility(View.GONE);
			getActivity().findViewById(R.id.btnNext).setVisibility(View.VISIBLE);
			getActivity().findViewById(R.id.scrlReadArticle).setVisibility(
					View.VISIBLE);
			getActivity().findViewById(R.id.btnPrevious).setVisibility(
					View.VISIBLE);

		}
	};
	private OnClickListener btnGovernmentListener = new OnClickListener() {

		public void onClick(View V) {

			// TODO Auto-generated method stub
			jobs_Government = new RssReaderCopy();
			position_hold = 0;
			getActivity().findViewById(R.id.scrlReadArticle).scrollTo(0, 0);//moves to top of view
			rssReaderTask = new RssReaderTask(NewspaperFragment.this.rssReader,
					getActivity().findViewById(R.id.txtReadArticle),
					getActivity().findViewById(R.id.txtReadTitle), 2);
			rssReaderTask.setCallback(NewspaperFragment.this);
			rssReaderTask.execute();
			// getActivity().findViewById(R.id.newsFrag_listview).setVisibility(View.GONE);
			getActivity().findViewById(R.id.btnNext)
					.setVisibility(View.VISIBLE);
			getActivity().findViewById(R.id.scrlReadArticle).setVisibility(
					View.VISIBLE);
			getActivity().findViewById(R.id.btnPrevious).setVisibility(
					View.VISIBLE);
		}

	};
	private OnClickListener btnSportsListener = new OnClickListener() {

		public void onClick(View V) {

			// TODO Auto-generated method stub
			jobs_Sports = new RssReaderCopy();
			getActivity().findViewById(R.id.scrlReadArticle).scrollTo(0, 0);//moves to top of view
			position_hold = 0;
			rssReaderTask = new RssReaderTask(NewspaperFragment.this.rssReader,
					getActivity().findViewById(R.id.txtReadArticle),
					getActivity().findViewById(R.id.txtReadTitle), 3);
			rssReaderTask.setCallback(NewspaperFragment.this);
			rssReaderTask.execute();
			// getActivity().findViewById(R.id.newsFrag_listview).setVisibility(View.GONE);
			getActivity().findViewById(R.id.btnNext)
					.setVisibility(View.VISIBLE);
			getActivity().findViewById(R.id.scrlReadArticle).setVisibility(
					View.VISIBLE);
			getActivity().findViewById(R.id.btnPrevious).setVisibility(
					View.VISIBLE);
		}

	};
	private OnClickListener btnFinanceListener = new OnClickListener() {

		
		public void onClick(View V) {

			jobs_Finance = new RssReaderCopy();
			position_hold = 0;
			// TODO Auto-generated method stub
			getActivity().findViewById(R.id.scrlReadArticle).scrollTo(0, 0);//moves to top of view
			rssReaderTask = new RssReaderTask(NewspaperFragment.this.rssReader,
					getActivity().findViewById(R.id.txtReadArticle),
					getActivity().findViewById(R.id.txtReadTitle), 4);
			rssReaderTask.setCallback(NewspaperFragment.this);
			rssReaderTask.execute();
			// getActivity().findViewById(R.id.newsFrag_listview).setVisibility(View.GONE);
			getActivity().findViewById(R.id.btnNext)
					.setVisibility(View.VISIBLE);
			getActivity().findViewById(R.id.scrlReadArticle).setVisibility(
					View.VISIBLE);
			getActivity().findViewById(R.id.btnPrevious).setVisibility(
					View.VISIBLE);
		}

	};
	private OnClickListener btnHealthListener = new OnClickListener() {

		public void onClick(View V) {

			// TODO Auto-generated method stub
			jobs_Health = new RssReaderCopy();
			getActivity().findViewById(R.id.scrlReadArticle).scrollTo(0, 0);//moves to top of view
			position_hold = 0;
			rssReaderTask = new RssReaderTask(NewspaperFragment.this.rssReader,
					getActivity().findViewById(R.id.txtReadArticle),
					getActivity().findViewById(R.id.txtReadTitle), 5);
			rssReaderTask.setCallback(NewspaperFragment.this);
			rssReaderTask.execute();
			// getActivity().findViewById(R.id.newsFrag_listview).setVisibility(View.GONE);
			getActivity().findViewById(R.id.btnNext)
					.setVisibility(View.VISIBLE);
			getActivity().findViewById(R.id.scrlReadArticle).setVisibility(
					View.VISIBLE);
			getActivity().findViewById(R.id.btnPrevious).setVisibility(
					View.VISIBLE);
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			final Bundle savedInstanceState) {

		final View V = inflater.inflate(R.layout.newspaper_fragment, container,
				false);
		

		V.findViewById(R.id.btnNext).setOnClickListener(btnNextListener);
		V.findViewById(R.id.btnPrevious)
				.setOnClickListener(btnPreviousListener);
		V.findViewById(R.id.btnGovernment).setOnClickListener(
				btnGovernmentListener);
		V.findViewById(R.id.btnSports).setOnClickListener(btnSportsListener);
		V.findViewById(R.id.btnFinance).setOnClickListener(btnFinanceListener);
		V.findViewById(R.id.btnWorld).setOnClickListener(btnWorldListener);
		V.findViewById(R.id.btnHealth).setOnClickListener(btnHealthListener);
		this.rssReader = new RssReader(getActivity());
		
		

		return V;

	}

}