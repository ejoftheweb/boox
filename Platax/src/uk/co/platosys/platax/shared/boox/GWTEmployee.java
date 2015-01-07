package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;
import java.util.Date;

import uk.co.platosys.pws.values.GWTMoney;
/**
 * Class to wrap employee details.
 * 
 * @author edward
 *
 */
public class GWTEmployee extends GWTPerson implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	String natinsno=null;
	String utr=null;
	Date startDate;
	Date dob;
	String taxCode=null;
	GWTMoney payRate;
	float hours;
	
	public GWTEmployee(){}

	public String getNatinsno() {
		return natinsno;
	}

	public void setNatinsno(String natinsno) {
		this.natinsno = natinsno;
	}

	public String getUtr() {
		return utr;
	}

	public void setUtr(String utr) {
		this.utr = utr;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
}
