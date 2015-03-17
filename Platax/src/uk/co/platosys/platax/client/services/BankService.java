package uk.co.platosys.platax.client.services;

import java.util.List;

import uk.co.platosys.platax.shared.boox.GWTBank;
import uk.co.platosys.platax.shared.boox.GWTHasAccount;
import uk.co.platosys.platax.shared.exceptions.PlataxException;
import uk.co.platosys.pws.values.GWTMoney;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bankService")
public interface BankService extends RemoteService {
	public List<GWTBank> getBanks(String enterpriseSysname);
	public GWTBank getBank(String enterpriseSysname, String bankSysname);
	public GWTBank addBankAccount(GWTBank bank, String enterpriseSysname) throws PlataxException;
	public Long pay  (String enterpriseSysname, String debit,String credit, GWTMoney amount, String note);
}
