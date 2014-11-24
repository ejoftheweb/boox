package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GWTRole implements Serializable, IsSerializable {
	private String name;
	 private String localisedName;
	 public GWTRole(){}
	 public GWTRole(String name, String localisedName){
		 this.name=name;
		 this.localisedName=name;
	 }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocalisedName() {
		return localisedName;
	}
	public void setLocalisedName(String localisedName) {
		this.localisedName = localisedName;
	}

}
