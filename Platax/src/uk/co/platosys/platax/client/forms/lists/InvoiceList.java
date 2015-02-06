package uk.co.platosys.platax.client.forms.lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.Label;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.DateFormats;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.services.InvoiceService;
import uk.co.platosys.platax.client.services.InvoiceServiceAsync;
import uk.co.platosys.platax.client.widgets.PlataxTabPanel;
import uk.co.platosys.platax.client.widgets.html.CustomerHTML;
import uk.co.platosys.platax.client.widgets.html.InvoiceRefHTML;
import uk.co.platosys.platax.client.widgets.labels.ColumnHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyColumnHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyLabel;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTInvoice;
import uk.co.platosys.platax.shared.boox.GWTInvoiceList;

public class InvoiceList extends AbstractList {
	 final InvoiceServiceAsync invoiceService = (InvoiceServiceAsync) GWT.create(InvoiceService.class);
		
	public InvoiceList(Platax parent, GWTEnterprise gwtEnterprise, int list_selection_type) {
		super(parent, gwtEnterprise.getName()+":Invoices", list_selection_type);
		setTitle("List of Invoices");
		 setSubTitle("blah blah");
		 invoiceService.listInvoices(gwtEnterprise.getEnterpriseID(), list_selection_type, invoiceListCallBack);
		 
		 //table headers:
		 table.setWidget(0, 0, new ColumnHeaderLabel(LabelText.LIST_INVOICE_NUMBER_HEADER));
		 table.setWidget(0, 1, new ColumnHeaderLabel(LabelText.LIST_INVOICE_CUSTOMER_HEADER));
		 table.setWidget(0, 2, new ColumnHeaderLabel(LabelText.LIST_INVOICE_VALUE_DATE_HEADER));
		 table.setWidget(0, 3, new ColumnHeaderLabel(LabelText.LIST_INVOICE_DUE_DATE_HEADER));
		 table.setWidget(0, 4, new ColumnHeaderLabel(LabelText.LIST_INVOICE_STATUS_HEADER));
		 table.setWidget(0, 5, new MoneyColumnHeaderLabel(LabelText.LIST_INVOICE_NET_HEADER));
		 table.setWidget(0, 6, new MoneyColumnHeaderLabel(LabelText.LIST_INVOICE_TAX_HEADER));
		 table.setWidget(0, 7, new MoneyColumnHeaderLabel(LabelText.LIST_INVOICE_TOTAL_HEADER));
		 
	
	}
	
	final AsyncCallback<ArrayList<GWTInvoice>> invoiceListCallBack= new AsyncCallback<ArrayList<GWTInvoice>>(){
		@Override
		public void onSuccess(ArrayList<GWTInvoice> invoices){
		Iterator<GWTInvoice> gwit = invoices.iterator();
		 int row = 1;
		 while(gwit.hasNext()){
			 GWTInvoice gwinvoice = gwit.next();
			 table.setWidget(row, 0, new InvoiceRefHTML(gwinvoice));
			 table.setWidget(row, 1, new Label(gwinvoice.getCustomer().getName()));
			 table.setWidget(row, 2, new Label(DateFormats.SHORT_DATE_FORMAT.format(gwinvoice.getValueDate())));
			 table.setWidget(row, 3, new Label(DateFormats.SHORT_DATE_FORMAT.format(gwinvoice.getDueDate())));
			 table.setWidget(row, 4, new Label(gwinvoice.getStatus()));
			 table.setWidget(row, 5, new MoneyLabel(gwinvoice.getNet()));
			 table.setWidget(row, 6, new MoneyLabel(gwinvoice.getTax()));
			 table.setWidget(row, 7, new MoneyLabel(gwinvoice.getGross()));
			 row++;
		 }
		}
		
		@Override
		public void onFailure(Throwable cause) {
			 //Debugging code
			
			StackTraceElement[] st = cause.getStackTrace();
		   String error = "get invoice list failed\n";
		  
		   error = error+cause.getClass().getName()+"\n";
		   if (cause instanceof StatusCodeException){
			    StatusCodeException sce=(StatusCodeException) cause;
			    int sc = sce.getStatusCode();
				error=error+"Status Code:"+ sc+"\n";
			}
		   for (int i=0; i<st.length; i++){
			   error = error + st[i].toString()+ "\n";
		   }
			Window.alert(error);
		}
	};

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
}

