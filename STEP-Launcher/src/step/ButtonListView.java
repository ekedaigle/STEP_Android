package step;

import java.util.zip.Inflater;

import com.step.launcher.R;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ListView;

public class ButtonListView extends FrameLayout implements OnTouchListener {
	
	private LinearLayout linLayout;
	private ListView frameLayout;
	private Button upButton;
	private Button downButton;
	private View v;
	private Handler repeatHandler = new Handler();
	private boolean upButtonClicked = false;
	
	private Runnable repeatTask = new Runnable()
	{
		@Override
		public void run() {
			if (upButtonClicked){
				pos++;
				frameLayout.smoothScrollToPosition(pos);
			} else {
				pos--;
				frameLayout.smoothScrollToPosition(pos);
			}
			
			repeatHandler.removeCallbacks(repeatTask);
			repeatHandler.postAtTime(repeatTask, SystemClock.uptimeMillis() + 100);
		}
	};
	
	public ButtonListView(Context context, AttributeSet attr)
	{
		super(context, attr);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.button_list_view, this);
		
		frameLayout = (ListView)v.findViewById(R.id.buttonListViewFrameLayout);
		linLayout = (LinearLayout)v.findViewById(R.id.buttonListViewLinearLayout);
		frameLayout.setVerticalFadingEdgeEnabled(false);
		
		upButton = (Button)v.findViewById(R.id.buttonListViewUpButton);
		upButton.setOnTouchListener(this);
		downButton = (Button)v.findViewById(R.id.buttonListViewDownButton);
		downButton.setOnTouchListener(this);
	}
	
	@Override
	public void onFinishInflate()
	{
		super.onFinishInflate();
		
		for (int i = 0; i < getChildCount(); i++)
		{
			View child = getChildAt(i);
			
			if (child != linLayout && child != frameLayout)
			{
				removeView(child);
				frameLayout.addView(child);
			}
		}
	}
	
	@Override
	public void addView(View v)
	{
		frameLayout.addView(v);
	}
	
	@Override
	public void removeAllViews()
	{
		frameLayout.removeAllViews();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent m) {
		int action = m.getAction();
		
		if (action == MotionEvent.ACTION_DOWN)
		{
			if (v == upButton)
			{
				pos++;
				frameLayout.smoothScrollToPosition(pos);
				upButtonClicked = true;
			}
			else if (v == downButton)
			{
				pos--;
				frameLayout.smoothScrollToPosition(pos);
				upButtonClicked = false;
			}
			
			repeatHandler.removeCallbacks(repeatTask);
			repeatHandler.postAtTime(repeatTask, SystemClock.uptimeMillis() + 800);
		}	
		else if (action == MotionEvent.ACTION_UP)
			repeatHandler.removeCallbacks(repeatTask);
		
		return false;
	}
	
//	public FrameLayout getFrame()
//	{
//		return frameLayout.getF;
//	}

}
