package step.address;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactList {
	
	private ArrayList<ContactListItem> mContactList;
	
	public ContactList() {
		this.mContactList = new ArrayList<ContactListItem>();
	}
	
	public void populateContactList(boolean showInvisible, Activity activity) {
		// Run query
		this.mContactList.clear();
		ContentResolver cr = activity.getContentResolver();
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] {
				ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME
		};
		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
				(showInvisible ? "0" : "1") + "'";
		String[] selectionArgs = null;
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
		Cursor cursor = activity.managedQuery(uri, projection, selection, selectionArgs, sortOrder);
		while(cursor.moveToNext()){
			ContactListItem cli = new ContactListItem(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
					cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
			this.mContactList.add(cli);
		}
	}
	
	public ArrayList<ContactListItem> getContactList(){
		return this.mContactList;
	}
	
	public class ContactListItem {
		public String mContactId;
		public String mContactName;
		
		public ContactListItem(){
			mContactId = null;
			mContactName = null;
		}
		public ContactListItem(String id, String name){
			mContactId = id;
			mContactName = name;
		}
	}
	
	
	
	

}
