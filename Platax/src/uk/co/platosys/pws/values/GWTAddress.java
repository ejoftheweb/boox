package uk.co.platosys.pws.values;

import java.io.Serializable;

/**
 * Class to wrap a geographical address
 * @author edward
 *
 */
public class GWTAddress implements Serializable {
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
	public GWTAddress(){}


	
	
		
	
	public String getShortAddress(){
		return building+" "+street+", "+postcode;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
