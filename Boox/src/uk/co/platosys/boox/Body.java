package uk.co.platosys.boox;


import java.util.HashMap;
import java.util.Map;
/*
import uk.co.platosys.xuser.Xaddress;
import uk.co.platosys.xuser.XaddressType;
import uk.co.platosys.xuser.Xbizinfo;*/

public abstract class Body {
	public static final String NAME="Name";
	public static final String LEGAL_NAME="Legal Name";
	//private Map<String, Xaddress> addresses=new HashMap<String, Xaddress>();
	//private Xbizinfo xbizinfo;
	private Map<String, String> info = new HashMap<String,String>();
	
	public Map<String, String> getInfo(){
		return info;
	}
    public void putInfo(String key, String value){
    	info.put(key, value);
    }
    public String getInfo(String key){
    	//TODO trap the not there problem
    	return info.get(key);
    }
	public Body() {
		// TODO Auto-generated constructor stub
	}
	/*public void addXaddress(String key, Xaddress address){
		addresses.put(key,address); 
	}
	public Xaddress getXaddress(String key){
		return addresses.get(key);
	}
	public Map<String, Xaddress> getAddresses() {
		return addresses;
	}
	public void setAddresses(Map<String, Xaddress> addresses) {
		this.addresses = addresses;
	}
	public Xbizinfo getXbizinfo() {
		return xbizinfo;
	}
	public void setXbizinfo(Xbizinfo xbizinfo) {
		this.xbizinfo = xbizinfo;
	}*/
}
