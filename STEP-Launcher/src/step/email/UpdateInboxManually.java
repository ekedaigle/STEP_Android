package step.email;

import android.os.AsyncTask;

public class UpdateInboxManually extends AsyncTask<String, Void, String>{
	Mail m;
	UpdateInboxManually(Mail m1){
		m = m1;
	}
	@Override
	protected String doInBackground(String...strings)
	{
		try
		{
			m.getMessages();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "Get Messages Failed";
		}
		return "Got Messages";
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