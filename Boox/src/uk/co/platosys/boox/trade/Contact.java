package uk.co.platosys.boox.trade;

import uk.co.platosys.xuser.Xcontact;
import uk.co.platosys.xuser.Xaddress;
import uk.co.platosys.xuser.XuserException;

public class Contact extends Xcontact{

private String givenName;
  private String familyName;
  private String email;
  private String mobile;
  private Xaddress xaddress;
  private String contactID;
  
    public Contact(String xcontactID) throws XuserException {
		super(xcontactID);
		
	}
public void setGivenName(String givenName) throws XuserException {
	setFieldValue(Xcontact.GIVEN_NAME, givenName);
	this.givenName=givenName;
}
public String getGivenName() throws XuserException {
	if(givenName==null){
		return getFieldValue(Xcontact.GIVEN_NAME);
	}else{
		return givenName;
	}
}

public void setFamilyName(String familyName) throws XuserException {
	setFieldValue(Xcontact.FAMILY_NAME, familyName);
	this.familyName=familyName;
}
public String getFamilyName() throws XuserException {
	if(familyName==null){
		return getFieldValue(Xcontact.GIVEN_NAME);
	}else{
		return familyName;
	}
}
public void setEmail(String email) throws XuserException {
	setFieldValue(Xcontact.EMAIL, email);
	this.email=email;
}
public String getEmail() throws XuserException {
	if(email==null){
		return getFieldValue(Xcontact.EMAIL);
	}else{
		return email;
	}
}
public void setMobile(String mobile) throws XuserException {
	setFieldValue(Xcontact.GIVEN_NAME, mobile);
	this.mobile=mobile;
}
public String getMobile() throws XuserException {
	if(mobile==null){
		return getFieldValue(Xcontact.MOBILE);
	}else{
		return mobile;
	}
}

public void setXaddress(Xaddress xaddress) {
	this.xaddress = xaddress;
}
public Xaddress getXaddress() {
	return xaddress;
}
public void setContactID(String contactID) {
	this.contactID = contactID;
}
public String getContactID() {
	return contactID;
}
	
}

