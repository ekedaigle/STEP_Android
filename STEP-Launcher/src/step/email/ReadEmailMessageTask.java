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
		String content = null;
		String fullBody = null;
		
		try
		{
           Multipart mp = (Multipart)this.m.msgs[this.idx].getContent();
           String contentType = this.m.msgs[this.idx].getContentType();
           for (int i=0, n=mp.getCount(); i<n; i++) {
        	   Part part = mp.getBodyPart(i);

        	   String disposition = part.getDisposition();

        	   if (disposition == null)
        	   {
    			   // Check if plain
    			   MimeBodyPart mbp = (MimeBodyPart)part;
    			   if (mbp.isMimeType("text/plain")) {
    				   content = mbp.getContent().toString();
    				   break;
    			   } else {
    			     // Special non-attachment cases here of 
    			     // image/gif, text/html, ...
    			   }
        	   }
           }
           if(content != null){
				Address[] to = this.m.msgs[this.idx].getAllRecipients();
				Address[] from = this.m.msgs[this.idx].getFrom();
				String subj = this.m.msgs[this.idx].getSubject();
				
				fullBody = new String();
				fullBody = "TO: ";
				fullBody = fullBody.concat(to[0].toString());
				fullBody = fullBody.concat("\nFROM: ");
				fullBody = fullBody.concat(from[0].toString());
				fullBody = fullBody.concat("\nSUBJ: ");
				fullBody = fullBody.concat(subj);
				fullBody = fullBody.concat("\n\n");
				fullBody = fullBody.concat(content);
				return fullBody;
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
    	TextView t = (TextView) this.v;
    	t.setText(result);
	}
	
}
