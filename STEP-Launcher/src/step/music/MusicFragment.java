package step.music;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.step.launcher.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import step.music.MusicAsyncTask;

public class MusicFragment extends Fragment implements MusicAsyncTaskCallback {
	
	private final String baseStationAddr = "10.0.50.1";
	private final int baseStationRecvPort = 13338;
	private final int baseStationSendPort = 13339;
	
	private TextView title;
	private Socket sendSocket;
	private Socket recvSocket;
	
	private MusicAsyncTask asyncTask;
	private MusicAdapter adapter;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        View v = inflater.inflate(R.layout.music_fragment, container, false);
        title = (TextView)v.findViewById(R.id.music_title);
        title.setText("HELLOOOO");
        
        asyncTask = new MusicAsyncTask();
        asyncTask.execute(baseStationRecvPort, baseStationSendPort);
        
        GridView gridview = (GridView)v.findViewById(R.id.gridview);
        adapter = new MusicAdapter(getActivity());
        
        String[] titles = {"Test1", "Test2", "Test3"};
        adapter.setTitles(titles);
        gridview.setAdapter(adapter);
        
        return v;
	}

	@Override
	public void taskGotData(String data)
	{
		title.setText(data);
	}
}
