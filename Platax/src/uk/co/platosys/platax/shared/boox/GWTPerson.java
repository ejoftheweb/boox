package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

import uk.co.platosys.platax.shared.HasSysname;
import uk.co.platosys.pws.values.PWSAddress;

/**
 * Class to wrap personal information
 * @author edward
 *
 */
public class GWTPerson implements HasSysname,  Serializable {
	private String phoneNo;
	private PWSAddress address;
	private String givenName;
	private String familyName;
	private String email;
	private String nationality;
	private String sysname;
public GWTPerson (){}
public String getPhoneNo() {
	return phoneNo;
}
public void setPhoneNo(String phoneNo) {
	this.phoneNo = phoneNo;
}
public PWSAddress getAddress() {
	return address;
}
public void setAddress(PWSAddress address) {
	this.address = address;
}
public String getGivenName() {
	return givenName;
}
public void setGivenName(String givenName) {
	this.givenName = givenName;
}
public String getFamilyName() {
	return familyName;
}
public void setFamilyName(String familyName) {
	this.familyName = familyName;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
/**
 * @return the nationality*/

public String getNationality() {
	return nationality;
}
/**
 * @param nationality the nationality to set*/

public void setNationality(String nationality) {
	this.nationality = nationality;
}
public String getName(){
	return givenName+" "+familyName;
}
@Override
public String getSysname() {
	return sysname;
}
@Override
public void setSysname(String sysname) {
	this.sysname=sysname;
	
}
}
