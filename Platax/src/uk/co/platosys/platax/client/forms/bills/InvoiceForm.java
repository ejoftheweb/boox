package uk.co.platosys.platax.client.forms.bills;

import java.util.ArrayList;
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
import uk.co.platosys.platax.client.constants.Styles;
import uk.co.platosys.platax.client.forms.CustomerForm;
import uk.co.platosys.platax.client.forms.ProductForm;
import uk.co.platosys.platax.client.forms.popups.AddCustomerPopupForm;
import uk.co.platosys.platax.client.forms.popups.AddProductPopupForm;
import uk.co.platosys.platax.client.services.CustomerService;
import uk.co.platosys.platax.client.services.CustomerServiceAsync;
import uk.co.platosys.platax.client.services.InvoiceService;
import uk.co.platosys.platax.client.services.InvoiceServiceAsync;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.platax.client.widgets.QuantityBox;
import uk.co.platosys.platax.client.widgets.buttons.LineCancelButton;
import uk.co.platosys.platax.client.widgets.labels.BodyNameLabel;
import uk.co.platosys.platax.client.widgets.labels.ColumnHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.FormHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.FormSubHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyColumnHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyLabel;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTBill;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTInvoice;
import uk.co.platosys.platax.shared.boox.GWTItem;
import uk.co.platosys.platax.shared.boox.GWTLineItem;
import uk.co.platosys.util.ISODate;
/**
 * InvoiceForm is a PTab for use in the central tabbed panel
 * 
 * @author edward
 *
 */

public class InvoiceForm extends AbstractBill {
	private GWTCustomer gwtCustomer=null;
	private GWTInvoice gwtInvoice=null;
	String invoiceSysname;
	String customerName;
	//widgets
	final Button newProductButton = new Button(ButtonText.ADD_NEW);
	final Button newCustomerButton = new Button(ButtonText.ADD_NEW);
	
	//services
    final InvoiceServiceAsync invoiceService = (InvoiceServiceAsync) GWT.create(InvoiceService.class);
    final ProductServiceAsync productService = (ProductServiceAsync) GWT.create(ProductService.class);
    final CustomerServiceAsync customerService = (CustomerServiceAsync) GWT.create(CustomerService.class);
	
    //callbacks
    final AsyncCallback<ArrayList<GWTItem>> getItemsCallback = new AsyncCallback<ArrayList<GWTItem>>(){
    	@Override
		public void onFailure(Throwable caught) {
			Window.alert(StringText.SERVER_ERROR+"INV0");
		}
    	@Override
		public void onSuccess(ArrayList<GWTItem> result) {
    		if(result==null){Window.alert(StringText.SERVER_ERROR+"INV0A");}
    		else {itemListBox.addItems(result);}
		}
    };
    
    final AsyncCallback<GWTInvoice> createInvoiceCallback = new AsyncCallback<GWTInvoice>(){
    	@Override
		public void onSuccess(GWTInvoice result) {
    		if(result==null){Window.alert(StringText.SERVER_ERROR+"INV1A");}
    		else {setInvoice(result);}
		}
  		@Override
		public void onFailure(Throwable cause) {
  			Window.alert(StringText.SERVER_ERROR+"INV1");
		}
    };
    
    final AsyncCallback<GWTLineItem> postLineCallback = new AsyncCallback<GWTLineItem>(){
    	@Override
		public void onSuccess(GWTLineItem result) {
    		if(result==null){Window.alert(StringText.SERVER_ERROR+"INV2A");}
    		else {
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
    	}}
		@Override
		public void onFailure(Throwable cause) {
			Window.alert(StringText.SERVER_ERROR+"INV2");
		}
    };
    
    final AsyncCallback<GWTLineItem> voidLineCallback = new AsyncCallback<GWTLineItem>(){
    	@Override
		public void onSuccess(GWTLineItem result) {
    		if(result==null){Window.alert(StringText.SERVER_ERROR+"INV3A");}
    		else {
    		int row = result.getLineNumber();
    		table.setWidget(row,1, new Label(result.getItemName()));
			table.setWidget(row,2, new Label(Float.toString(result.getItemQty())));
			table.setWidget(row,3, new MoneyLabel(result.getPrice()));
			table.setWidget(row,4, new MoneyLabel(result.getNet()));
			table.setWidget(row,5, new MoneyLabel(result.getTax()));
			table.setWidget(row,6, new MoneyLabel(result.getGross()));
		}}
		@Override
		public void onFailure(Throwable cause) {
			Window.alert(StringText.SERVER_ERROR+"INV3");
		}
    };
    final AsyncCallback<ArrayList<GWTCustomer>> getCustomersCallback = new AsyncCallback<ArrayList<GWTCustomer>>(){
    	@Override
		public void onFailure(Throwable caught) {
			Window.alert(StringText.SERVER_ERROR+"INV4");
		}
    	@Override
		public void onSuccess(ArrayList<GWTCustomer> result) {
    		if(result==null){Window.alert(StringText.SERVER_ERROR+"INV4A");}
    		else {contactListBox.addContacts(result);}
		}
    };
    
