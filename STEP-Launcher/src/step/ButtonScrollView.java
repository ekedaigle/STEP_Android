package step;

import java.util.zip.Inflater;

import com.step.launcher.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import android.widget.ScrollView;

public class ButtonScrollView extends FrameLayout implements OnTouchListener {
	
	private LinearLayout linLayout;
	private ScrollView frameLayout;
	private Button upButton;
	private Button downButton;
	private View v;
	private Handler repeatHandler = new Handler();
	private boolean upButtonClicked = false;
	
	private Runnable repeatTask = new Runnable()
	{
		@Override
		public void run() {
			if (upButtonClicked)
				frameLayout.scrollBy(0, -30);
			else
				frameLayout.scrollBy(0, 30);
			
			repeatHandler.removeCallbacks(repeatTask);
			repeatHandler.postAtTime(repeatTask, SystemClock.uptimeMillis() + 100);
		}
	};
	
	public ButtonScrollView(Context context, AttributeSet attr)
	{
		super(context, attr);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.button_scroll_view, this);
		
		frameLayout = (ScrollView)v.findViewById(R.id.buttonScrollViewFrameLayout);
		linLayout = (LinearLayout)v.findViewById(R.id.buttonScrollViewLinearLayout);
		frameLayout.setVerticalFadingEdgeEnabled(false);
		
		upButton = (Button)v.findViewById(R.id.buttonScrollViewUpButton);
		upButton.setOnTouchListener(this);
		downButton = (Button)v.findViewById(R.id.buttonScrollViewDownButton);
		downButton.setOnTouchListener(this);
	}
	
	public void setUpButtonDrawable(Drawable d)
	{
		upButton.setBackgroundDrawable(d);
	}
	
	public void setDownButtonDrawable(Drawable d)
	{
		downButton.setBackgroundDrawable(d);
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
				frameLayout.scrollBy(0, -30);
				upButtonClicked = true;
			}
			else if (v == downButton)
			{
				frameLayout.scrollBy(0, 30);
				upButtonClicked = false;
			}
			
			repeatHandler.removeCallbacks(repeatTask);
			repeatHandler.postAtTime(repeatTask, SystemClock.uptimeMillis() + 800);
		}	
		else if (action == MotionEvent.ACTION_UP)
			repeatHandler.removeCallbacks(repeatTask);
		
		return false;
	}
	
	public FrameLayout getFrame()
	{
		return frameLayout;
	}

}
