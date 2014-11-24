package uk.co.platosys.platax.client.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTInvoice;
import uk.co.platosys.platax.shared.boox.GWTLineItem;
import uk.co.platosys.platax.shared.boox.GWTMoney;
import uk.co.platosys.platax.shared.boox.GWTInvoiceList;



import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("invoice")
public interface InvoiceService extends RemoteService {
   public GWTInvoice createInvoice(String enterpriseID, String customerID, Date date);
   public GWTLineItem postLine(GWTLineItem line);
   public GWTLineItem voidLine(GWTLineItem line);
   public GWTInvoice raiseInvoice(GWTInvoice invoice);
   public GWTInvoiceList listInvoices(String enterpriseID, int selection);
}
