package uk.co.platosys.platax.shared;
/**
 * PXAddress is a serializable version of the serverside Xaddress. 
 */

import java.io.Serializable;
import com.google.gwt.user.client.rpc.IsSerializable;

public class PXAddress implements Serializable, IsSerializable {
 
  public static final String[] FIELD_NAMES = {"building","street","district","town","county","postcode"};
	private String[] fieldValues=new String[FIELD_NAMES.length];
	private String xaddressid;
  
  public PXAddress(){}
  public PXAddress(String xaddressid, String[] fieldvals){
	  this.xaddressid=xaddressid;
	this.fieldValues=fieldvals;  
  }
  /**
	 * Returns the address as a string.
	 * @param oneline if true, it will be formatted on one line, otherwise with natural linebreaks.
	 * @return
	 * @throws Exception
	 */
	public String getAddress(boolean oneline) {
		String address="";
		try{
		for(int i=0; i<fieldValues.length; i++){
		    String val=fieldValues[i];
		    if(!val.equals("")||val==null){
		    	address=address+val;
		        if(oneline){
		        	address=address+", ";
		        }else{
		        	address=address+"\n";
		        }
		    }
		}
		return address;
		}catch(Exception e){
			return("Address Error: "+e.getMessage());
		}
	}
	public void setAddress(String xaddressid, String[] vals){
		this.xaddressid=xaddressid;
		this.fieldValues=vals;
	}
}
