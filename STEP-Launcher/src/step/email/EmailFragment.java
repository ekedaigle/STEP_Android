package step.email;
import step.*;
import com.step.launcher.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.app.Activity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.*;

public class EmailFragment extends Fragment {
	Mail gmail;
	
    private OnClickListener btnInboxListener = new OnClickListener() {
    	public void onClick(View v)
    	{
        	//Visible = 0
        	//Invisible = 1
        	//Gone = 2
    		getActivity().findViewById(R.id.tabelLayout1).setVisibility(View.VISIBLE);
    		getActivity().findViewById(R.id.txtReadEmail).setVisibility(View.GONE);
    	}
    };

    private OnClickListener btnGetMailListener = new OnClickListener() {
    	public void onClick(View v)
    	{
	    	UpdateInboxManually task = new UpdateInboxManually(EmailFragment.this.gmail);
	    	task.execute();
    	}
    };
    
    private OnClickListener btnRowSelectListener = new OnClickListener() {
    	public void onClick(View v)
    	{
    		getActivity().findViewById(R.id.tabelLayout1).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.txtReadEmail).setVisibility(View.VISIBLE);
        	TableRow row = (TableRow) v;
        	TextView idx = (TextView) row.getChildAt(3);
        	int i = Integer.parseInt((String)idx.getText());
        	TextView t = (TextView) getActivity().findViewById(R.id.txtConn);
        	t.setText(Integer.toString(i));
        	try
        	{
        		EmailFragment.this.gmail.readEmail(i, getActivity().findViewById(R.id.txtReadEmail));
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
		TableLayout t1 = (TableLayout) V.findViewById(R.id.tabelLayout1);
        TableModel tableModel = new TableModel(t1);
        
        V.findViewById(R.id.btnInbox).setOnClickListener(btnInboxListener);
        V.findViewById(R.id.btnUpdate).setOnClickListener(btnGetMailListener);
        V.findViewById(R.id.tableRow2).setOnClickListener(btnRowSelectListener);
        V.findViewById(R.id.tableRow3).setOnClickListener(btnRowSelectListener);
        V.findViewById(R.id.tableRow4).setOnClickListener(btnRowSelectListener);
        V.findViewById(R.id.tableRow5).setOnClickListener(btnRowSelectListener);
        V.findViewById(R.id.tableRow6).setOnClickListener(btnRowSelectListener);
        V.findViewById(R.id.tableRow7).setOnClickListener(btnRowSelectListener);
        this.gmail = new Mail(tableModel);
        gmail.setUserPass("capstone.group6.2012", "capstone2012");
        ConnectToEmailTask task = new ConnectToEmailTask(this.gmail, V.findViewById(R.id.txtConn));
    	task.execute();
    	return V;
	}
    

    
}
