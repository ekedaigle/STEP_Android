package step.music;

import com.step.launcher.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

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
	
	//@Override
	public int getCount() {
		return titles.length;
	}

	//@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	//@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Button button;
		
		if (convertView == null)
		{
			button = new Button(context);
			button.setHeight(100);
			button.setBackgroundDrawable(fragment.getResources().getDrawable(R.drawable.generic_button));
			button.setTextSize(24);
			button.setLayoutParams(new AbsListView.LayoutParams(300, LayoutParams.WRAP_CONTENT));
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					fragment.onStationClick(v);
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
