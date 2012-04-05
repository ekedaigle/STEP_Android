package step;

import java.util.zip.Inflater;

import com.step.launcher.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class ButtonScrollView extends FrameLayout implements OnClickListener {
	
	LinearLayout linLayout;
	ScrollView frameLayout;
	Button upButton;
	Button downButton;
	View v;
	
	public ButtonScrollView(Context context, AttributeSet attr)
	{
		super(context, attr);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.button_scroll_view, this);
		
		frameLayout = (ScrollView)v.findViewById(R.id.buttonScrollViewFrameLayout);
		linLayout = (LinearLayout)v.findViewById(R.id.buttonScrollViewLinearLayout);
		
		upButton = (Button)v.findViewById(R.id.buttonScrollViewUpButton);
		upButton.setOnClickListener(this);
		downButton = (Button)v.findViewById(R.id.buttonScrollViewDownButton);
		downButton.setOnClickListener(this);
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
	public void onClick(View v) {
		if (v == upButton)
		{
			frameLayout.scrollBy(0, -30);
		}
		else if (v == downButton)
		{
			frameLayout.scrollBy(0, 30);
		}
	}
	
	public FrameLayout getFrame()
	{
		return frameLayout;
	}

}
