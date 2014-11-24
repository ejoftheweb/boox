package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GWTRatio implements Serializable,  IsSerializable {
   private String name;
   private String value;
   private String info;
   private String target;
	public GWTRatio(){
	   
   }
	public GWTRatio(String name, String value, String info){
		this.name=name;
		this.value=value;
		this.info=info;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getInfo() {
		return info;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getTarget() {
		return target;
	}
}
