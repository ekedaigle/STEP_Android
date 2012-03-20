package step.email;

import java.util.ArrayList;
import java.util.Date;

import javax.mail.Address;
import javax.mail.Message;

public class EmailList {
	private ArrayList<EmailListItem> emails;
	
	EmailList(){
		this.emails = new ArrayList<EmailListItem>();
	}
	
	public ArrayList<EmailListItem> getEmailList(){
		return this.emails;
	}
	
	public void addMessages(Message[] msgs) throws Exception {
		EmailListItem item; 
		for(int i=0; i<msgs.length; i++)
		{
			item = new EmailListItem();
			item.fromMsg(msgs[i]);
			this.emails.add(item);
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

		
		//Default Constructor
		public EmailListItem() {
			this("Subject", "From", "Date");
		}
		
		//main constructor
		public EmailListItem(String subject, String from, String date){
			super();
			this.subject = subject;
			this.from = from;
			this.date = date;
		}
		
		public void fromMsg(Message msg) throws Exception{
			Address[] a;
			Date d;
			a = msg.getFrom();
			this.from = a[0].toString();
			d = msg.getSentDate();
			this.date = d.toString();
			this.subject = msg.getSubject();
		}
	}
}
