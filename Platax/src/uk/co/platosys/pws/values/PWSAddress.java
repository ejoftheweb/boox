package uk.co.platosys.pws.values;

import java.io.Serializable;

/**
 * Class to wrap a geographical address
 * @author edward
 *
 */
public class PWSAddress implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	    private String building="";
	   private String street=""; 
	   private String district=""; 
	   private String town="";  
	   private String postcode=""; 
	   private String county=""; 
	   private String country=""; 
	   private String xAddressID="";
	   public static final String[] FIELD_NAMES = {"building","street","district","town","postcode","county","country"};
		  private String[] vals = {building, street, district, town, postcode, county, country};
	public PWSAddress(){}

    public PWSAddress(String xAddressID, String[] xAddressValues) throws Exception{
    	if(xAddressValues.length==7){
    	this.xAddressID=xAddressID;
    	this.building=xAddressValues[0];
    	this.street=xAddressValues[1];
    	this.district=xAddressValues[2];
    	this.town=xAddressValues[3];
    	this.postcode=xAddressValues[4];
    	this.county=xAddressValues[5];
    	this.country=xAddressValues[6];}
    	else{
    		throw new Exception("Xaddress value array wrong size");
    	}
    }
	
	
		
	

	public String getShortAddress(){
		return building+" "+street+", "+postcode;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
		vals[0]=building;
	}

	public String getStreet() {
		return street;
		
	}

	public void setStreet(String street) {
		vals[1]=street;
		this.street = street;
	}

	public String getDistrict() {
		 return district;
	}

	public void setDistrict(String district) {
		vals[2]=district;
		this.district = district;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		vals[3]=town;
		this.town = town;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		vals[4]=postcode;
		this.postcode = postcode;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		vals[5]=county;
		this.county = county;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		vals[6]=country;
		this.country = country;
	}

	/**
	 * @return the vals*/
	
	public String[] getVals() {
		return vals;
	}

	/**
	 * @param vals the vals to set*/
	
	public void setVals(String[] vals) {
		this.vals = vals;
	}

	/**
	 * @return the xAddressID*/
	
	public String getxAddressID() {
		return xAddressID;
	}

	/**
	 * @param xAddressID the xAddressID to set*/
	
	public void setxAddressID(String xAddressID) {
		this.xAddressID = xAddressID;
	}
}
