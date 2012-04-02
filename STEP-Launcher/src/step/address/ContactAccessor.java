package step.address;

import java.util.ArrayList;

import step.address.ContactList.ContactListItem;
import step.email.EmailListAdapter;

import com.step.launcher.R;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ContactAccessor {
	
	private boolean mShowInvisible;
	private Activity mActivity;
	private ContactList mContactList;
	private boolean mEmailNull;
	private boolean mAddressNull;
	private boolean mMobileNull;
	private boolean mHomeNull;
	
	
	public ContactAccessor(Activity act) {
		this.mActivity = act;
		this.mShowInvisible = false;
		this.mContactList = new ContactList();
	}
	
	/**
	 * Returns a Pick Contact intent using the "contacts" URI.
	 */
	public void populateContacts(ListView list) {
		this.mContactList.populateContactList(mShowInvisible, mActivity);
		ContactListAdapter adapter = new ContactListAdapter(this.mActivity, R.layout.contact_entry, this.mContactList.getContactList());
		list.setAdapter(adapter);
	}
	
	public ArrayList<ContactListItem> getContactList(){
		return this.mContactList.getContactList();
	}
	
	public void displayContactInfo(ContactInfo ci) {
		// Register Fields
		TextView _mid = (TextView) this.mActivity.findViewById(R.id.contact_id);
		EditText mName = (EditText) this.mActivity.findViewById(R.id.contact_DisplayName);
		EditText mHomeNumber = (EditText) this.mActivity.findViewById(R.id.contact_Homephone);
		EditText mMobileNumber = (EditText) this.mActivity.findViewById(R.id.contact_Mobilephone);
		EditText mAddress = (EditText) this.mActivity.findViewById(R.id.contact_address);
		EditText mEmail = (EditText) this.mActivity.findViewById(R.id.contact_email);
		
		mName.setText(ci.getName());
		mHomeNumber.setText(ci.getHomeNumber());
		mMobileNumber.setText(ci.getMobileNumber());
		mAddress.setText(ci.getAddress());
		mEmail.setText(ci.getEmail());
		_mid.setText(ci.getId());
		this.mActivity.findViewById(R.id.btnSave).setVisibility(View.GONE);
	}
	
	public void deleteContact(ContentResolver contentResolver) {
		TextView _mid = (TextView) this.mActivity.findViewById(R.id.contact_id);
		String contactId = _mid.getText().toString();
		String where = ContactsContract.Data.CONTACT_ID+ " = ? ";
		String[] params = new String[] {contactId};
		
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
    	        .withSelection(where, params)
    	        .build());
        try {
			contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void clearView() {
		TextView _mid = (TextView) this.mActivity.findViewById(R.id.contact_id);
		EditText mName = (EditText) this.mActivity.findViewById(R.id.contact_DisplayName);
		EditText mHomeNumber = (EditText) this.mActivity.findViewById(R.id.contact_Homephone);
		EditText mMobileNumber = (EditText) this.mActivity.findViewById(R.id.contact_Mobilephone);
		EditText mAddress = (EditText) this.mActivity.findViewById(R.id.contact_address);
		EditText mEmail = (EditText) this.mActivity.findViewById(R.id.contact_email);
		
		_mid.setText("");
		mName.setText("");
		mHomeNumber.setText("");
		mMobileNumber.setText("");
		mAddress.setText("");
		mEmail.setText("");
	}
	
	public void addContact(){
		EditText mName = (EditText) this.mActivity.findViewById(R.id.contact_DisplayName);
		EditText mHomeNumber = (EditText) this.mActivity.findViewById(R.id.contact_Homephone);
		EditText mMobileNumber = (EditText) this.mActivity.findViewById(R.id.contact_Mobilephone);
		EditText mAddress = (EditText) this.mActivity.findViewById(R.id.contact_address);
		EditText mEmail = (EditText) this.mActivity.findViewById(R.id.contact_email);
		
		String name = mName.getText().toString();
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		String where = "";
		String[] params = null;
		
		
		Cursor newNameCur = this.mActivity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
		        "DISPLAY_NAME = '" + name + "'", null, null);
		if(newNameCur.moveToFirst())
		{
			Log.d("CONTACT APP", "Name already exists");
			return;
		}
	    
	    if(name.contentEquals("") || name == null)
	    {
	    	Log.d("CONTACT APP", "Name cannot be blank");
	    	return;
	    }
	    
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
		// Set Address Parameters
		if(mAddress.getText().toString() != null){
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE)
					.withValue(StructuredPostal.TYPE, StructuredPostal.TYPE_HOME)
					.withValue(StructuredPostal.FORMATTED_ADDRESS, mAddress.getText().toString())
					.build());
		}

		// Set Email Parameters
		if(mEmail.getText().toString() != null){
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
					.withValue(Email.TYPE, Email.TYPE_HOME)
					.withValue(Email.DATA, mEmail.getText().toString())
					.build());
		}

		// Set Home Phone Parameters
		if(mHomeNumber.getText().toString() != null){
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
					.withValue(Phone.TYPE, Phone.TYPE_HOME)
					.withValue(Phone.NUMBER, mHomeNumber.getText().toString())
					.build());
		}
		// Set Cell Phone Parameters
		if(mMobileNumber.getText().toString() != null){
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
					.withValue(Phone.TYPE, Phone.TYPE_MOBILE)
					.withValue(Phone.NUMBER, mMobileNumber.getText().toString())
					.build());
		}
		try {
			//ops.add(builder.build());
			this.mActivity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (OperationApplicationException e){
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void modifyContactInfo() {
		// Register Fields
		
		//AccountManager am = AccountManager.get(this.mActivity);
		//Account[] accounts = am.getAccounts();
		
		TextView _mid = (TextView) this.mActivity.findViewById(R.id.contact_id);
		EditText mName = (EditText) this.mActivity.findViewById(R.id.contact_DisplayName);
		EditText mHomeNumber = (EditText) this.mActivity.findViewById(R.id.contact_Homephone);
		EditText mMobileNumber = (EditText) this.mActivity.findViewById(R.id.contact_Mobilephone);
		EditText mAddress = (EditText) this.mActivity.findViewById(R.id.contact_address);
		EditText mEmail = (EditText) this.mActivity.findViewById(R.id.contact_email);
		
		String contactId = _mid.getText().toString(); 
		String name = mName.getText().toString();
		ArrayList<ContentProviderOperation> update_ops = new ArrayList<ContentProviderOperation>();
		ArrayList<ContentProviderOperation> insert_ops = new ArrayList<ContentProviderOperation>();
		String where = "";
		String[] params = null;
		
		//this.mActivity.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder)
		Cursor OldNameCur = this.mActivity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
		        "_ID = '" + contactId + "'", null, null);
	    if (OldNameCur.moveToFirst()) {
	        String contactName = OldNameCur.getString(OldNameCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	        //String account = OldNameCur.getString(OldNameCur.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));
	        if(!contactName.contentEquals(name)){
				Cursor newNameCur = this.mActivity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
				        "DISPLAY_NAME = '" + name + "'", null, null);
				if(newNameCur.moveToFirst())
				{
					Log.d("CONTACT APP", "Name already exists");
					return;
				}
	        }
	    }
	    
	    if(name.contentEquals("") || name == null)
	    {
	    	Log.d("CONTACT APP", "Name cannot be blank");
	    	return;
	    }
		// Set Name Parameters
		params = new String [] {contactId};
		where = Data.CONTACT_ID + "=? AND " + Data.MIMETYPE +"='"+ StructuredName.CONTENT_ITEM_TYPE+"'";
		update_ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
				.withSelection(where, params)
				.withValue(StructuredName.DISPLAY_NAME, name)
				.build());
		// Set Address Parameters
		if(this.mAddressNull) {
			insert_ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValue(Data.RAW_CONTACT_ID, contactId)
					.withValue(Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE)
					.withValue(StructuredPostal.TYPE, StructuredPostal.TYPE_HOME)
					.withValue(StructuredPostal.FORMATTED_ADDRESS, mAddress.getText().toString())
					.build());
		} else {
			params = new String [] {contactId, String.valueOf(StructuredPostal.TYPE_HOME)};
			where = Data.CONTACT_ID + "=? AND " + Data.MIMETYPE +"='"+ StructuredPostal.CONTENT_ITEM_TYPE+"' AND "+StructuredPostal.TYPE + "=?";
			update_ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					.withSelection(where, params)
					.withValue(StructuredPostal.FORMATTED_ADDRESS, mAddress.getText().toString())
					.build());
		}
		// Set Email Parameters
		if(this.mEmailNull){
			insert_ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValue(Data.RAW_CONTACT_ID, contactId)
					.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
					.withValue(Email.TYPE, Email.TYPE_HOME)
					.withValue(Email.DATA, mEmail.getText().toString())
					.build());
		} else {
			params = new String [] {contactId, String.valueOf(Email.TYPE_HOME)};
			where = Data.CONTACT_ID + "=? AND " + Data.MIMETYPE +"='"+ Email.CONTENT_ITEM_TYPE+"' AND "+ Email.TYPE + "=?";
			update_ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					.withSelection(where, params)
					.withValue(Email.DATA, mEmail.getText().toString())
					.build());
		}
		// Set Home Phone Parameters
		if(this.mHomeNull){
			insert_ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValue(Data.RAW_CONTACT_ID, contactId)
					.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
					.withValue(Phone.TYPE, Phone.TYPE_HOME)
					.withValue(Phone.NUMBER, mHomeNumber.getText().toString())
					.build());
		} else {
			params = new String [] {contactId, String.valueOf(Phone.TYPE_HOME)};
			where = Data.CONTACT_ID + "=? AND " + Data.MIMETYPE +"='"+Phone.CONTENT_ITEM_TYPE+"' AND "+Phone.TYPE + "=?";
			update_ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					.withSelection(where, params)
					.withValue(Phone.NUMBER, mHomeNumber.getText().toString())
					.build());
		}
		// Set Cell Phone Parameters
		if(this.mMobileNull){
			insert_ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValue(Data.RAW_CONTACT_ID, contactId)
					.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
					.withValue(Phone.TYPE, Phone.TYPE_MOBILE)
					.withValue(Phone.NUMBER, mMobileNumber.getText().toString())
					.build());
		} else {
			params = new String [] {contactId, String.valueOf(Phone.TYPE_MOBILE)};
			where = Data.CONTACT_ID + "=? AND " + Data.MIMETYPE +"='"+Phone.CONTENT_ITEM_TYPE+"' AND "+Phone.TYPE + "=?";
			update_ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					.withSelection(where, params)
					.withValue(Phone.NUMBER, mMobileNumber.getText().toString())
					.build());
		}
		try {
			//ops.add(builder.build());
			if(!update_ops.isEmpty()){
				this.mActivity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, update_ops);
			}
			if(!insert_ops.isEmpty()){
				this.mActivity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, insert_ops);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (OperationApplicationException e){
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	public ContactInfo loadContact(ContentResolver contentResolver, String contactId) {
		ContactInfo contactInfo = new ContactInfo();
        contactInfo.setId(contactId);
		
	    Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
	        "_ID = '" + contactId + "'", null, null);
	    if (cursor.moveToFirst()) {
	        String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	    	//  Get all phone numbers.
			contactInfo.setName(contactName);
	        Cursor phones = contentResolver.query(Phone.CONTENT_URI, null,
	            Phone.CONTACT_ID + " = " + contactId, null, null);
	        while (phones.moveToNext()) {
	            String number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
	            int type = phones.getInt(phones.getColumnIndex(Phone.TYPE));
	            switch (type) {
	                case Phone.TYPE_HOME:
	                    contactInfo.setHomeNumber(number);
	                    break;
	                case Phone.TYPE_MOBILE:
	                	contactInfo.setMobileNumber(number);
	                    break;
	                }
	        }
	        phones.close();
	        //  Get all email addresses.
	        Cursor emails = contentResolver.query(Email.CONTENT_URI, null,
	            Email.CONTACT_ID + " = " + contactId, null, null);
	        while (emails.moveToNext()) {
	            String email = emails.getString(emails.getColumnIndex(Email.DATA));
	            int type = emails.getInt(emails.getColumnIndex(Phone.TYPE));
	            switch (type) {
	                case Email.TYPE_HOME:
	                    contactInfo.setEmail(email);
	                    break;
	                case Email.TYPE_WORK:
	                    // do something with the Work email here...
	                    break;
	            }
	        }
	        emails.close();
        //  Get all addresses.
        Cursor addresses = contentResolver.query(StructuredPostal.CONTENT_URI, null,
        		StructuredPostal.CONTACT_ID + " = " + contactId, null, null);
        while (addresses.moveToNext()) {
            String address = addresses.getString(addresses.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS));
            int type = addresses.getInt(addresses.getColumnIndex(StructuredPostal.TYPE));
            switch (type) {
                case StructuredPostal.TYPE_HOME:
                    contactInfo.setAddress(address);
                    break;
            }
        }
        addresses.close();
	}
	cursor.close();
	
	this.mAddressNull = contactInfo.getAddress()==null ? true : false;
	this.mEmailNull   = contactInfo.getEmail()==null ? true : false;
	this.mHomeNull    = contactInfo.getHomeNumber()==null ? true : false;
	this.mMobileNull  = contactInfo.getMobileNumber()==null ? true : false;
	
	return contactInfo;
	}
	

}
