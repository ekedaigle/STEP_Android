package step.music;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.nio.ByteOrder;

import com.step.launcher.R;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.GridLayoutAnimationController;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import step.ButtonScrollView;
import step.music.MusicAsyncTask;

public class MusicFragment extends Fragment implements MusicAsyncTaskCallback, View.OnClickListener {
	
	private final int baseStationPort = 13330;
	private final int volumeStep = 5;
	
	private TextView title;
	
	private MusicAsyncTask asyncTask;
	private MusicAdapter adapter;
	private Map<String, Genre> stations;
	private ArrayList<Button> genre_buttons;
	private LinearLayout scrollLayout;
	private RadioGroup musicScrollGroup;
	private LinearLayout musicStationsLayout;
	private Genre selected_genre;
	private View v;
	private int volume = 70;
	Button volumeUpButton;
	Button volumeDownButton;
	Button stopButton;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        v = inflater.inflate(R.layout.music_fragment, container, false);
        title = (TextView)v.findViewById(R.id.music_title);
        
        musicScrollGroup = (RadioGroup)v.findViewById(R.id.musicScrollGroup);
        title = (TextView)v.findViewById(R.id.music_title);
        musicStationsLayout = (LinearLayout)v.findViewById(R.id.musicStationLayout);
        volumeUpButton = (Button)v.findViewById(R.id.musicVolumeUp);
        volumeUpButton.setOnClickListener(this);
        volumeDownButton = (Button)v.findViewById(R.id.musicVolumeDown);
        volumeDownButton.setOnClickListener(this);
        stopButton = (Button)v.findViewById(R.id.musicStopButton);
        stopButton.setOnClickListener(this);
        adapter = new MusicAdapter(this);
        
        if (genre_buttons == null)
        {
        	// find base station ip
        	WifiManager manager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
        	int addr = manager.getDhcpInfo().gateway;
        	byte[] bytes = new byte[4];
        	
        	// Need to flip the byte order, since java is big-endian and the
        	// tablet is little-endian. Java is so fucking useless that it
        	// doesn't handle this shit for you.
        	if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
        	{
	        	bytes[0] = (byte)addr;
	        	bytes[1] = (byte)(addr >> 8);
	        	bytes[2] = (byte)(addr >> 16);
	        	bytes[3] = (byte)(addr >> 24);
        	}
        	else
        		bytes = BigInteger.valueOf(addr).toByteArray();
        	
        	InetAddress baseStationAddr = null;
        	
			try {
				baseStationAddr = InetAddress.getByAddress(bytes);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
	        asyncTask = new MusicAsyncTask(baseStationAddr);
	        asyncTask.setCallback(this);
	        asyncTask.execute(baseStationPort);
        }
        else
        {
        	musicScrollGroup.removeAllViews();
        	
        	for (Button b : genre_buttons)
        		musicScrollGroup.addView(b);
        }
        
        return v;
	}

	//@Override
	public void taskGotStations(Map<String, Genre> stations)
	{
		this.stations = stations;
		//LinearLayout layout = (LinearLayout)scrollLayout;
		//layout.removeAllViews();
		musicScrollGroup.removeAllViews();
		
		Resources r = getResources();
		genre_buttons = new ArrayList<Button>(stations.size());
		
		for (String genre : stations.keySet())
		{
			RadioButton b = new RadioButton(this.getActivity());
			b.setText(genre);
			b.setButtonDrawable(r.getDrawable(R.drawable.null_drawable));
			b.setBackgroundDrawable(r.getDrawable(R.drawable.generic_radio_button));
			b.setTextSize(36);
			b.setOnClickListener(this);
			genre_buttons.add(b);
			musicScrollGroup.addView(b);
		}
	}
	
	public void onClick(View v)
	{
		if (v == volumeUpButton)
		{
			volume += volumeStep;
			setVolume(volume);
		}
		else if (v == volumeDownButton)
		{
			volume -= volumeStep;
			setVolume(volume);
		}
		else if (v == stopButton)
		{
			asyncTask.sendMessage("STOP");
		}
		else if (genre_buttons.contains(v))
		{
			String title = ((Button)v).getText().toString();
			selected_genre = stations.get(title);
			musicStationsLayout.removeAllViews();
			int index = 0;
			LinearLayout curLayout = null;
			
			for (Station s : selected_genre.stations)
			{
				if (index % 3 == 0)
				{
					if (curLayout != null)
						musicStationsLayout.addView(curLayout);
					
					curLayout = new LinearLayout(getActivity());
					curLayout.setOrientation(LinearLayout.HORIZONTAL);
					curLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT, 0.33f));
				}
				
				Button button = new Button(getActivity());
				button.setBackgroundDrawable(getResources().getDrawable(R.drawable.address_addcon_action));
				button.setTextSize(36);
				button.setTextColor(Color.WHITE);
				button.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));
				button.setText(s.name);
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onStationClick(v);
					}
				});
				
				curLayout.addView(button);
				index += 1;
			}
			
			//musicStationsLayout.addView(curLayout);
		}
	}
	
	public void onStationClick(View v)
	{
		String title = ((Button)v).getText().toString();
		Station selected_station = null;
		
		for (Station s : selected_genre.stations)
		{
			if (s.name.compareTo(title) == 0)
				selected_station = s;
		}
		
		if (selected_station != null)
		{
			this.title.setText("Now Playing: " + selected_station.name);
			asyncTask.sendMessage("PLAY " + selected_genre.id + " " + selected_station.id);
		}
	}
	
	public void setVolume(int v)
	{
		asyncTask.sendMessage("VOL " + v);
	}
	
	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		musicScrollGroup.removeAllViews();
	}
}
