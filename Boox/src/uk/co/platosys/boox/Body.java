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
	public static final String BODY_ID="Body_ID";
	
	private String name;
	private String legalName;
	private String sysname;
	private String databaseName="none";
	private boolean isTrade;
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
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLegalName() {
		return legalName;
	}
	protected void setLegalName(String legalName) {
		this.legalName = legalName;
	}
	public String getSysname() {
		return sysname;
	}
	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
	public boolean isTrade() {
		return isTrade;
	}
	public void setTrade(boolean isTrade) {
		this.isTrade = isTrade;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
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
