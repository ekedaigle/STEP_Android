package step.music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.step.launcher.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import step.music.MusicAsyncTask;

public class MusicFragment extends Fragment implements MusicAsyncTaskCallback {
	
	private final int baseStationPort = 13331;
	
	private TextView title;
	
	private MusicAsyncTask asyncTask;
	private MusicAdapter adapter;
	private Map<String, Genre> stations;
	private ArrayList<Button> genre_buttons;
	private LinearLayout scrollLayout;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        View v = inflater.inflate(R.layout.music_fragment, container, false);
        title = (TextView)v.findViewById(R.id.music_title);
        
        scrollLayout = (LinearLayout)v.findViewById(R.id.musicScrollLayout);
        
        asyncTask = new MusicAsyncTask();
        asyncTask.setCallback(this);
        asyncTask.execute(baseStationPort);
        
        return v;
	}

	@Override
	public void taskGotStations(Map<String, Genre> stations)
	{
		this.stations = stations;
		LinearLayout layout = (LinearLayout)scrollLayout;
		layout.removeAllViews();
		
		for (String genre : stations.keySet())
		{
			Button b = new Button(this.getActivity());
			b.setText(genre);
			layout.addView(b);
		}
	}
	
	public void onClick(View v)
	{
		String title = ((Button)v).getText().toString();
		Genre genre = stations.get(title);
	}
}
