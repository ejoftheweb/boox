package uk.co.platosys.platax.client.forms.lists;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.Label;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.services.CustomerService;
import uk.co.platosys.platax.client.services.CustomerServiceAsync;
import uk.co.platosys.platax.client.widgets.labels.ColumnHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyColumnHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyLabel;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;

public class CustomerList extends LTab {
	 final CustomerServiceAsync customerService = (CustomerServiceAsync) GWT.create(CustomerService.class);
	 Platax parent;
	public CustomerList( int list_selection_type) {
		super( list_selection_type);
		this.parent=parent;
		 setTitle("List of Customers");
		 setSubTitle("blah blah");
		 final GWTEnterprise enterprise = Platax.getEnterprise();
		 customerService.listCustomers(enterprise.getEnterpriseID(), list_selection_type, customerListCallBack);
		 
		 //table headers:
		// table.setWidget(0, 0, new ColumnHeaderLabel(LabelText.LIST_INVOICE_NUMBER_HEADER));
		 table.getFlexCellFormatter().setRowSpan(0,0,2); 
		 table.getFlexCellFormatter().setRowSpan(0,1,2); 
			
		 table.setWidget(0, 1, new ColumnHeaderLabel(LabelText.LIST_CUSTOMER_NAME_HEADER));
		 table.getFlexCellFormatter().setColSpan(0,2,3);
		 table.getFlexCellFormatter().setColSpan(0,3,3);
			
		 table.setWidget(0, 2, new ColumnHeaderLabel(LabelText.LIST_CUSTOMER_BALANCE_HEADER));
		 table.setWidget(0, 3, new ColumnHeaderLabel(LabelText.LIST_CUSTOMER_SALES_HEADER));
		 //the rowspan buggers up the logical column numbering here!
		 table.setWidget(1, 0, new MoneyColumnHeaderLabel(LabelText.LIST_CUSTOMER_THIS_HEADER));
		 table.setWidget(1, 1, new MoneyColumnHeaderLabel(LabelText.LIST_CUSTOMER_OVERDUE_HEADER));
		 table.setWidget(1, 2, new MoneyColumnHeaderLabel(LabelText.LIST_CUSTOMER_DISPUTED_HEADER));
		 table.setWidget(1, 3, new MoneyColumnHeaderLabel(LabelText.LIST_CUSTOMER_LAST_HEADER));
		 table.setWidget(1, 4, new MoneyColumnHeaderLabel(LabelText.LIST_CUSTOMER_THIS_HEADER));
		 table.setWidget(1, 5, new MoneyColumnHeaderLabel(LabelText.LIST_CUSTOMER_CHANGE_HEADER));
		 
	}
	
	
	final AsyncCallback<ArrayList<GWTCustomer>> customerListCallBack= new AsyncCallback<ArrayList<GWTCustomer>>(){

		@Override
		public void onSuccess(ArrayList<GWTCustomer> customers){

		Iterator<GWTCustomer> gwit = customers.iterator();
		 int row = 2;
		 while(gwit.hasNext()){
			 GWTCustomer customer = gwit.next();
			 //note that the column numbering is 2 greater than the headers
			 
			// table.setWidget(row, 0, new InvoiceRefHTML(gwinvoice));
			 table.setWidget(row, 1, new Label(customer.getName()));//, parent));
			table.setWidget(row, 2, new MoneyLabel(customer.getBalance()));
			 table.setWidget(row, 3, new MoneyLabel(customer.getOverdueBalance()));
			table.setWidget(row, 4, new MoneyLabel(customer.getDisputedBalance()));
			//table.setWidget(row, 5, new Label(customer.getPreviousSales()));
			 table.setWidget(row, 6, new MoneyLabel(customer.getSales()));
			//table.setWidget(row, 7, new PercentChangeLabel(customer.getSalesChange()));
			 row++;
		 }
		}
		
		@Override
		public void onFailure(Throwable cause) {
			 //Debugging code
			
			StackTraceElement[] st = cause.getStackTrace();
		   String error = "get customer list failed\n";
		  
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
