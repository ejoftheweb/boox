package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GWTContact implements Serializable, IsSerializable{
protected String name;
protected String sysname;
public GWTContact(){}

public GWTContact(String name, String sysname){
	this.name=name;
	this.sysname=sysname;
}

public void setName(String name) {
	this.name = name;
}
public String getName() {
	return name;
}
public void setSysname(String sysname) {
	this.sysname = sysname;
}
public String getSysname() {
	return sysname;
}


}
