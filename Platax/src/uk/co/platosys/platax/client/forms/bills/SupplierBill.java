package uk.co.platosys.platax.client.forms.bills;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.forms.popups.AddCustomerPopupForm;
import uk.co.platosys.platax.client.forms.popups.AddProductPopupForm;
import uk.co.platosys.platax.client.services.CustomerService;
import uk.co.platosys.platax.client.services.CustomerServiceAsync;
import uk.co.platosys.platax.client.services.InvoiceService;
import uk.co.platosys.platax.client.services.InvoiceServiceAsync;
import uk.co.platosys.platax.client.widgets.QuantityBox;
import uk.co.platosys.platax.client.widgets.buttons.LineCancelButton;
import uk.co.platosys.platax.client.widgets.labels.ColumnHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyColumnHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyLabel;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTBill;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTInvoice;
import uk.co.platosys.platax.shared.boox.GWTLineItem;


public class SupplierBill extends BTab {
	private GWTCustomer gwtCustomer=null;
	private GWTInvoice gwtInvoice=null;
	Button newProductButton;
    final InvoiceServiceAsync invoiceService = (InvoiceServiceAsync) GWT.create(InvoiceService.class);
	String invoiceSysname;
	
	String customerName;
	
	public SupplierBill() {
		super();
		this.gwtEnterprise=gwtEnterprise;
		this.enterpriseName=gwtEnterprise.getName();
		this.enterpriseID=gwtEnterprise.getEnterpriseID();
		this.setTabHeaderText(gwtEnterprise.getName()+":"+ LabelText.INVOICE);
		table.setWidget(1, 0, new Label(LabelText.CUSTOMER));
		table.setWidget(0,0, new Label(LabelText.INVOICE));
		table.setWidget(0,4, new Label(LabelText.DATE));
		table.setWidget(0,5, dateLabel);
		contactListBox.addContacts(gwtEnterprise.getCustomers());
		//itemListBox.addItems(gwtEnterprise.getProducts());
		table.setWidget(1, 1, contactListBox);
		Button newCustomerButton = new Button(ButtonText.ADD_NEW);
		newProductButton = new Button(ButtonText.ADD_NEW);
		table.setWidget(1,2, newCustomerButton);
		newCustomerButton.addClickHandler(new ClickHandler(){
		    @Override
			public void onClick(ClickEvent event) {
				AddCustomerPopupForm acf = new AddCustomerPopupForm(SupplierBill.this, gwtEnterprise);
				acf.setPopupPositionAndShow(acf.poscall);
			}
		});
		newProductButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
					AddProductPopupForm apf = new AddProductPopupForm(SupplierBill.this, customerName, gwtEnterprise);
					apf.setPopupPositionAndShow(apf.poscall);
				
			}
		});
		contactListBox.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event){
				String customerSysname=contactListBox.getValue(contactListBox.getSelectedIndex());
				invoiceService.createInvoice(enterpriseID, customerSysname,new Date(),  createInvoiceCallback);
				table.setWidget(1, 2, null);
				table.setWidget(2,1, new ColumnHeaderLabel(LabelText.ITEM_NAME_HEADER));
				table.setWidget(2,2, new ColumnHeaderLabel(LabelText.ITEM_QTY_HEADER));
				table.setWidget(2,3, new MoneyColumnHeaderLabel(LabelText.ITEM_PRICE_HEADER));
				table.setWidget(2,4, new MoneyColumnHeaderLabel(LabelText.ITEM_NET_HEADER));
				table.setWidget(2,5, new MoneyColumnHeaderLabel(LabelText.ITEM_TAX_HEADER));
				table.setWidget(2,6, new MoneyColumnHeaderLabel(LabelText.ITEM_GROSS_HEADER));
				addLine();
			}
		});
		itemListBox.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				String itemName=itemListBox.getItemText(itemListBox.getSelectedIndex());
				if (itemName.equals(StringText.ADD_NEW)){
					AddProductPopupForm apf = new AddProductPopupForm(SupplierBill.this, customerName, gwtEnterprise);
					apf.setPopupPositionAndShow(apf.poscall);
				}else{
					qtyBox.setEnabled(true);
				}
			}
		});
		qtyBox.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent event) {
				postButton.setEnabled(true);
			}
		});
		postButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				postLine();
			}
		});
	}

	protected void setInvoice(GWTInvoice gwtInvoice) {
		this.gwtInvoice=gwtInvoice;
		this. itemListBox.addItems(gwtInvoice.getProducts());
		//this.invoiceSysname=gwtInvoice.getSysname();
		this.gwtCustomer=gwtInvoice.getCustomer();
		this.customerName=gwtCustomer.getName();
		setTabHeaderText(enterpriseName+":"+customerName);
		table.setWidget(1,1, new Label(customerName));
	}

	protected void addLine() {
		int rows = table.getRowCount();
		table.setWidget(rows, 0, newProductButton);
		table.setWidget(rows, 1, itemListBox);
		//qtyBox.setEnabled(false);
		table.setWidget(rows, 2, qtyBox);
		//postButton.setEnabled(false);
		table.setWidget(rows, 7, postButton);
			
	}
    protected void postLine(){
    	final int rows = table.getRowCount()-1;
    	final GWTLineItem gwtLineItem = new GWTLineItem();
    	gwtLineItem.setEnterprise(gwtEnterprise);
    	gwtLineItem.setCustomer(gwtCustomer);
    	gwtLineItem.setInvoiceSysname(gwtInvoice.getSysname());
    	gwtLineItem.setLineNumber(rows);
    	gwtLineItem.setItemSysname(itemListBox.getValue(itemListBox.getSelectedIndex()));
    	try {
			gwtLineItem.setItemQty(qtyBox.getQuantity());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//Logger.log("exception thrown", e);
		}
    	invoiceService.postLine(gwtLineItem, postLineCallback);
    	table.setWidget(rows, 1, new Label(itemListBox.getItemText(itemListBox.getSelectedIndex())));
		table.setWidget(rows, 2, new Label(qtyBox.getText()));
		final Button voidLineButton = new LineCancelButton();
		voidLineButton.addClickHandler(new ClickHandler(){
           @Override
			public void onClick(ClickEvent event) {
				invoiceService.voidLine(gwtLineItem, voidLineCallback);
			}
		});
		table.setWidget(rows, 7,voidLineButton);
		addLine();
    }
    final AsyncCallback<GWTInvoice> createInvoiceCallback = new AsyncCallback<GWTInvoice>(){
    	@Override
		public void onSuccess(GWTInvoice result) {
			setInvoice(result);
		}
  		@Override
		public void onFailure(Throwable cause) {
			 //Debugging code
			StackTraceElement[] st = cause.getStackTrace();
		   String error = "addCustomer failed\n";
		   error = error+cause.getClass().getName()+"\n";
		   for (int i=0; i<st.length; i++){
			   error = error + st[i].toString()+ "\n";
		   }
			Window.alert(error);
		}
 };
    final AsyncCallback<GWTLineItem> postLineCallback = new AsyncCallback<GWTLineItem>(){
    	@Override
		public void onSuccess(GWTLineItem result) {
    		int row = result.getLineNumber();
    		table.setWidget(row,1, new Label(result.getItemName()));
			table.setWidget(row,2, new Label(Float.toString(result.getItemQty())));
			table.setWidget(row,3, new MoneyLabel(result.getPrice()));
			table.setWidget(row,4, new MoneyLabel(result.getNet()));
			table.setWidget(row,5, new MoneyLabel(result.getTax()));
			table.setWidget(row,6, new MoneyLabel(result.getGross()));
			
		}
		@Override
		public void onFailure(Throwable cause) {
			 //Debugging code
			StackTraceElement[] st = cause.getStackTrace();
		   String error = "addCustomer failed\n";
		   error = error+cause.getClass().getName()+"\n";
		   for (int i=0; i<st.length; i++){
			   error = error + st[i].toString()+ "\n";
		   }
			Window.alert(error);
		}
    };
    final AsyncCallback<GWTLineItem> voidLineCallback = new AsyncCallback<GWTLineItem>(){
    	@Override
		public void onSuccess(GWTLineItem result) {
    		int row = result.getLineNumber();
    		table.setWidget(row,1, new Label(result.getItemName()));
			table.setWidget(row,2, new Label(Float.toString(result.getItemQty())));
			table.setWidget(row,3, new MoneyLabel(result.getPrice()));
			table.setWidget(row,4, new MoneyLabel(result.getNet()));
			table.setWidget(row,5, new MoneyLabel(result.getTax()));
			table.setWidget(row,6, new MoneyLabel(result.getGross()));
		}
		@Override
		public void onFailure(Throwable cause) {
			 //Debugging code
			StackTraceElement[] st = cause.getStackTrace();
		   String error = "addCustomer failed\n";
		   error = error+cause.getClass().getName()+"\n";
		   for (int i=0; i<st.length; i++){
			   error = error + st[i].toString()+ "\n";
		   }
			Window.alert(error);
		}
    };

	@Override
	public GWTBill getGWTBill() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
}
