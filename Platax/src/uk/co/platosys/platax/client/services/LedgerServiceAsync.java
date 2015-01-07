package uk.co.platosys.platax.client.services;

import java.util.ArrayList;

import uk.co.platosys.platax.shared.boox.GWTAuditLine;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTLedger;
import uk.co.platosys.pws.values.GWTMoney;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LedgerServiceAsync {

	void audit(String enterpriseID, String ledgername, AsyncCallback<ArrayList<GWTAuditLine>> callback);

	void readBalance(String enterpriseID, String ledgername, AsyncCallback<GWTMoney> callback);
	public void getGeneralLedger(GWTEnterprise enterprise, AsyncCallback<GWTLedger> callback);
	   public void getLedger(GWTEnterprise enterprise, String ledgername, AsyncCallback<GWTLedger> callback);
}

