package step;

import java.io.IOException;
import java.io.OutputStreamWriter;

import step.email.EmailFragment;

import com.step.launcher.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;

public class LauncherActivity extends Activity
{	
	private final int NUM_BUTTONS = 4;
	private final int MUSIC = 0;
	private final int NEWSPAPER = 1;
	private final int ADDRESS = 2;
	private final int EMAIL = 3;

	private Fragment fragments[];
	private Fragment current_fragment = null;
	
	private Button buttons[];
	private Drawable button_icons_normal[];
	private Drawable button_icons_selected[];
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        hideBar();
        
        buttons = new Button[NUM_BUTTONS];
        buttons[MUSIC] = (Button)findViewById(R.id.music_button);
        buttons[NEWSPAPER] = (Button)findViewById(R.id.newspaper_button);
        buttons[ADDRESS] = (Button)findViewById(R.id.address_button);
        buttons[EMAIL] = (Button)findViewById(R.id.email_button);
        
        button_icons_normal = new Drawable[NUM_BUTTONS];
        Resources res = getResources();
        button_icons_normal[MUSIC] = res.getDrawable(R.drawable.music_button);
        button_icons_normal[NEWSPAPER] = res.getDrawable(R.drawable.newspaper_button);
        button_icons_normal[ADDRESS] = res.getDrawable(R.drawable.address_button);
        button_icons_normal[EMAIL] = res.getDrawable(R.drawable.email_button);
        
        button_icons_selected = new Drawable[NUM_BUTTONS];
        button_icons_selected[MUSIC] = res.getDrawable(R.drawable.music_button_pressed);
        button_icons_selected[NEWSPAPER] = res.getDrawable(R.drawable.newspaper_button_pressed);
        button_icons_selected[ADDRESS] = res.getDrawable(R.drawable.address_button_pressed);
        button_icons_selected[EMAIL] = res.getDrawable(R.drawable.email_button_pressed);
        
        fragments = new Fragment[NUM_BUTTONS];
        fragments[MUSIC] = new MusicFragment();
        fragments[EMAIL] = new EmailFragment();
        fragments[NEWSPAPER] = new NewspaperFragment();
        fragments[ADDRESS] = new AddressFragment();
        
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_layout, fragments[MUSIC]);
        ft.commit();
        
        buttons[MUSIC].setBackgroundDrawable(button_icons_selected[MUSIC]);
        
        current_fragment = fragments[MUSIC];
    }
    
    private void changeToFragmentNum(int n)
    {
    	if (fragments[n] != current_fragment)
    	{
    		FragmentTransaction ft = getFragmentManager().beginTransaction();
    		ft.remove(current_fragment);
    		ft.add(R.id.fragment_layout, fragments[n]);
    		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    		ft.commit();

    		current_fragment = fragments[n];
    		
    		// reset all the buttons
    		for (int i = 0; i < NUM_BUTTONS; i++)
    			buttons[i].setBackgroundDrawable(button_icons_normal[i]);
    		
    		// set the one button to selected
    		buttons[n].setBackgroundDrawable(button_icons_selected[n]);
    	}
    }
    
    public void onClick(View v)
    {    	
    	for (int i = 0; i < NUM_BUTTONS; i++)
    	{
    		if (v == buttons[i])
    			changeToFragmentNum(i);
    	}
    }
    
    private void hideBar()
    {
    	execCommandLine("service call activity 79 s16 com.android.systemui");
    }
    
    private void showBar()
    {
    	execCommandLine("am startservice -n com.android.systemui/.SystemUIService");
    }
    
    private void execCommandLine(String command)
    {
        Runtime runtime = Runtime.getRuntime();
        Process proc = null;
        OutputStreamWriter osw = null;

        try
        {
            proc = runtime.exec("su");
            osw = new OutputStreamWriter(proc.getOutputStream());
            osw.write(command);
            osw.flush();
            osw.close();
        }
        catch (IOException ex)
        {
            Log.e("execCommandLine()", "Command resulted in an IO Exception: " + command);
            return;
        }
        finally
        {
            if (osw != null)
            {
                try
                {
                    osw.close();
                }
                catch (IOException e){}
            }
        }

        try 
        {
            proc.waitFor();
        }
        catch (InterruptedException e){}

        if (proc.exitValue() != 0)
        {
            Log.e("execCommandLine()", "Command returned error: " + command + "\n  Exit code: " + proc.exitValue());
        }
    }
}