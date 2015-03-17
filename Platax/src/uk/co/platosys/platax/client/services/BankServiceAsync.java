package uk.co.platosys.platax.client.services;

import java.util.List;

import uk.co.platosys.platax.shared.boox.GWTBank;
import uk.co.platosys.platax.shared.boox.GWTHasAccount;
import uk.co.platosys.platax.shared.exceptions.PlataxException;
import uk.co.platosys.pws.values.GWTMoney;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface BankServiceAsync {
	public void getBanks(String enterpriseSysname, AsyncCallback<List<GWTBank>> callback);
	public void getBank(String enterpriseSysname, String bankID, AsyncCallback<GWTBank> callback);
	public void addBankAccount(GWTBank register, String enterpriseSysname, AsyncCallback<GWTBank> callback)throws PlataxException;
	public void pay  (String enterpriseSysname, String debit, String credit, GWTMoney amount, String note, AsyncCallback<Long> callback);
}
