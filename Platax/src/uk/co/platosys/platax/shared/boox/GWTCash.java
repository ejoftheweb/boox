package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;
import java.util.Map;

/**
 * Class to wrap details about a cash account, particularly cash registers, but also for 
 * petty cash tins.
 * 
 * GWTCash isn't an exact analogue of ..boox.cash.Cash. 
 * 
 * @author edward
 *
 */
public class GWTCash implements Serializable, GWTSelectable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GWTMoney balance;
	GWTMoney floatbal;
	GWTMoney paidouts;
	GWTMoney runningTotal;
	int depts;
	String name;
	String sysname;
	String description;
	String model;
	int seqno;
	Map<String, GWTMoney> deptsMap;
	
	public GWTCash(){}

	public GWTMoney getBalance() {
		return balance;
	}

	public void setBalance(GWTMoney balance) {
		this.balance = balance;
	}

	public GWTMoney getFloatbal() {
		return floatbal;
	}

	public void setFloatbal(GWTMoney floatbal) {
		this.floatbal = floatbal;
	}

	public GWTMoney getPaidouts() {
		return paidouts;
	}

	public void setPaidouts(GWTMoney paidouts) {
		this.paidouts = paidouts;
	}

	public GWTMoney getRunningTotal() {
		return runningTotal;
	}

	public void setRunningTotal(GWTMoney runningTotal) {
		this.runningTotal = runningTotal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSeqno() {
		return seqno;
	}

	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}

	public Map<String, GWTMoney> getDeptsMap() {
		return deptsMap;
	}

	public void setDeptsMap(Map<String, GWTMoney> depts) {
		this.deptsMap = depts;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public boolean isMultiSelect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSysname() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDepts() {
		return depts;
	}

	public void setDepts(int depts) {
		this.depts = depts;
	}


}
