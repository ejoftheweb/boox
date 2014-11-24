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
import com.google.gwt.user.client.ui.FlowPanel;
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
import uk.co.platosys.platax.client.widgets.labels.FormHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyColumnHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyLabel;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTBill;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTInvoice;
import uk.co.platosys.platax.shared.boox.GWTLineItem;
import uk.co.platosys.util.ISODate;

public class InvoiceForm extends AbstractBill {
	private GWTCustomer gwtCustomer=null;
	private GWTInvoice gwtInvoice=null;
	Button newProductButton;
    final InvoiceServiceAsync invoiceService = (InvoiceServiceAsync) GWT.create(InvoiceService.class);
	String invoiceSysname;
	
	String customerName;
	
	public InvoiceForm(Platax parent, final GWTEnterprise gwtEnterprise) {
		super(parent, gwtEnterprise.getName()+":"+StringText.INVOICE);
		this.gwtEnterprise=gwtEnterprise;
		this.enterpriseName=gwtEnterprise.getName();
		this.enterpriseID=gwtEnterprise.getEnterpriseID();
		this.setTabHeaderText(gwtEnterprise.getName()+":"+ LabelText.INVOICE);
		
		//hpanel.add( new Label(LabelText.CUSTOMER));
		headPanel.add(new FormHeaderLabel(LabelText.INVOICE));
		headPanel.add( new Label(LabelText.DATE));
		headPanel.add( dateBox);
		//form.add(hpanel0);
		
		//hpanel.add( new Label(LabelText.CUSTOMER));
		cpartyPanel.add( new Label(LabelText.CUSTOMER));
		//hpanel1.add( dateBox);
		contactListBox.addContacts(gwtEnterprise.getCustomers());
		cpartyPanel.add( contactListBox);
		
		//itemListBox.addItems(gwtEnterprise.getProducts());
		//table.setWidget(1, 1, contactListBox);
		Button newCustomerButton = new Button(ButtonText.ADD_NEW);
		newProductButton = new Button(ButtonText.ADD_NEW);
		cpartyPanel.add(newCustomerButton);
		//form.add(hpanel1);
		
		newCustomerButton.addClickHandler(new ClickHandler(){
		    @Override
			public void onClick(ClickEvent event) {
				AddCustomerPopupForm acf = new AddCustomerPopupForm(InvoiceForm.this, gwtEnterprise);
				acf.setPopupPositionAndShow(acf.poscall);
			}
		});
		newProductButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
					AddProductPopupForm apf = new AddProductPopupForm(InvoiceForm.this, customerName, gwtEnterprise);
					apf.setPopupPositionAndShow(apf.poscall);
			}
		});
		contactListBox.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event){
				String customerSysname=contactListBox.getValue(contactListBox.getSelectedIndex());
				invoiceService.createInvoice(enterpriseID, customerSysname,new Date(),  createInvoiceCallback);
				table.setWidget(0,0, new ColumnHeaderLabel(LabelText.ITEM_LINENO_HEADER));
				table.setWidget(0,1, new ColumnHeaderLabel(LabelText.ITEM_NAME_HEADER));
				table.setWidget(0,2, new ColumnHeaderLabel(LabelText.ITEM_QTY_HEADER));
				table.setWidget(0,3, new MoneyColumnHeaderLabel(LabelText.ITEM_PRICE_HEADER));
				table.setWidget(0,4, new MoneyColumnHeaderLabel(LabelText.ITEM_NET_HEADER));
				table.setWidget(0,5, new MoneyColumnHeaderLabel(LabelText.ITEM_TAX_HEADER));
				table.setWidget(0,6, new MoneyColumnHeaderLabel(LabelText.ITEM_GROSS_HEADER));
				lineEntryPanel.add(new Label(LabelText.ENTER_INVOICE_ITEM));
				lineEntryPanel.add(itemListBox);
				lineEntryPanel.add(newProductButton);
				lineEntryPanel.add(new Label(LabelText.ITEM_QTY_HEADER));
				lineEntryPanel.add(qtyBox);
				lineEntryPanel.add(postButton);
			}
		});
		itemListBox.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				String itemName=itemListBox.getItemText(itemListBox.getSelectedIndex());
				if (itemName.equals(StringText.ADD_NEW)){
					AddProductPopupForm apf = new AddProductPopupForm(InvoiceForm.this, customerName, gwtEnterprise);
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
				try {
					postLine();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		submitButtonPanel.add(new Button(ButtonText.RAISE_INVOICE));
	}

	protected void setInvoice(GWTInvoice gwtInvoice) {
		this.gwtInvoice=gwtInvoice;
		this. itemListBox.addItems(gwtInvoice.getProducts());
		//this.invoiceSysname=gwtInvoice.getSysname();
		this.gwtCustomer=gwtInvoice.getCustomer();
		this.customerName=gwtCustomer.getName();
		int index= cpartyPanel.getWidgetIndex(contactListBox);
		cpartyPanel.remove(index);
		cpartyPanel.insert(new Label(customerName), index);
		setTabHeaderText(enterpriseName+":"+customerName);
		//table.setWidget(1,1, new Label(customerName));
	}

	
    protected void postLine() throws Exception{
    	final int rows = table.getRowCount();
    	final GWTLineItem gwtLineItem = new GWTLineItem();
    	gwtLineItem.setEnterprise(gwtEnterprise);
    	gwtLineItem.setCustomer(gwtCustomer);
    	gwtLineItem.setSIN(gwtInvoice.getSysname());
    	//gwtLineItem.setLineNumber(rows);
    	gwtLineItem.setItemSysname(itemListBox.getValue(itemListBox.getSelectedIndex()));
    	gwtLineItem.setItemQty(qtyBox.getQuantity());
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
    		table.setWidget(row, 0, new Label(Integer.toString(result.getLineNumber())));
    		table.setWidget(row,1, new Label(result.getItemName()));
			table.setWidget(row,2, new Label(Float.toString(result.getItemQty())));
			table.setWidget(row,3, new MoneyLabel(result.getPrice()));
			table.setWidget(row,4, new MoneyLabel(result.getNet()));
			table.setWidget(row,5, new MoneyLabel(result.getTax()));
			table.setWidget(row,6, new MoneyLabel(result.getGross()));
			try{
				netMoney = netMoney.add(result.getNet());
				taxMoney= taxMoney.add(result.getTax());
				grossMoney=grossMoney.add(result.getGross());
				billNet.setValue(netMoney);
				billTax.setValue(taxMoney);
				billGross.setValue(grossMoney);
			}catch(Exception x){}
			table.setWidget(row+1, 4, billNet);
			table.setWidget(row+1, 5, billTax);
			table.setWidget(row+1, 6, billGross);
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
		   String error = "void line failed\n";
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
}
