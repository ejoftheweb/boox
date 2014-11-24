package uk.co.platosys.platax.shared.boox;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GWTSupplier implements IsSerializable{
private String name;
private String contactID;
private String sysname;
/**
 * @return the sysname
 */
public String getSysname() {
	return sysname;
}

/**
 * @param sysname the sysname to set
 */
public void setSysname(String sysname) {
	this.sysname = sysname;
}

public GWTSupplier(String name, String contactID){
	this.name=name;
	this.contactID=contactID;
}

public void setName(String name) {
	this.name = name;
}
public String getName() {
	return name;
}
public void setContactID(String contactID) {
	this.contactID = contactID;
}
public String getContactID() {
	return contactID;
}


}
