package step.address;

public class ContactInfo {
	private String mName;
	private String mHomeNumber;
	private String mMobileNumber;
	private String mAddress;
	private String mEmail;
	private String _id;
	
	public void setName(String name) {
		this.mName = name;
	}
	
	public String getName() {
		return this.mName;
	}
	
	public void setId(String id){
		this._id = id;
	}
	
	public String getId() {
		return this._id;
	}
	
	public void setHomeNumber(String number) {
		this.mHomeNumber = number;
	}
	
	public String getHomeNumber() {
		return this.mHomeNumber;
	}
	
	public void setMobileNumber(String number) {
		this.mMobileNumber = number;
	}
	
	public String getMobileNumber() {
		return this.mMobileNumber;
	}
	
	public void setAddress(String addr) {
		this.mAddress = addr;
	}
	
	public String getAddress() {
		return this.mAddress;
	}
	
	public void setEmail(String email) {
		this.mEmail = email;
	}
	
	public String getEmail() {
		return this.mEmail;
	}
	
	
}
