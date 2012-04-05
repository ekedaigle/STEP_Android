package step.email;

import android.os.AsyncTask;
import android.widget.Toast;

public class UpdateInboxTask extends AsyncTask<String, Void, String>{
	Mail m;
	UpdateInboxTask(Mail m1){
		m = m1;
	}
	@Override
	protected String doInBackground(String...strings)
	{
		try
		{
			this.m.setHaveMsgs(false);
			m.getMessages();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "Failed";
		}
		return "Got Messages";
	}
	protected void onPostExecute(String result)
	{
		if(result.contentEquals("Got Messages")){
			this.m.setHaveMsgs(true);
			Toast.makeText(this.m.getActivity(), "Updated Messages", Toast.LENGTH_SHORT).show();
		}
	}
	
}