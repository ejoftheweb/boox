package uk.co.platosys.platax.client.services;
import java.util.ArrayList;
import java.util.Date;
import com.google.gwt.user.client.rpc.AsyncCallback;

import uk.co.platosys.platax.shared.boox.GWTInvoice;
import uk.co.platosys.platax.shared.boox.GWTLineItem;

public interface InvoiceServiceAsync {
	void createInvoice(String enterpriseID, String customerID, Date date,  AsyncCallback<GWTInvoice> callback);
	void postLine(GWTLineItem line, AsyncCallback<GWTLineItem> callback);
     void voidLine(GWTLineItem line, AsyncCallback<GWTLineItem> callback);
     void raiseInvoice(GWTInvoice invoice, AsyncCallback<GWTInvoice> invoiceCallBack);
     void deleteInvoice(GWTInvoice invoice, AsyncCallback<GWTInvoice> invoiceCallBack);
     void saveInvoice(GWTInvoice invoice, AsyncCallback<GWTInvoice> invoiceCallBack);
     void listInvoices(String enterpriseID, int selection, AsyncCallback<ArrayList<GWTInvoice>> listCallback);
}   
