package step.email;

import java.util.ArrayList;

import javax.mail.Address;

public class CurrentMessage {

	private Address[] mTo;
	private Address[] mFrom;
	private String mSubj;
	private int mNumAttachment;
	private ArrayList<String> mAttachmentLoc;
	private String mBody;
	private int msgNumber;
	
	public CurrentMessage(){
		mNumAttachment = 0;
		msgNumber = -1;
		mTo = null;
		mFrom = null;
		mSubj = null;
		mAttachmentLoc = new ArrayList<String>();
		mBody = null;
	}
	
	
	public void setMsgNumber(int num){
		msgNumber = num;
	}
	
	public int getMsgNumber(){
		return msgNumber;
	}
	
	public void clearMsgData(){
		mNumAttachment = 0;
		mTo = null;
		mFrom = null;
		mSubj = null;
		mAttachmentLoc.clear();
		mBody = null;
	}
	
	public void setTo(Address[] to){
		mTo = to;
	}
	
	public String getTo(){
		return mTo[0].toString();
	}
	
	public void setFrom(Address[] from){
		mFrom = from;
	}
	
	public String getFrom(){
		return mFrom[0].toString();
	}

	public void setSubj(String subj){
		mSubj = subj;
	}
	
	public String getSubj(){
		return mSubj;
	}
	
	public void addAttachmentLoc(String loc){
		mAttachmentLoc.add(loc);
		mNumAttachment = mAttachmentLoc.size();
	}
	
	public String getAttachmentLoc(int pos){
		return mAttachmentLoc.get(pos);
	}
	
	public int getNumAttachment(){
		return mNumAttachment;
	}
	
	public void setBody(String body){
		mBody = body;
	}
	
	public String getBody(){
		return mBody;
	}
	


}
