package step.email;
import com.step.launcher.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class EmailFragment extends Fragment {

	//define data source
	public Mail mail;
	
    private OnClickListener btnSyncListener = new OnClickListener() {
    	public void onClick(View v)
    	{
        	UpdateInboxTask task = new UpdateInboxTask(EmailFragment.this.mail);
        	task.execute();
    	}
    };
	
	
    private OnClickListener btnSendMailListener = new OnClickListener() {
    	public void onClick(View v)
    	{
    		getActivity().findViewById(R.id.emailFrag_listview).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.scrlReadEmail).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlCompose).setVisibility(View.GONE);
    		TextView tv = (TextView) getActivity().findViewById(R.id.txtConn);
    		tv.setText("Sending Email");
    		try{
    			EmailFragment.this.mail.sendEmail();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    };
    
    private OnClickListener btnInboxListener = new OnClickListener() {
    	public void onClick(View v)
    	{
        	//Visible = 0
        	//Invisible = 1
        	//Gone = 2
    		getActivity().findViewById(R.id.emailFrag_listview).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.scrlReadEmail).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlCompose).setVisibility(View.GONE);
    	}
    };
    
    private OnClickListener btnComposeListener = new OnClickListener() {
    	public void onClick(View v)
    	{
        	//Visible = 0
        	//Invisible = 1
        	//Gone = 2
    		getActivity().findViewById(R.id.emailFrag_listview).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlReadEmail).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlCompose).setVisibility(View.VISIBLE);
    	}
    };

    
    private OnItemClickListener listItemSelectListener = new OnItemClickListener() {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    	{
    		getActivity().findViewById(R.id.emailFrag_listview).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.scrlReadEmail).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.scrlCompose).setVisibility(View.GONE);
    		
    		try
        	{
        		EmailFragment.this.mail.readEmail(position, getActivity().findViewById(R.id.txtReadEmail));
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
		V.findViewById(R.id.btnInbox).setOnClickListener(btnInboxListener);
		V.findViewById(R.id.btnCompose).setOnClickListener(btnComposeListener);
		V.findViewById(R.id.btnSend).setOnClickListener(btnSendMailListener);
		V.findViewById(R.id.btnSync).setOnClickListener(btnSyncListener);
		//setup the data source
		this.mail = new Mail(getActivity(), list);
        mail.setUserPass("capstone.group6.2012", "capstone2012");
        ConnectToEmailTask task = new ConnectToEmailTask(this.mail, V.findViewById(R.id.txtConn));
    	task.execute();
		
		return V;
	}
    
}
