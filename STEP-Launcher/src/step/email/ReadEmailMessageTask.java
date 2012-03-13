package step.email;

import javax.mail.Address;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

public class ReadEmailMessageTask extends AsyncTask<String, Void, String>{
	Mail m;
	int idx;
	View v;
	ReadEmailMessageTask(Mail m1, int idx, View v){
		this.m = m1;
		this.idx = idx;
		this.v = v;
	}
	@Override
	protected String doInBackground(String...strings)
	{
		try
		{
           Multipart mp = (Multipart)this.m.msgs[this.idx].getContent();
           for (int i=0, n=mp.getCount(); i<n; i++) {
        	   Part part = mp.getBodyPart(i);

        	   String disposition = part.getDisposition();

        	   if (disposition == null)
        	   {
    			   // Check if plain
    			   MimeBodyPart mbp = (MimeBodyPart)part;
    			   if (mbp.isMimeType("text/plain")) {
    				   return mbp.getContent().toString();
    			   } else {
    			     // Special non-attachment cases here of 
    			     // image/gif, text/html, ...
    			   }
        	   }
           }
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "Read Messages Failed";
		}
		return "Failed";
	}
	protected void onPostExecute(String result)
	{
		try
		{
	    	Address[] to = this.m.msgs[this.idx].getAllRecipients();
	    	Address[] from = this.m.msgs[this.idx].getFrom();
	    	String subj = this.m.msgs[this.idx].getSubject();
	    	
	    	String fullBody = new String();
	    	fullBody = "TO: ";
	    	fullBody = fullBody.concat(to[0].toString());
	    	fullBody = fullBody.concat("\nFROM: ");
	    	fullBody = fullBody.concat(from[0].toString());
	    	fullBody = fullBody.concat("\nSUBJ: ");
	    	fullBody = fullBody.concat(subj);
	    	fullBody = fullBody.concat("\n\n");
	    	fullBody = fullBody.concat(result);
	    	TextView t = (TextView) this.v;
	    	t.setText(fullBody);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
