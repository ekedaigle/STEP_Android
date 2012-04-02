package step.address;

import step.email.EmailFragment;

import com.step.launcher.R;

import android.app.Fragment;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
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
	private boolean addingNew = false;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        View V = inflater.inflate(R.layout.address_fragment, container, false);
        this.mContactAccessor = new ContactAccessor(getActivity());
        
//        buttons = new Button[NUM_BUTTONS];
//        buttons[MUSIC] = (Button)findViewById(R.id.music_button);
//        buttons[NEWSPAPER] = (Button)findViewById(R.id.newspaper_button);
//        buttons[ADDRESS] = (Button)findViewById(R.id.address_button);
//        buttons[EMAIL] = (Button)findViewById(R.id.email_button);
//        
//        button_icons_normal = new Drawable[NUM_BUTTONS];
//        Resources res = getResources();
//        button_icons_normal[MUSIC] = res.getDrawable(R.drawable.music_button_landscape);
//        button_icons_normal[NEWSPAPER] = res.getDrawable(R.drawable.newspaper_button_landscape);
//        button_icons_normal[ADDRESS] = res.getDrawable(R.drawable.address_button_landscape);
//        button_icons_normal[EMAIL] = res.getDrawable(R.drawable.email_button_landscape);
//        
//        button_icons_selected = new Drawable[NUM_BUTTONS];
//        button_icons_selected[MUSIC] = res.getDrawable(R.drawable.music_button_pressed_landscape);
//        button_icons_selected[NEWSPAPER] = res.getDrawable(R.drawable.newspaper_button_pressed_landscape);
//        button_icons_selected[ADDRESS] = res.getDrawable(R.drawable.address_button_pressed_landscape);
//        button_icons_selected[EMAIL] = res.getDrawable(R.drawable.email_button_pressed_landscape);
        
        
        
        
        mContactListView = (ListView) V.findViewById(R.id.addressFrag_listview);
        mContactListView.setOnItemClickListener(listItemSelectListener);
        V.findViewById(R.id.btnSave).setOnClickListener(btnSaveListener);
        V.findViewById(R.id.btnDelete).setOnClickListener(btnDeleteListener);
        V.findViewById(R.id.btnAddContact).setOnClickListener(btnAddContactListener);
        V.findViewById(R.id.btnAddSave).setOnClickListener(btnAddSaveListener);
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
			if(AddressFragment.this.addingNew == true){
				getActivity().findViewById(R.id.btnAddSave).setVisibility(View.VISIBLE);
			} else {
				getActivity().findViewById(R.id.btnSave).setVisibility(View.VISIBLE);
			}
		}
		
	};
	
    private OnClickListener btnAddContactListener = new OnClickListener() {
    	public void onClick(View v)
    	{
    		AddressFragment.this.addingNew = true;
    		AddressFragment.this.mContactAccessor.clearView();
    		populateContactList();
    		getActivity().findViewById(R.id.btnAddSave).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.btnSave).setVisibility(View.GONE);
    		getActivity().findViewById(R.id.btnDelete).setVisibility(View.GONE);
    	}
    };
	
    private OnClickListener btnAddSaveListener = new OnClickListener() {
    	public void onClick(View v)
    	{
    		AddressFragment.this.addingNew = false;
    		AddressFragment.this.mContactAccessor.addContact();
    		populateContactList();
    		getActivity().findViewById(R.id.btnAddSave).setVisibility(View.GONE);
    	}
    };
    
    private OnClickListener btnSaveListener = new OnClickListener() {
    	public void onClick(View v)
    	{
    		TextView _mid = (TextView) getActivity().findViewById(R.id.contact_id);
    		String contactId = _mid.getText().toString();
    		AddressFragment.this.mContactAccessor.modifyContactInfo();
    		AddressFragment.this.mContactAccessor.loadContact(getActivity().getContentResolver(), contactId);
    		populateContactList();
    		getActivity().findViewById(R.id.btnSave).setVisibility(View.GONE);
    	}
    };
    
    private OnClickListener btnDeleteListener = new OnClickListener() {
    	public void onClick(View v)
    	{
    		AddressFragment.this.mContactAccessor.deleteContact(getActivity().getContentResolver());
    		populateContactList();
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
        		AddressFragment.this.addingNew = false;
        		getActivity().findViewById(R.id.btnAddSave).setVisibility(View.GONE);
        		getActivity().findViewById(R.id.btnSave).setVisibility(View.GONE);
        		getActivity().findViewById(R.id.btnDelete).setVisibility(View.VISIBLE);
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
