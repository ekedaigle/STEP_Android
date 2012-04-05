package step.email;

import java.io.File;
import java.io.IOException;

import javax.mail.Address;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;

import com.step.launcher.R;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class ReadEmailMessageTask extends AsyncTask<String, Void, String>{
	Mail m;
	int idx;
	ReadEmailMessageTask(Mail m1, int idx){
		this.m = m1;
		this.idx = idx;
	}
	@Override
	protected String doInBackground(String...strings)
	{
		String content = null;
		String fullBody = null;
		String filename = null;
		Multipart mp_msg;
		File file;
		File sdcard = Environment.getExternalStorageDirectory();
		File dir = new File(sdcard.getAbsolutePath()+"/step/emailapp/attachments");
		dir.mkdirs();
		
		this.m.getCurMsg().clearMsgData();
		//String folder = "../attachments/";
		
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
    			   if (mbp.isMimeType("text/plain")){
    				   content = mbp.getContent().toString();
    			   } else if(mbp.isMimeType("multipart/ALTERNATIVE")){
    				   mp_msg = (Multipart)mbp.getContent();
    				   content = mp_msg.getBodyPart(0).getContent().toString();
    			   }
        	   }
        	   else if (disposition.equalsIgnoreCase(Part.ATTACHMENT)){
				   filename = part.getFileName();
				   if (filename == null || filename.length() == 0) {
					   filename = "Attachment" + i;
				   }
				   try {
					   Log.d("EMAIL APP", dir.toString());
					   file = new File(dir, filename);
					   file.createNewFile();
					   ((MimeBodyPart)part).saveFile(file);
					   this.m.getCurMsg().addAttachmentLoc(filename);
					   Log.d("Email Fragment", "Saved the Attachment "+i+" to the following filename ["+filename+"].");
				   } catch (IOException ex) {
					   Log.e("Email Fragment", "Caught an exception trying to save an attachment to the filename ["+filename+"].", ex);
				   }
			      //Special non-attachment cases here of 
			     // image/gif, text/html, ...
			   }
           }

	        if(content != null){
	        	this.m.getCurMsg().setBody(content);
	        	this.m.getCurMsg().setTo(this.m.msgs[this.idx].getAllRecipients());
	        	this.m.getCurMsg().setFrom(this.m.msgs[this.idx].getFrom());
	        	this.m.getCurMsg().setSubj(this.m.msgs[this.idx].getSubject());
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
    	TextView tv = (TextView) this.m.getActivity().findViewById(R.id.txtReadEmailFrom);
    	tv.setText(this.m.getCurMsg().getFrom());
    	tv = (TextView) this.m.getActivity().findViewById(R.id.txtReadEmailSubject);
    	tv.setText(this.m.getCurMsg().getSubj());
    	tv = (TextView) this.m.getActivity().findViewById(R.id.txtReadEmailBody);
    	tv.setText(this.m.getCurMsg().getBody());
    	if(this.m.getCurMsg().getNumAttachment() > 0){
    		this.m.getActivity().findViewById(R.id.readEmailAttachment_LinLay).setVisibility(View.VISIBLE);
    		this.m.getActivity().findViewById(R.id.btnGetAttachment).setVisibility(View.VISIBLE);
    	}
	}
	
}
