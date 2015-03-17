package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;
import java.util.Date;

import uk.co.platosys.pws.values.GWTMoney;
import uk.co.platosys.pws.values.PWSAddress;
import uk.co.platosys.pws.values.ValuePair;
/**
 * Class to wrap employee details.
 * 
 * @author edward
 *
 */
public class GWTBank  implements Serializable , ValuePair, GWTHasAccount {
	
	
	private static final long serialVersionUID = 1L;
	String name;
	String accholder;
	String accno;
	String sortcode;
	String iban;
	PWSAddress address;
	GWTMoney balance;
	GWTMoney highlimit;
	GWTMoney lowlimit=new GWTMoney();
	GWTAccount account;
	String sysname;
	
	public GWTBank(){}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccholder() {
		return accholder;
	}

	public void setAccholder(String accholder) {
		this.accholder = accholder;
	}

	public String getAccno() {
		return accno;
	}

	public void setAccno(String accno) {
		this.accno = accno;
	}

	public String getSortcode() {
		return sortcode;
	}

	public void setSortcode(String sortcode) {
		this.sortcode = sortcode;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public PWSAddress getAddress() {
		return address;
	}

	public void setAddress(PWSAddress address) {
		this.address = address;
	}

	public GWTMoney getBalance() {
		return balance;
	}

	public void setBalance(GWTMoney balance) {
		this.balance = balance;
	}

	public GWTAccount getAccount() {
		return account;
	}

	public void setAccount(GWTAccount account) {
		this.account = account;
	}

	public String getSysname() {
		return sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

	public GWTMoney getHighlimit() {
		return highlimit;
	}

	public void setHighlimit(GWTMoney highlimit) {
		this.highlimit = highlimit;
	}

	public GWTMoney getLowlimit() {
		return lowlimit;
	}

	public void setLowlimit(GWTMoney lowlimit) {
		this.lowlimit = lowlimit;
	}

	@Override
	public String getLabel() {
		
		return name;
	}

	@Override
	public void setLabel(String label) {
		this.name=label;
		
	}

	@Override
	public String getValue() {
		
		return sysname;
	}

}
