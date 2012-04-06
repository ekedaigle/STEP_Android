package step;

import java.io.IOException;
import java.io.OutputStreamWriter;

import step.address.AddressFragment;
import step.email.ConnectToEmailTask;
import step.email.DisplayMessagesTask;
import step.email.EmailFragment;
import step.email.Mail;
import step.email.UpdateMessagesTimerTask;
import step.music.MusicFragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.step.launcher.R;
import com.tmm.android.rssreader.NewspaperFragment;

public class LauncherActivity extends Activity
{	
	private final int NUM_BUTTONS = 4;
	private final int MUSIC = 0;
	private final int NEWSPAPER = 1;
	private final int ADDRESS = 2;
	private final int EMAIL = 3;

	private Fragment fragments[];
	private Fragment current_fragment = null;
	private Mail mail;
	
	private Button buttons[];
	private Drawable button_icons_normal[];
	private Drawable button_icons_selected[];
    private int m_interval = 30000; //30 seconds by default, can be changed later
    private Handler m_handler;
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
        m_handler = new Handler();
        
        fragments = new Fragment[NUM_BUTTONS];
        fragments[MUSIC] = new MusicFragment();
        fragments[EMAIL] = new EmailFragment();
        fragments[NEWSPAPER] = new NewspaperFragment();
        fragments[ADDRESS] = new AddressFragment();
        
        this.mail = new Mail(this);
        mail.setUserPass("capstone.group6.2012", "capstone2012");
        ((EmailFragment)fragments[EMAIL]).setMail(mail);
        ConnectToEmailTask task = new ConnectToEmailTask(mail);
    	task.execute();
    	m_statusChecker.run();
        // have the music button selected by default
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_layout, fragments[MUSIC]);
        ft.commit();
        buttons[MUSIC].setBackgroundDrawable(button_icons_selected[MUSIC]);
        current_fragment = fragments[MUSIC];
    }
    
    Runnable m_statusChecker = new Runnable()
    {
         @Override 
         public void run() {
 			  UpdateMessagesTimerTask task = new UpdateMessagesTimerTask(LauncherActivity.this.mail);
 			  task.execute();
              m_handler.postDelayed(m_statusChecker, m_interval);
         }
    };
    
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
    
//    private void showBar()
//    {
//    	execCommandLine("am startservice -n com.android.systemui/.SystemUIService");
//    }
    
    // run the string as a command
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