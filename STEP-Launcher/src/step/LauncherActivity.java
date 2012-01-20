package step;

import java.io.IOException;
import java.io.OutputStreamWriter;

import com.step.launcher.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.*;

public class LauncherActivity extends Activity
{	
	private MusicFragment music_fragment;
	private EmailFragment email_fragment;
	private NewspaperFragment newspaper_fragment;
	private StoreFragment store_fragment;
	private Fragment current_fragment = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        hideBar();
        
        music_fragment = new MusicFragment();
        email_fragment = new EmailFragment();
        newspaper_fragment = new NewspaperFragment();
        store_fragment = new StoreFragment();
        
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_layout, music_fragment);
        ft.commit();
        
        current_fragment = music_fragment;
    }
    
    public void onClick(View v)
    {		
    	Fragment next_fragment = null;

    	if (v == findViewById(R.id.music_button))
    	{
    		next_fragment = music_fragment;
    	}
    	else if (v == findViewById(R.id.newspaper_button))
    	{
    		next_fragment = newspaper_fragment;
    	}
    	else if (v == findViewById(R.id.store_button))
    	{
    		next_fragment = store_fragment;
    	}
    	else if (v == findViewById(R.id.email_button))
    	{
    		next_fragment = email_fragment;
    	}
    	
    	if (next_fragment != current_fragment)
    	{
    		FragmentTransaction ft = getFragmentManager().beginTransaction();
    		ft.remove(current_fragment);
    		ft.add(R.id.fragment_layout, next_fragment);
    		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    		ft.commit();

    		current_fragment = next_fragment;
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