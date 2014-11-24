package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

/**
 * This class encapsulates bank details
 * At present it just encapsulates the regular 4 UK details (bankname, accountname, accountno, sortcode);
 * 
 * We will make it encapsulate some read-only credentials and sort the security out for using Yodlee for bank feeds
 * but that is for Ron. 
 * 
 * @author edward
 *
 */
public class GWTBankDetails implements Serializable {
  private String bankname=null;
  private String accountname=null;
  private String accno=null;
  private String sortcode=null;
  
  public GWTBankDetails(){}

public String getBankname() {
	return bankname;
}

public String getAccountname() {
	return accountname;
}

public String getAccno() {
	return accno;
}

public String getSortcode() {
	return sortcode;
}

public void setBankname(String bankname) {
	this.bankname = bankname;
}

public void setAccountname(String accountname) {
	this.accountname = accountname;
}

public void setAccno(String accno) {
	this.accno = accno;
}

public void setSortcode(String sortcode) {
	this.sortcode = sortcode;
}
  
}
