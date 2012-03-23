package step.music;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class MusicAdapter extends BaseAdapter {

	private String titles[];
	private Context context;
	private MusicFragment fragment;
	
	public MusicAdapter(MusicFragment m)
	{
		fragment = m;
		context = m.getActivity();
		titles = new String[0];
	}
	
	public void setTitles(String[] t)
	{
		titles = t;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return 12;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Button button;
		
		if (convertView == null)
		{
			button = new Button(context);
			button.setHeight(130);
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					fragment.onClick(v);
				}
			});
		}
		else
			button = (Button)convertView;
		
		if (position < titles.length)
			button.setText(titles[position]);
		else
			button.setText("");
		
		return button;
	}

}
