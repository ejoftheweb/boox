package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

import uk.co.platosys.pws.values.ValuePair;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GWTContact implements ValuePair, Serializable, IsSerializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected String name;
protected String sysname;
protected String legalName;
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

public String getLegalName() {
	return legalName;
}

public void setLegalName(String legalName) {
	this.legalName = legalName;
}

@Override
public String getLabel() {
	
	return getName();
}

@Override
public void setLabel(String label) {
}

@Override
public String getValue() {
	return getSysname();
}


}
