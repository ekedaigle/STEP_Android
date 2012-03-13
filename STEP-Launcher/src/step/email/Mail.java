package step.email;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.*;

import com.sun.mail.pop3.POP3SSLStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.ParseException;


public class Mail{
	
	private Session session = null;
    private Store store = null;
    private String username, password;
    private Folder folder;
    private TableModel tm;
    public Message[] msgs;
    boolean pop3 = false;
    
    Mail(TableModel t1){
    	this.tm = t1;
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
		String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		Properties imapProps = new Properties();
		imapProps.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
		imapProps.setProperty("mail.imap.socketFactory.fallback", "false");
        imapProps.setProperty("mail.imap.port",  "993");
        imapProps.setProperty("mail.imap.socketFactory.port", "993");
        
        URLName url = new URLName("pop3", "imap.gmail.com", 993, "", username, password);
        this.session = Session.getInstance(imapProps, null);
        this.store = this.session.getStore(url);
        this.store.connect();
    }
    public void connect_pop() throws Exception{
    	String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        
        Properties pop3Props = new Properties();
        
        pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
        pop3Props.setProperty("mail.pop3.port",  "995");
        pop3Props.setProperty("mail.pop3.socketFactory.port", "995");
        
        URLName url = new URLName("pop3", "pop.gmail.com", 995, "",
                username, password);
        
        session = Session.getInstance(pop3Props, null);
        store = new POP3SSLStore(session, url);
        store.connect();
        
        //Download message headers from server
        // Open the Folder
        folder = store.getDefaultFolder();
        
        folder = folder.getFolder("INBOX");
        
        if (folder == null) {
            throw new Exception("Invalid folder");
        }
        
        // try to open read/write and if that fails try read-only
        try {
            
            folder.open(Folder.READ_WRITE);
            
        } catch (MessagingException ex) {
            
            folder.open(Folder.READ_ONLY);
            
        }

        
    }
    
    public void getMessages() throws Exception{
	   //Get folder's list of messages
	   this.msgs = folder.getMessages();
	   //Retrieve message headers 
	   FetchProfile profile = new FetchProfile();
	   profile.add(FetchProfile.Item.ENVELOPE);
	   folder.fetch(this.msgs, profile);

    }
    
    public void displayMessages() throws Exception{

        tm.setMessages(this.msgs);
    }
    
    public void readEmail(int idx, View v) throws Exception{
    	ReadEmailMessageTask task = new ReadEmailMessageTask(this, idx, v);
    	task.execute();
    }
    
    public void closeFolder() throws Exception {
        folder.close(false);
    }
}