package step.music;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.step.launcher.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import step.music.MusicAsyncTask;

public class MusicFragment extends Fragment implements MusicAsyncTaskCallback {
	
	private final int baseStationPort = 13331;
	
	private TextView title;
	
	private MusicAsyncTask asyncTask;
	private MusicAdapter adapter;
	private ArrayList<ArrayList<String>> categories;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        View v = inflater.inflate(R.layout.music_fragment, container, false);
        title = (TextView)v.findViewById(R.id.music_title);
        title.setText("HELLOOOO");
        
        asyncTask = new MusicAsyncTask();
        asyncTask.setCallback(this);
        asyncTask.execute(baseStationPort);
        
        GridView gridview = (GridView)v.findViewById(R.id.gridview);
        adapter = new MusicAdapter(this);
        gridview.setAdapter(adapter);
        
        return v;
	}

	@Override
	public void taskGotGenres(ArrayList<ArrayList<String>> categories)
	{
		this.categories = categories;
		
		String[] titles_array = new String[categories.size()];
		
		for (int i = 0; i < titles_array.length; i++)
			titles_array[i] = categories.get(i).get(0);
		
		adapter.setTitles(titles_array);
	}
	
	public void onClick(View v)
	{
		String title = ((Button)v).getText().toString();
		
		if (title.compareTo("<") == 0)
		{
			String[] titles_array = new String[categories.size()];
			
			for (int i = 0; i < titles_array.length; i++)
				titles_array[i] = categories.get(i).get(0);
			
			adapter.setTitles(titles_array);
		}
		else
		{
			for (int i = 0; i < categories.size(); i++)
			{
				if (title.compareTo(categories.get(i).get(0)) == 0)
				{
					String[] titles_array = new String[categories.get(i).size()];
					titles_array[0] = "<";
					
					for (int j = 1; j < categories.get(i).size(); j++)
						titles_array[j] = categories.get(i).get(j);
					
					adapter.setTitles(titles_array);
					break;
				}
				
				for (int j = 1; j < categories.get(i).size(); j++)
				{
					if (title.compareTo(categories.get(i).get(j)) == 0)
					{
						asyncTask.sendMessage("PLAY " + title);
					}
				}
			}
		}
	}
}
