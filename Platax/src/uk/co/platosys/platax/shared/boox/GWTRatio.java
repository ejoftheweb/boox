package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

import uk.co.platosys.pws.values.GWTMoney;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GWTRatio implements Serializable,  IsSerializable {
   private String name;
   private GWTMoney value;
   private double percentValue;
   private boolean percent=false;
   private double percentTarget;
   private String info;
   private GWTMoney target;
   
   /**A ratio consists of three, and optionally four, fields:
    * name and info, which are respectivel short and more discursive labels; 
    * plus value - the value of the ratio, and optionally target (what we would like the ratio to be, so we can highlight variances).
    * Value and Target can be set as either money or percentages.
    * the boolean percent is 
    */
	public GWTRatio(){
	   
   }
	public GWTRatio(String name, GWTMoney value, String info){
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
	public void setValue(GWTMoney value) {
		this.value = value;
	}
	public GWTMoney getValue() {
		return value;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getInfo() {
		return info;
	}
	public void setTarget(GWTMoney target) {
		this.target = target;
	}
	public GWTMoney getTarget() {
		return target;
	}
}
