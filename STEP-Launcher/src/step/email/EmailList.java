package step.email;

import java.util.ArrayList;
import java.util.Date;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.Folder;

public class EmailList {
	private ArrayList<EmailListItem> emails;
	
	EmailList(){
		this.emails = new ArrayList<EmailListItem>();
	}
	
	public ArrayList<EmailListItem> getEmailList(){
		return this.emails;
	}
	
	public void clearMessages(){
		this.emails.clear();
	}
	
	public void addMessages(Message[] msgs) throws Exception {
		EmailListItem item; 
		for(int i=msgs.length-1; i>=0; i--)
		{
			item = new EmailListItem();
			item.fromMsg(msgs[i]);
			this.emails.add(item);
			//Folder.getUID(msgs[i]);
		}
	}
	
	public void addSingleMessage(Message msg) throws Exception {
		EmailListItem item = new EmailListItem();
		item.fromMsg(msg);
		this.emails.add(item);
	}
	
	public class EmailListItem {

		public String subject;
		public String from;
		public String date;
		public boolean isNew;

		
		//Default Constructor
		public EmailListItem() {
			this("Subject", "From", "Date", false);
		}
		
		//main constructor
		public EmailListItem(String subject, String from, String date, boolean is_new){
			super();
			this.subject = subject;
			this.from = from;
			this.date = date;
			this.isNew = is_new;
		}
		
		public void fromMsg(Message msg) throws Exception{
			Address[] a;
			Date d;
			a = msg.getFrom();
			this.from = a[0].toString();
			d = msg.getSentDate();
			this.date = d.toString();
			this.subject = msg.getSubject();
			this.isNew = !msg.isSet(Flags.Flag.SEEN);
		}
	}
}
