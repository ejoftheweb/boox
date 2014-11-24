package uk.co.platosys.platax.client.services;

import java.util.ArrayList;

import uk.co.platosys.platax.shared.boox.GWTAuditLine;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTLedger;
import uk.co.platosys.platax.shared.boox.GWTMoney;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ledgerService")

public interface LedgerService extends RemoteService {
   public GWTMoney readBalance(String enterpriseID, String ledgername);
   public ArrayList<GWTAuditLine> audit (String enterpriseID, String ledgername);
   public GWTLedger getGeneralLedger(GWTEnterprise enterprise);
   public GWTLedger getLedger(GWTEnterprise enterprise, String ledgername);
}
