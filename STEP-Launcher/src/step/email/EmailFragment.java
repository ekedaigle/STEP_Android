package step.email;
import java.util.ArrayList;

import step.address.ContactInfo;

import com.step.launcher.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentProviderOperation;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EmailFragment extends Fragment {

	//define data source
	public Mail mail;
	
	public void setMail(Mail m){
		this.mail = m;
	}
	
	public View getSidebarView(){
		return getActivity().findViewById(R.id.emailFrag_listview);
	}
	
    private OnClickListener btnAddAddressee = new OnClickListener() {
    	public void onClick(View v)
    	{
    		//Declare Dialog box for selecting Contacts
    		final Dialog listDialog = new Dialog(getActivity());
    		listDialog.setTitle("Pick a Contact");
    		
    		//Setup header button
    		LayoutInflater inflater = getActivity().getLayoutInflater();
    		View header = inflater.inflate(R.layout.email_contact_dialog_header, null);
    		
    		Button btnEnterEmail = (Button) header.findViewById(R.id.btnEnterEmailManually);
    		btnEnterEmail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
					final EditText edtEmail = new EditText(getActivity());
					edtEmail.setHint("Enter Email Address");
					edtEmail.setTextSize(TypedValue.COMPLEX_UNIT_PT, 12f);
					edtEmail.setInputType(InputType.TYPE_CLASS_TEXT | 
							InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS); //textEmailType

					alert.setTitle("Enter Recipient Email Address");
					alert.setView(edtEmail);
					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							TextView tv = (TextView) getActivity().findViewById(R.id.compose_to);
							tv.setText(edtEmail.getText().toString());
							getActivity().findViewById(R.id.recipientLinLay).setVisibility(View.VISIBLE);
							//Toast.makeText(getActivity(), edtEmail.getText().toString(), Toast.LENGTH_SHORT).show();
						}
					});
					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					
					listDialog.dismiss();
					alert.show();
				}
    		});
    		
    		btnEnterEmail.setEnabled(true);
    		
    		ArrayList<ContactInfo> cli = EmailFragment.this.mail.get_cli_w_email();
        	ListView lv = new ListView(getActivity());
        	lv.addHeaderView(header);
    		DialogListAdapter adapter = new DialogListAdapter(getActivity(), R.layout.email_dialog_list_element, cli);
    		lv.setAdapter(adapter);
    		
    		lv.setOnItemClickListener(new OnItemClickListener() {
    			@Override
    			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    			{
    				ArrayList<ContactInfo> ci = EmailFragment.this.mail.get_cli_w_email();
					TextView tv = (TextView) getActivity().findViewById(R.id.compose_to);
					tv.setText(ci.get(position-1).getEmail());
					getActivity().findViewById(R.id.recipientLinLay).setVisibility(View.VISIBLE);
    				//Toast.makeText(getActivity(),  ci.get(position-1).getEmail(), Toast.LENGTH_SHORT).show();
    				listDialog.dismiss();
        		
    			}
    		});
    		
    		listDialog.setContentView(lv);
    		listDialog.setCancelable(true);

        	
    		listDialog.show();
    	}
    };
	
	
    private OnClickListener btnSendMailListener = new OnClickListener() {
    	public void onClick(View v)
    	{
    		getActivity().findViewById(R.id.lblEmailStart).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.scrlReadEmail).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlCompose).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.recipientLinLay).setVisibility(View.GONE);
    		Toast.makeText(getActivity(), "Sending Email ..." , Toast.LENGTH_SHORT).show();
    		try{
    			EmailFragment.this.mail.sendEmail();
    		} catch (Exception e) {
    			Toast.makeText(getActivity(), "Create Message Failed!", Toast.LENGTH_SHORT).show();
    			e.printStackTrace();
    		}
    	}
    };
    
    private OnClickListener btnGetAttachmentListener = new OnClickListener() {
    	public void onClick(View v)
    	{
        	//Visible = 0
        	//Invisible = 1
        	//Gone = 2
    		ImageView iv = (ImageView) getActivity().findViewById(R.id.attachmentImg);
    		CurrentMessage cm = EmailFragment.this.mail.getCurMsg();
    		Toast.makeText(getActivity(), cm.getAttachmentLoc(0), Toast.LENGTH_SHORT).show();
    		Bitmap bmp = BitmapFactory.decodeFile(EmailFragment.this.mail.getCurMsg().getAttachmentLoc(0));
    		iv.setImageBitmap(bmp);
    		getActivity().findViewById(R.id.btnGetAttachment).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.attachmentImg).setVisibility(View.VISIBLE);
    	}
    };
    
    private OnClickListener btnComposeListener = new OnClickListener() {
    	public void onClick(View v)
    	{
        	//Visible = 0
        	//Invisible = 1
        	//Gone = 2
			TextView tv = (TextView) getActivity().findViewById(R.id.compose_to);
			tv.setText("");
			tv = (TextView) getActivity().findViewById(R.id.compose_subject);
			tv.setText("");
			tv = (TextView) getActivity().findViewById(R.id.compose_message_content);
			tv.setText("");
			getActivity().findViewById(R.id.recipientLinLay).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.btnGetAttachment).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.readEmailAttachment_LinLay).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.attachmentImg).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.lblEmailStart).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlReadEmail).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlCompose).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.btnDeleteMsg).setVisibility(View.GONE);
	    	getActivity().findViewById(R.id.btnReplyMsg).setVisibility(View.GONE);
    	}
    };

    private OnClickListener btnDeleteListener = new OnClickListener() {
    	public void onClick(View v)
    	{
        	//Visible = 0
        	//Invisible = 1
        	//Gone = 2
    		Toast.makeText(getActivity(), "Deleting Message ...", Toast.LENGTH_SHORT).show();
			DeleteMessageTask task = new DeleteMessageTask(EmailFragment.this.mail);
			task.execute();
    		getActivity().findViewById(R.id.readEmailAttachment_LinLay).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.attachmentImg).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.lblEmailStart).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.scrlReadEmail).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlCompose).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.btnDeleteMsg).setVisibility(View.GONE);
	    	getActivity().findViewById(R.id.btnReplyMsg).setVisibility(View.GONE);
    	}
    };
    
    private OnClickListener btnReplyListener = new OnClickListener() {
    	public void onClick(View v)
    	{
        	//Visible = 0
        	//Invisible = 1
        	//Gone = 2
			TextView tv = (TextView) getActivity().findViewById(R.id.compose_to);
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			String where = "";
			String[] params = null;
			
			int start = EmailFragment.this.mail.getCurMsg().getFrom().indexOf('<')+1;
			int end = EmailFragment.this.mail.getCurMsg().getFrom().indexOf('>');
			int nStart = 0;
			int nEnd = start - 2;
			
			if(start <= -1 || end <= -1){
				tv.setText(EmailFragment.this.mail.getCurMsg().getFrom());
			} else {
				String email = EmailFragment.this.mail.getCurMsg().getFrom().substring(start, end);
				String name = EmailFragment.this.mail.getCurMsg().getFrom().substring(nStart, nEnd);
				Cursor newNameCur = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
				        "DISPLAY_NAME = '" + name + "'", null, null);
				if(!(newNameCur.moveToFirst()||name.contentEquals("") || name == null))
				{
					ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				             .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				             .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
				             .build());
					// Set Name Parameters
					ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
							.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
							.withValue(StructuredName.DISPLAY_NAME, name)
							.build());
					if(email != null){
						ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
								.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
								.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
								.withValue(Email.TYPE, Email.TYPE_HOME)
								.withValue(Email.DATA, email)
								.build());
					}
					try {
						//ops.add(builder.build());
						getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);

					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (OperationApplicationException e){
						e.printStackTrace();
					} catch (Exception e){
						e.printStackTrace();
					}
				}
				EmailFragment.this.mail.getContactsWithEmail();
				tv.setText(email);
			}
			tv = (TextView) getActivity().findViewById(R.id.compose_subject);
			tv.setText("");
			tv = (TextView) getActivity().findViewById(R.id.compose_message_content);
			tv.setText("");
    		getActivity().findViewById(R.id.btnGetAttachment).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.readEmailAttachment_LinLay).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.attachmentImg).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.lblEmailStart).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlReadEmail).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlCompose).setVisibility(View.VISIBLE);
			getActivity().findViewById(R.id.recipientLinLay).setVisibility(View.VISIBLE);
			getActivity().findViewById(R.id.btnDeleteMsg).setVisibility(View.GONE);
	    	getActivity().findViewById(R.id.btnReplyMsg).setVisibility(View.GONE);
    	}
    };
    
    
    private OnItemClickListener listItemSelectListener = new OnItemClickListener() {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    	{
    		TextView tv = (TextView) getActivity().findViewById(R.id.txtReadEmailBody);
    		tv.setText("");
    		tv = (TextView) getActivity().findViewById(R.id.txtReadEmailFrom);
    		tv.setText("");
    		tv = (TextView) getActivity().findViewById(R.id.txtReadEmailSubject);
    		tv.setText("");
    		Toast.makeText(getActivity(), "Getting Email" , Toast.LENGTH_SHORT).show();
    		getActivity().findViewById(R.id.btnGetAttachment).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.readEmailAttachment_LinLay).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.attachmentImg).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.lblEmailStart).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlReadEmail).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.scrlCompose).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.btnDeleteMsg).setVisibility(View.GONE);
	    	getActivity().findViewById(R.id.btnReplyMsg).setVisibility(View.GONE);
    		EmailList eml = EmailFragment.this.mail.getEmailList();
    		int msgListPos = -1;
    		if(eml.getEmailList().get(position).isNew == true){
    			EmailFragment.this.mail.getEmailList().getEmailList().get(position).isNew = false;
    			view.findViewById(R.id.msgIsNew).setVisibility(View.GONE);
    		}
			
    		try
        	{
    			msgListPos = eml.getEmailList().size()-position-1;
        		
        		EmailFragment.this.mail.readEmail(msgListPos);
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
        		
    	}
    };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View V = inflater.inflate(R.layout.email_fragment, container, false);
		ListView list = (ListView) V.findViewById(R.id.emailFrag_listview).findViewById(R.id.buttonListViewFrameLayout);
		list.setTextFilterEnabled(true);
		list.setOnItemClickListener(listItemSelectListener);
		this.mail.setListView(list);
		//V.findViewById(R.id.btnInbox).setOnClickListener(btnInboxListener);
		V.findViewById(R.id.btnCompose).setOnClickListener(btnComposeListener);
		V.findViewById(R.id.btnSend).setOnClickListener(btnSendMailListener);
		V.findViewById(R.id.btnAddAddressee).setOnClickListener(btnAddAddressee);
		V.findViewById(R.id.btnGetAttachment).setOnClickListener(btnGetAttachmentListener);
		V.findViewById(R.id.btnDeleteMsg).setOnClickListener(btnDeleteListener);
		V.findViewById(R.id.btnReplyMsg).setOnClickListener(btnReplyListener);
		//setup the data source
		if(this.mail.haveMsgs()){
			try
			{
				this.mail.displayMessages();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		} else {
			DisplayMessagesTask task = new DisplayMessagesTask(this.mail);
			task.execute();
		}
		
		return V;
	}
    
}
