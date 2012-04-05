package step.email;
import java.util.ArrayList;

import step.address.ContactInfo;

import com.step.launcher.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
    		getActivity().findViewById(R.id.btnGetAttachment).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.readEmailAttachment_LinLay).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.attachmentImg).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.lblEmailStart).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlReadEmail).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlCompose).setVisibility(View.VISIBLE);
    	}
    };

    
    private OnItemClickListener listItemSelectListener = new OnItemClickListener() {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    	{
    		getActivity().findViewById(R.id.btnGetAttachment).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.readEmailAttachment_LinLay).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.attachmentImg).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.lblEmailStart).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlReadEmail).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.scrlCompose).setVisibility(View.GONE);
    		
    		try
        	{
        		EmailFragment.this.mail.readEmail(position);
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
		ListView list = (ListView) V.findViewById(R.id.emailFrag_listview);
		list.setTextFilterEnabled(true);
		list.setOnItemClickListener(listItemSelectListener);
		//V.findViewById(R.id.btnInbox).setOnClickListener(btnInboxListener);
		V.findViewById(R.id.btnCompose).setOnClickListener(btnComposeListener);
		V.findViewById(R.id.btnSend).setOnClickListener(btnSendMailListener);
		V.findViewById(R.id.btnAddAddressee).setOnClickListener(btnAddAddressee);
		V.findViewById(R.id.btnGetAttachment).setOnClickListener(btnGetAttachmentListener);
		//setup the data source
		this.mail = new Mail(getActivity(), list);
        mail.setUserPass("capstone.group6.2012", "capstone2012");
        ConnectToEmailTask task = new ConnectToEmailTask(this.mail);
    	task.execute();
		
		return V;
	}
    
}
