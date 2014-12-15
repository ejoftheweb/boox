package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;
import java.util.Map;

/**
 * Class to wrap details about a cash account, particularly cash registers, but also for 
 * petty cash tins.
 * 
 * @author edward
 *
 */
public class GWTCash implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GWTMoney balance;
	GWTMoney floatbal;
	GWTMoney paidouts;
	GWTMoney runningTotal;
	String name;
	int seqno;
	Map<String, GWTMoney> depts;
	
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

	public Map<String, GWTMoney> getDepts() {
		return depts;
	}

	public void setDepts(Map<String, GWTMoney> depts) {
		this.depts = depts;
	}


}
