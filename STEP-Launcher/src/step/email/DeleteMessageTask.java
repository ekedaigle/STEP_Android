package step.email;

import android.os.AsyncTask;
import android.widget.Toast;

public class DeleteMessageTask extends AsyncTask<String, Void, String>{
	Mail m;
	public DeleteMessageTask(Mail m1){
		m = m1;
	}
	@Override
	protected String doInBackground(String...strings)
	{
		try {
			this.m.deleteMsg();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Done";
	}
	protected void onPostExecute(String result)
	{
		if(result.contentEquals("Done")){	
			try {
				this.m.displayMessages();
				Toast.makeText(this.m.getActivity(), "Message Deleted", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}