    //constructor
	/**
	 * Constructs an invoice form.
	 * @param platax
	 * @param gwtEnterprise
	 */
	public InvoiceForm(final Platax platax, final GWTEnterprise gwtEnterprise) {
		super(platax, gwtEnterprise.getName()+":"+StringText.INVOICE);
		setStyleName(Styles.PTAB_INVOICE);
		setHeadStyleName(Styles.PTABH_INVOICE);
		setTitle(LabelText.INVOICE);
		setTabHeaderText(gwtEnterprise.getName()+":"+ LabelText.INVOICE);
		this.gwtEnterprise=gwtEnterprise;
		this.enterpriseName=gwtEnterprise.getName();
		this.enterpriseID=gwtEnterprise.getEnterpriseID();
		
		refreshProducts();
		refreshCustomers();
		//the counter-party panel
		cpartyPanel.insert( new FormSubHeaderLabel(LabelText.CUSTOMER),0);
		cpartyNamePanel.setWidget(contactListBox);
		cpartyPanel.add(newCustomerButton);
		submitButtonPanel.add(new Button(ButtonText.RAISE_INVOICE));
		//handlers
		newCustomerButton.addClickHandler(new ClickHandler(){
		    @Override
			public void onClick(ClickEvent event) {
				platax.addTab(new CustomerForm(platax, gwtEnterprise));
			}
		});
		newProductButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
					platax.addTab(new ProductForm(platax, gwtEnterprise));
			}
		});
		contactListBox.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent event){
				String customerSysname=contactListBox.getValue(contactListBox.getSelectedIndex());
				cpartyNamePanel.setWidget(new BodyNameLabel(contactListBox.getItemText(contactListBox.getSelectedIndex())));
				cpartyPanel.remove(newCustomerButton);
				invoiceService.createInvoice(enterpriseID, customerSysname,new Date(),  createInvoiceCallback);
				table.setWidget(0,0, new ColumnHeaderLabel(LabelText.ITEM_LINENO_HEADER));
				table.setWidget(0,1, new ColumnHeaderLabel(LabelText.ITEM_NAME_HEADER));
				table.setWidget(0,2, new ColumnHeaderLabel(LabelText.ITEM_QTY_HEADER));
				table.setWidget(0,3, new MoneyColumnHeaderLabel(LabelText.ITEM_PRICE_HEADER));
				table.setWidget(0,4, new MoneyColumnHeaderLabel(LabelText.ITEM_NET_HEADER));
				table.setWidget(0,5, new MoneyColumnHeaderLabel(LabelText.ITEM_TAX_HEADER));
				table.setWidget(0,6, new MoneyColumnHeaderLabel(LabelText.ITEM_GROSS_HEADER));
				table.setWidget(1,0, new Label());
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
					e.printStackTrace();
				}
			}
		});
		
	}

	protected void setInvoice(GWTInvoice gwtInvoice) {
		this.gwtInvoice=gwtInvoice;
		Window.alert("invoice size" +gwtInvoice.getLineItems().size());
		billNumberBox.setValue(gwtInvoice.getUserno());
		this. itemListBox.addItems(gwtInvoice.getProducts());
		this.gwtCustomer=gwtInvoice.getCustomer();
		this.customerName=gwtCustomer.getName();
		setTabHeaderText(enterpriseName+":"+customerName);
		for(GWTLineItem lineItem:gwtInvoice.getLineItems()){
			insertLine(lineItem);
		}
		
	}

	 protected void insertLine(final GWTLineItem gwtLineItem) {
	    	final int rows = table.getRowCount()-1;
	    	table.setWidget(rows, 1, new Label(gwtLineItem.getItemName()));
			table.setWidget(rows, 2, new QuantityBox(gwtLineItem.getItemQty()));
			table.setWidget(rows,3, new MoneyLabel(gwtLineItem.getPrice()));
			table.setWidget(rows,4, new MoneyLabel(gwtLineItem.getNet()));
			table.setWidget(rows,5, new MoneyLabel(gwtLineItem.getTax()));
			table.setWidget(rows,6, new MoneyLabel(gwtLineItem.getGross()));
			final Button voidLineButton = new LineCancelButton();
			voidLineButton.addClickHandler(new ClickHandler(){
	           @Override
				public void onClick(ClickEvent event) {
					invoiceService.voidLine(gwtLineItem, voidLineCallback);
				}
			});
			table.setWidget(rows, 7,voidLineButton);
		}
    protected void postLine() throws Exception{
    	final int rows = table.getRowCount()-1;
    	final GWTLineItem gwtLineItem = new GWTLineItem();
    	gwtLineItem.setEnterprise(gwtEnterprise);
    	gwtLineItem.setCustomer(gwtCustomer);
    	gwtLineItem.setSIN(gwtInvoice.getSysname());
    	gwtLineItem.setLineNumber(rows);
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
    
    public void refreshProducts(){
    	productService.listProducts(enterpriseID, 0, getItemsCallback);
    }
    public void refreshCustomers(){
    	customerService.listCustomers(enterpriseID, 0, getCustomersCallback);
    }
	@Override
	public GWTBill getGWTBill() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		refreshProducts();
		refreshCustomers();
	}
}
