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
	int selected_category = -1;
	View V;
	
	RssReaderCopy jobs_World;
	RssReaderCopy jobs_Sports;
	RssReaderCopy jobs_Government;
	RssReaderCopy jobs_Finance;
	RssReaderCopy jobs_Health;
	
	public enum jobs
	{
		WORLD, SPORTS, GOVERNMENT, FINANCE, HEALTH
	}
	
	RssReaderCopy[] copies = new RssReaderCopy[5];

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
							View.INVISIBLE);
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
					getActivity().findViewById(R.id.btnNext).setVisibility(
							View.INVISIBLE);	
				}
				getActivity().findViewById(R.id.btnPrevious).setVisibility(
						View.VISIBLE);
				NewspaperFragment.this.rssReader.readArticle(position_hold,
						getActivity().findViewById(R.id.txtReadArticle),
						getActivity().findViewById(R.id.txtReadTitle));
				
				getActivity().findViewById(R.id.scrlReadArticle).scrollTo(0, 0);//moves to top of view

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};
	
	public void taskGotRssFeed(int pick, ArrayList<JSONObject> store)
	{
		ArrayList<JSONObject> store_copy = new ArrayList<JSONObject>(store);
		copies[pick - 1].setJobs(store_copy);
	}
	
	public void changeToCategory(int category)
	{
		selected_category = category;
		position_hold = 0;
		V.findViewById(R.id.scrlReadArticle).scrollTo(0, 0);//moves to top of view
		
		if (copies[category].getJobs() == null)
		{
			rssReaderTask = new RssReaderTask(
					NewspaperFragment.this.rssReader, getActivity()
							.findViewById(R.id.txtReadArticle),
					getActivity().findViewById(R.id.txtReadTitle), category + 1);
			rssReaderTask.setCallback(NewspaperFragment.this);
			rssReaderTask.execute();
		}
		else
		{
			rssReader.setJobs(copies[category].getJobs());
			
			try {
				NewspaperFragment.this.rssReader.readArticle(position_hold,
						getActivity().findViewById(R.id.txtReadArticle),
						getActivity().findViewById(R.id.txtReadTitle));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		V.findViewById(R.id.btnNext).setVisibility(View.VISIBLE);
		V.findViewById(R.id.scrlReadArticle).setVisibility(
				View.VISIBLE);
		V.findViewById(R.id.btnPrevious).setVisibility(
				View.INVISIBLE);
	}

	private OnClickListener btnWorldListener = new OnClickListener() {
		public void onClick(View V) {
			changeToCategory(0);
		}
	};
	
	private OnClickListener btnGovernmentListener = new OnClickListener() {
		public void onClick(View V) {
			changeToCategory(1);
		}
	};
	
	private OnClickListener btnSportsListener = new OnClickListener() {
		public void onClick(View V) {
			changeToCategory(2);
		}
	};
	
	private OnClickListener btnFinanceListener = new OnClickListener() {
		public void onClick(View V) {
			changeToCategory(3);
		}
	};
	
	private OnClickListener btnHealthListener = new OnClickListener() {
		public void onClick(View V) {
			changeToCategory(4);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			final Bundle savedInstanceState) {

		V = inflater.inflate(R.layout.newspaper_fragment, container,
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
		
		for (int i = 0; i < 5; i++)
			copies[i] = new RssReaderCopy();
		
		return V;

	}
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		if (selected_category >= 0)
			changeToCategory(selected_category);
	}

}