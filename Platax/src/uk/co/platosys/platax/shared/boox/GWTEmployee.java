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
public class GWTEmployee extends GWTPerson implements Serializable, GWTHasAccount {
	
	
	private static final long serialVersionUID = 1L;
	String natinsno=null;
	String utr=null;
	Date startDate;
	Date dob;
	String taxCode=null;
	String payFreq;
	String canWork;
	GWTMoney payRate;
	String sysname;
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

	public String getPayFreq() {
		return payFreq;
	}

	public void setPayFreq(String payFreq) {
		this.payFreq = payFreq;
	}

	public String getCanWork() {
		return canWork;
	}

	public void setCanWork(String canWork) {
		this.canWork = canWork;
	}

	public GWTMoney getPayRate() {
		return payRate;
	}

	public void setPayRate(GWTMoney payRate) {
		this.payRate = payRate;
	}

	public float getHours() {
		return hours;
	}

	public void setHours(float hours) {
		this.hours = hours;
	}

	public String getSysname() {
		return sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
}
