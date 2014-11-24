package uk.co.platosys.boox.trade;

import java.util.HashMap;
import java.util.Map;

import uk.co.platosys.boox.Body;
import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.util.Logger;
//import uk.co.platosys.xuser.Xaddress;
/**
 * CounterParty is the abstract superclass for Customers and Suppliers. Each of these has its own Ledger and Account.
 * 
 * 
 * 
 * @author edward
 *
 */
public abstract class CounterParty extends Body {
	public static Logger logger = Logger.getLogger(Boox.APPLICATION_NAME);
	
    public abstract Ledger getLedger();
    public abstract Account getAccount();
    public abstract String getName();
}
