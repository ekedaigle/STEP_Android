package step.email;

import step.address.ContactInfo;
import step.address.ContactList.ContactListItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.widget.*;

import com.step.launcher.R;
import com.sun.mail.pop3.POP3SSLStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.ParseException;


public class Mail{
	
	
	private Session session = null;
    private Store store = null;
    private String username, password;
    private Folder inbox;
    private Activity activity;
    //private TableModel tm;
    public Message[] msgs;
    private EmailList emailList;
    private ListView email_listView;
    private ArrayList<ContactInfo> cli_w_email;
    boolean pop3 = false;
    
    Mail(Activity a, ListView email_listView){
    	this.activity = a;
    	this.emailList = new EmailList();
    	this.email_listView = email_listView;
    	this.cli_w_email = new ArrayList<ContactInfo>();
    	getContactsWithEmail();
    };
    
    public ArrayList<ContactInfo> get_cli_w_email(){
    	return this.cli_w_email;
    }
    
    public void getContactsWithEmail(){
    	ContentResolver cr = this.activity.getContentResolver();
    	String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
    	Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, sortOrder);
        if (cur.getCount() > 0) {
		    while (cur.moveToNext()) {
		        String contactId = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
		        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		        Cursor emails = cr.query(Email.CONTENT_URI, null, Email.CONTACT_ID + " = " + contactId, null, null);
		        if(emails.moveToNext()) {
		            String email = emails.getString(emails.getColumnIndex(Email.DATA));
		            if(email!=null && !email.contentEquals("")){
			            ContactInfo ci = new ContactInfo();
			            ci.setName(name);
			            ci.setEmail(email);
			            this.cli_w_email.add(ci);
		            }
		        }
		    }
		}
    }
	
    public void setUserPass(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public void connect() throws Exception{
    	if(this.pop3){
    		connect_pop();
    	}
    	else
    	{
    		connect_imap();
    	}
    }
    public void connect_imap() throws Exception{
    	
    	Properties imapProps = new Properties();
    	imapProps.setProperty("mail.store.protocol", "imaps");
    	this.session = Session.getDefaultInstance(imapProps, null);
		this.store = this.session.getStore("imaps");
		this.store.connect("imap.gmail.com", this.username, this.password);
        //Download message headers from server
        // Open the Folder
        this.inbox = this.store.getDefaultFolder();
        this.inbox = this.inbox.getFolder("INBOX");
        
        if (this.inbox == null) {
            throw new Exception("Invalid folder");
        }
        
        // try to open read/write and if that fails try read-only
        try {
            
            this.inbox.open(Folder.READ_WRITE);
            
        } catch (MessagingException ex) {
            
            this.inbox.open(Folder.READ_ONLY);
            
        }
    	
    }
    public void connect_pop() throws Exception{
    	
    	String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties pop3Props = new Properties();
        pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
        pop3Props.setProperty("mail.pop3.port",  "995");
        pop3Props.setProperty("mail.pop3.socketFactory.port", "995");
        
        URLName url = new URLName("pop3", "pop.gmail.com", 995, "",
        		this.username, this.password);
        
        this.session = Session.getInstance(pop3Props, null);
        this.store = new POP3SSLStore(this.session, url);
        this.store.connect();
        
        //Download message headers from server
        // Open the Folder
        this.inbox = this.store.getDefaultFolder();
        this.inbox = this.inbox.getFolder("INBOX");
        
        if (this.inbox == null) {
            throw new Exception("Invalid folder");
        }
        
        // try to open read/write and if that fails try read-only
        try {
            
        	this.inbox.open(Folder.READ_WRITE);
            
        } catch (MessagingException ex) {
            
        	this.inbox.open(Folder.READ_ONLY);
            
        }

        
    }
    
    public void getMessages() throws Exception{
       this.emailList.clearMessages();
	   //Get folder's list of messages
	   this.msgs = this.inbox.getMessages();
	   //Retrieve message headers 
	   FetchProfile profile = new FetchProfile();
	   profile.add(FetchProfile.Item.ENVELOPE);
	   this.inbox.fetch(this.msgs, profile);
	   this.emailList.addMessages(this.msgs);
    }
    
    public void displayMessages() throws Exception{
		//setup the data adaptor
		EmailListAdapter adapter = new EmailListAdapter(this.activity, R.layout.message_list_element, this.emailList.getEmailList());
		this.email_listView.setAdapter(adapter);
    }
    
    public void readEmail(int idx, View v) throws Exception{
    	ReadEmailMessageTask task = new ReadEmailMessageTask(this, idx, v);
    	task.execute();
    }
    
    public void sendEmail() throws Exception{
    	Properties props = System.getProperties();
    	 
        props.put("mail.smtp.user", this.username);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", 
              "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
 
        // Required to avoid security exception.
        MyAuthenticator authentication = 
              new MyAuthenticator(this.username, this.password);
        Session smtpSession = 
              Session.getInstance(props,authentication);
        smtpSession.setDebug(true);
    	
    	
    	MimeMessage message = new MimeMessage(smtpSession);
    	EditText body = (EditText) this.activity.findViewById(R.id.compose_message_content);
    	EditText subj = (EditText) this.activity.findViewById(R.id.compose_subject);
    	TextView to   = (TextView) this.activity.findViewById(R.id.compose_to);
    	
    	message.setText(body.getText().toString());
    	message.setSubject(subj.getText().toString());
    	message.setFrom(new InternetAddress(this.username));
    	message.addRecipient(RecipientType.TO, new InternetAddress(to.getText().toString()));

    	SendEmailTask task = new SendEmailTask(this.activity, smtpSession.getTransport("smtps"), this.username, this.password, message);
    	task.execute();
    	
    }
    
    private class MyAuthenticator extends javax.mail.Authenticator {
        String User;
        String Password;
        public MyAuthenticator (String user, String password) {
            User = user;
            Password = password;
        }
         
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new javax.mail.PasswordAuthentication(User, Password);
        }
    }
    
    public void closeFolder() throws Exception {
    	this.inbox.close(false);
    }
}