package step.email;

import android.os.AsyncTask;


public class UpdateMessagesTimerTask extends AsyncTask<String, Void, String> {
		private Mail m;
		
		public UpdateMessagesTimerTask(Mail m1){
			this.m = m1;
		}
		@Override
		protected String doInBackground(String...strings)
		{

			try
			{
				if(m.getNewMessages()){
					return "New Mail";
				} else {
					return "No New Updates";
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return "Failed";
			}
		}

		@Override
		protected void onPostExecute(String result)
		{
			if(result.contentEquals("New Mail")){
				this.m.setHaveMsgs(true);
				DisplayMessagesTask task = new DisplayMessagesTask(this.m);
				task.execute();
			}
		}
}
