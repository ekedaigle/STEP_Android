package step.address;


import java.util.ArrayList;

import com.step.launcher.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import step.address.ContactList.ContactListItem;

public class ContactListAdapter extends BaseAdapter{
	// store the context (as an inflated layout)
	private LayoutInflater inflater;
	// store the resource (typically list_item.xml)
	private int resource;
	// store (a reference to) the data
	private ArrayList<ContactListItem> data;
	
	/**
	 * Default constructor. Creates the new Adapter object to
	 * provide a ListView with data.
	 * @param context
	 * @param resource
	 * @param data
	 */
	public ContactListAdapter(Context context, int resource, ArrayList<ContactListItem> data) {
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = resource;
		this.data = data;
	}
	
	
	/**
	 * Return the size of the data set.
	 */
	public int getCount() {
		return this.data.size();
	}
	
	/**
	 * Return an object in the data set.
	 */
	public Object getItem(int position){
		return this.data.get(position);
	}
	
	/**
	 * Return the position provided.
	 */
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * Return a generated view for a position.
	 */
	public View getView(int position, View convertView, ViewGroup parent){
		//reuse a given view, or inflate a new one from the xml
		View view;
		
		if(convertView == null) {
			view = this.inflater.inflate(resource, parent, false);
		} else {
			view = convertView;
		}
		
		// bind the data to the view object
		return this.bindData(view, position);
	}
		
	/**
	 * Bind the provided data to the view
	 * This is the only method not required by base adapter.		
	 */
	public View bindData(View view, int position){
		// make sure it's worth drawing the view
		if (this.data.get(position) == null){
			return view;
		}
		
		// pull out the object
		ContactListItem item = this.data.get(position);
		
		// Extract the view object
		View viewElement = view.findViewById(R.id.contactEntryText);
		// cast to the correct type
		TextView tv = (TextView) viewElement;
		// set the value
		tv.setText(item.mContactName);
		
//		viewElement = view.findViewById(R.id.emailList_from);
//		tv = (TextView) viewElement;
//		tv.setText(item.from);
//		
//		viewElement = view.findViewById(R.id.emailList_date);
//		tv = (TextView) viewElement;
//		tv.setText(item.date);
		
		// return final view object
		return view;
	}
}
