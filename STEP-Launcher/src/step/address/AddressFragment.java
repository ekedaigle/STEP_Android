package step.address;

import step.email.EmailFragment;

import com.step.launcher.R;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

public class AddressFragment extends Fragment {
	private ContactAccessor mContactAccessor;
	private ListView mContactListView;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        View V = inflater.inflate(R.layout.address_fragment, container, false);
        this.mContactAccessor = new ContactAccessor(getActivity());
        mContactListView = (ListView) V.findViewById(R.id.addressFrag_listview);
        mContactListView.setOnItemClickListener(listItemSelectListener);
        V.findViewById(R.id.btnSave).setOnClickListener(btnSaveListener);
        EditText text = (EditText) V.findViewById(R.id.contact_address);
        text.addTextChangedListener(textWatcher);
        text = (EditText) V.findViewById(R.id.contact_DisplayName);
        text.addTextChangedListener(textWatcher);
        text = (EditText) V.findViewById(R.id.contact_email);
        text.addTextChangedListener(textWatcher);
        text = (EditText) V.findViewById(R.id.contact_Homephone);
        text.addTextChangedListener(textWatcher);
        text = (EditText) V.findViewById(R.id.contact_Mobilephone);
        text.addTextChangedListener(textWatcher);
        
        populateContactList();
        
        return V;

	}
	
	private TextWatcher textWatcher = new TextWatcher() {
		public void afterTextChanged(Editable s){
		}
		
		public void beforeTextChanged(CharSequence s, int start, int before, int count){
		}
		
		public void onTextChanged(CharSequence s, int start, int before, int count){
			getActivity().findViewById(R.id.btnSave).setVisibility(View.VISIBLE);
		}
		
	};
	
    private OnClickListener btnSaveListener = new OnClickListener() {
    	public void onClick(View v)
    	{
    		EditText et = (EditText) AddressFragment.this.getActivity().findViewById(R.id.contact_DisplayName);
    		String name = et.getText().toString();
    		AddressFragment.this.mContactAccessor.modifyContactInfo(name);
    		populateContactList();
    		getActivity().findViewById(R.id.btnSave).setVisibility(View.GONE);
    	}
    };
	
    private OnItemClickListener listItemSelectListener = new OnItemClickListener() {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    	{
    		ContactInfo ci;
    		String name, _id;
    		try
        	{
    			_id = AddressFragment.this.mContactAccessor.getContactList().get(position).mContactId;
        		ci = AddressFragment.this.mContactAccessor.loadContact(getActivity().getContentResolver(), _id);
        		AddressFragment.this.mContactAccessor.displayContactInfo(ci);
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
        		
    	}
    };
	
	private void populateContactList() {
		this.mContactAccessor.populateContacts(mContactListView);
//        Cursor cursor = mContactAccessor.getContacts();
//        String[] fields = new String[] {
//        		ContactsContract.Data.DISPLAY_NAME
//        };
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.contact_entry, cursor,
//        		fields, new int[] {R.id.contactEntryText});
//        this.mContactList.setAdapter(adapter);
	}
}
