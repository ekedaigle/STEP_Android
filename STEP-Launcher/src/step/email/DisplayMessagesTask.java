package step.email;

import android.os.AsyncTask;

public class DisplayMessagesTask extends AsyncTask<String, Void, String>{
	Mail m;
	DisplayMessagesTask(Mail m1){
		m = m1;
	}
	@Override
	protected String doInBackground(String...strings)
	{
		while(!this.m.haveMsgs())
		{
			
		}
		return "Done";
	}
	protected void onPostExecute(String result)
	{
		try
		{
			m.displayMessages();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
}