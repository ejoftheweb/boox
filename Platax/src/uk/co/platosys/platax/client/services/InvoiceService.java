package uk.co.platosys.platax.client.services;

import java.util.ArrayList;
import java.util.Date;
import uk.co.platosys.platax.shared.boox.GWTInvoice;
import uk.co.platosys.platax.shared.boox.GWTLineItem;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("invoiceService")
public interface InvoiceService extends RemoteService {
   public GWTInvoice createInvoice(String enterpriseID, String customerID, Date date);
   public GWTLineItem postLine(GWTLineItem line);
   public GWTLineItem voidLine(GWTLineItem line);
   public GWTInvoice raiseInvoice(GWTInvoice invoice);
   public GWTInvoice deleteInvoice(GWTInvoice invoice);
   public GWTInvoice saveInvoice(GWTInvoice invoice);
   public ArrayList<GWTInvoice> listInvoices(String enterpriseID, int selection);
}
