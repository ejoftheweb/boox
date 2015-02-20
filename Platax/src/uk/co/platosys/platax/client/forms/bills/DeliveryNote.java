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
import com.google.gwt.user.client.ui.Label;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.Styles;
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
import uk.co.platosys.platax.client.widgets.labels.FormSubHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.InlineLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyColumnHeaderLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyGrandTotalLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyTotalLabel;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTBill;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTInvoice;
import uk.co.platosys.platax.shared.boox.GWTItem;
import uk.co.platosys.platax.shared.boox.GWTLineItem;
import uk.co.platosys.pws.values.GWTMoney;
/**
 * InvoiceForm is a PTab for use in the central tabbed panel
 * 
 * @author edward
 *
 */

public class DeliveryNote extends AbstractBill {
	private GWTCustomer gwtCustomer=null;
	private GWTInvoice gwtInvoice=null;
	String invoiceSysname;
	String customerName;
	static final float DEFAULT_QUANTITY=1;
	
	//widgets
	//final Button newProductButton = new Button(ButtonText.ADD_NEW);
	//final Button newCustomerButton = new Button(ButtonText.ADD_NEW);
	final Button raiseInvoiceButton = (new Button(ButtonText.RAISE_INVOICE));
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
    			updateLine(result);
    		    //fillActiveRow();
    		    fillFinalRow();
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
    final AsyncCallback<GWTInvoice> saveInvoiceCallback = new AsyncCallback<GWTInvoice>(){
    	@Override
		public void onSuccess(GWTInvoice result) {
    		if(result==null){Window.alert(StringText.SERVER_ERROR+"INV4A");}
    		else {setInvoice(result);}
		}
  		@Override
		public void onFailure(Throwable cause) {
  			Window.alert(StringText.SERVER_ERROR+"INV4");
		}
    };
    final AsyncCallback<GWTInvoice> raiseInvoiceCallback = new AsyncCallback<GWTInvoice>(){
    	@Override
		public void onSuccess(GWTInvoice result) {
    		if(result==null){Window.alert(StringText.SERVER_ERROR+"INV5A");}
    		else {clearInvoice(result);}
		}
  		@Override
		public void onFailure(Throwable cause) {
  			Window.alert(StringText.SERVER_ERROR+"INV5");
		}
    };
    final AsyncCallback<GWTInvoice> deleteInvoiceCallback = new AsyncCallback<GWTInvoice>(){
    	@Override
		public void onSuccess(GWTInvoice result) {
    		clearInvoiceForm();
    	}
  		@Override
		public void onFailure(Throwable cause) {
  			Window.alert(StringText.SERVER_ERROR+"INV6");
		}
    };
    //constructor
	/**
	 * Constructs an invoice form.
	 * @param platax
	 * @param gwtEnterprise
	 */
	public DeliveryNote() {
		super();
		setStyleName(Styles.PTAB_INVOICE);
		setHeadStyleName(Styles.PTABH_INVOICE);
		setTitle(LabelText.INVOICE);
		setTabHeaderText(gwtEnterprise.getName()+":"+ LabelText.INVOICE);
		this.gwtEnterprise=gwtEnterprise;
		this.enterpriseName=gwtEnterprise.getName();
		this.enterpriseID=gwtEnterprise.getEnterpriseID();
		qtyBox.setQuantity(DEFAULT_QUANTITY);
    	refreshProducts();
		refreshCustomers();
		//the counter-party panel
		cpartyPanel.insert( new FormSubHeaderLabel(LabelText.CUSTOMER),0);
		cpartyNamePanel.setWidget(contactListBox);
		cpartyRefPanel.add(new InlineLabel(LabelText.POREF));
		cpartyRefPanel.add(refNumberBox);
		//cpartyPanel.add(newCustomerButton);
		//the submit-button panel
		//cancel and save are in the superclass, abstract bill, we just need raise:
		submitButtonPanel.add(raiseInvoiceButton);
		//handlers
		
		
		contactListBox.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent event){
				String customerSysname=contactListBox.getValue(contactListBox.getSelectedIndex());
				cpartyNamePanel.setWidget(new BodyNameLabel(contactListBox.getItemText(contactListBox.getSelectedIndex())));
				contactListBox.setSelectedIndex(0);
				invoiceService.createInvoice(enterpriseID, customerSysname,new Date(),  createInvoiceCallback);
			}
		});
		itemListBox.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				String itemName=itemListBox.getItemText(itemListBox.getSelectedIndex());
				if (itemName.equals(StringText.ADD_NEW)){
					AddProductPopupForm apf = new AddProductPopupForm(DeliveryNote.this, customerName, gwtEnterprise);
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
		raiseInvoiceButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if(Window.confirm("Are you sure you want to raise this invoice and send it the customer?")){
					raiseInvoice();
				}
			}
		});
		saveButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				saveInvoice();
			}
		});
		deleteButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if(Window.confirm("Are you sure you want to delete this invoice?")){
					deleteInvoice();
				}
			}
		});
		fillHeaderRow();
	}

	protected void setInvoice(GWTInvoice gwtInvoice) {
		this.gwtInvoice=gwtInvoice;
		setBill(gwtInvoice);
		Window.alert("invoice size " +gwtInvoice.getLineItems().size());
		billNumberBox.setValue(gwtInvoice.getUserno());
		this. itemListBox.addItems(gwtInvoice.getProducts());
		this.gwtCustomer=gwtInvoice.getCustomer();
		this.customerName=gwtCustomer.getName();
		//setTabHeaderText(enterpriseName+":"+customerName);
		for(GWTLineItem lineItem:gwtInvoice.getLineItems()){
			updateLine(lineItem);
			baseRow++;
		}
		fillActiveRow();
		fillFinalRow();
	}
    private void deleteInvoice(){
    	if(Window.confirm("Void this entire invoice?")){
    		invoiceService.deleteInvoice(gwtInvoice, deleteInvoiceCallback);
    		
    	}
    }
    private void raiseInvoice(){
    	invoiceService.raiseInvoice(gwtInvoice, raiseInvoiceCallback);
    }
    private void saveInvoice(){
    	clearInvoiceForm();
    	if(Window.confirm("Invoice No: "+gwtInvoice.getUserno()+ " for "+gwtInvoice.getGross().toPrefixedString() 
				+ " \n saved "
				+ " \n Do another one? "))
		{
		}else{
			close();
		}
    }
    
	 protected void insertLine(final GWTLineItem gwtLineItem) {
	    	int lineNo = gwtLineItem.getLineNumber();
	    	setRow(lineNo);
	    	//Window.alert("inserting line "+currentRow()+"\n price="+gwtLineItem.getPrice().toPlainString()+"\n tax="+gwtLineItem.getTax().toPlainString());
	    	Label lineNumberLabel=new Label();
	    	lineNumberLabel.setText(Integer.toString(lineNo));
	    	table.setWidget(currentRow(), 0, lineNumberLabel);
	    	table.setWidget(currentRow(), 1, new Label(gwtLineItem.getItemName()));
	    	QuantityBox quantityBox=new QuantityBox(gwtLineItem.getItemQty());
			table.setWidget(currentRow(), 2, quantityBox);
			quantityBox.addChangeHandler(new ChangeHandler(){
				@Override
				public void onChange(ChangeEvent event){
					//need to call the amend thing in the invoice.
				}
			});
			table.setWidget(currentRow(),3, new MoneyLabel(gwtLineItem.getPrice()));
			table.setWidget(currentRow(),4, new MoneyLabel(gwtLineItem.getNet()));
			table.setWidget(currentRow(),5, new MoneyLabel(gwtLineItem.getTax()));
			table.setWidget(currentRow(),6, new MoneyLabel(gwtLineItem.getGross()));
			final Button voidLineButton = new LineCancelButton();
			voidLineButton.addClickHandler(new ClickHandler(){
	           @Override
				public void onClick(ClickEvent event) {
					invoiceService.voidLine(gwtLineItem, voidLineCallback);
				}
			});
			table.setWidget(currentRow(),7, new Label("..."));
			table.setWidget(currentRow(), 8,voidLineButton);
	}
	 protected void updateLine(final GWTLineItem gwtLineItem) {
	    	int lineNo = gwtLineItem.getLineNumber();
	    	adjustTotals(gwtLineItem);
	    	//Window.alert("updating line "+lineNo+"\n price="+gwtLineItem.getPrice().toPlainString()+"\n tax="+gwtLineItem.getTax().toPlainString());
	    	table.setWidget(lineNo, 0, new Label(Integer.toString(lineNo)));
	    	table.setWidget(lineNo, 1, new Label(gwtLineItem.getItemName()));
	    	QuantityBox quantityBox=new QuantityBox(gwtLineItem.getItemQty());
			table.setWidget(lineNo, 2, quantityBox);
			quantityBox.addChangeHandler(new ChangeHandler(){
				@Override
				public void onChange(ChangeEvent event){
					//need to call the amend thing in the invoice.
				}
			});
			table.setWidget(lineNo,3, new MoneyLabel(gwtLineItem.getPrice()));
			table.setWidget(lineNo,4, new MoneyLabel(gwtLineItem.getNet()));
			table.setWidget(lineNo,5, new MoneyLabel(gwtLineItem.getTax()));
			table.setWidget(lineNo,6, new MoneyLabel(gwtLineItem.getGross()));
			final Button voidLineButton = new LineCancelButton();
			voidLineButton.addClickHandler(new ClickHandler(){
	           @Override
				public void onClick(ClickEvent event) {
					invoiceService.voidLine(gwtLineItem, voidLineCallback);
				}
			});
			table.setWidget(lineNo,7, new Label("OK"));
			table.setWidget(lineNo, 8,voidLineButton);
		}
	 
	 
    protected void postLine() throws Exception{
    	int rows = activeRow();
    	//Window.alert("row number being posted is "+rows);
    	final GWTLineItem gwtLineItem = new GWTLineItem();
    	gwtLineItem.setEnterprise(gwtEnterprise);
    	gwtLineItem.setCustomer(gwtCustomer);
    	gwtLineItem.setInvoiceSysname(gwtInvoice.getSysname());
    	gwtLineItem.setLineNumber(rows);
    	gwtLineItem.setItemSysname(itemListBox.getValue(itemListBox.getSelectedIndex()));
    	itemListBox.setSelectedIndex(0);
    	gwtLineItem.setItemQty(qtyBox.getQuantity());
    	qtyBox.setQuantity(DEFAULT_QUANTITY);
    	invoiceService.postLine(gwtLineItem, postLineCallback);
    	insertLine(gwtLineItem);
    	fillActiveRow();
	}
    
    public void refreshProducts(){
    	productService.listProducts(enterpriseID, 0, getItemsCallback);
    }
    public void refreshCustomers(){
    	customerService.listCustomers(enterpriseID, 0, getCustomersCallback);
    }
	public void clearInvoice(GWTInvoice result){
		clearInvoiceForm();
		if(Window.confirm("Invoice No: "+result.getUserno()+ " for "+result.getGross().toPrefixedString() 
				+ " \n raised and sent to "+result.getCustomer().getName()
				+ " \n Do another one? "))
		{
			
		}else{
			close();
		}
	}
    
    @Override
	public GWTBill getGWTBill() {
		return bill;
	}

	@Override
	public void refresh() {
		refreshProducts();
		refreshCustomers();
	}
	private void clearInvoiceForm(){
		clearBill();
		cpartyNamePanel.setWidget(contactListBox);
		customerName="";
		resetRow();
	}
	private void fillHeaderRow(){
		table.setWidget(0,0, new ColumnHeaderLabel(LabelText.ITEM_LINENO_HEADER));
		table.setWidget(0,1, new ColumnHeaderLabel(LabelText.ITEM_NAME_HEADER));
		table.setWidget(0,2, new ColumnHeaderLabel(LabelText.ITEM_QTY_HEADER));
		table.setWidget(0,3, new MoneyColumnHeaderLabel(LabelText.ITEM_PRICE_HEADER));
		table.setWidget(0,4, new MoneyColumnHeaderLabel(LabelText.ITEM_NET_HEADER));
		table.setWidget(0,5, new MoneyColumnHeaderLabel(LabelText.ITEM_TAX_HEADER));
		table.setWidget(0,6, new MoneyColumnHeaderLabel(LabelText.ITEM_GROSS_HEADER));
	}
	private void fillActiveRow(){
		//Window.alert("active row is"+activeRow());
		table.setWidget(activeRow(),0, new Label(Integer.toString(activeRow())));
		table.setWidget(activeRow(),1, itemListBox);
		table.setWidget(activeRow(),2, qtyBox);
		table.setWidget(activeRow(),3, postButton);
		table.setWidget(activeRow(),4, new Label(""));
		table.setWidget(activeRow(),5, new Label(""));
		table.setWidget(activeRow(),6, new Label(""));
	}
	private void fillFinalRow(){
		//Window.alert("Bill is"+bill.getSysname());
		
		//table.setWidget(0,0, new ColumnHeaderLabel(LabelText.ITEM_LINENO_HEADER));
		//table.setWidget(0,1, new ColumnHeaderLabel(LabelText.ITEM_NAME_HEADER));
		//table.setWidget(0,2, new ColumnHeaderLabel(LabelText.ITEM_QTY_HEADER));
		//table.setWidget(0,3, new MoneyColumnHeaderLabel(LabelText.ITEM_PRICE_HEADER));
		GWTMoney gross = bill.getGross();
		//Window.alert("Gross is "+gross.toPlainString());
		GWTMoney net = bill.getNet();
		GWTMoney tax= bill.getTax();
		table.setWidget(finalRow(),4, new MoneyTotalLabel(net));
		table.setWidget(finalRow(),5, new MoneyTotalLabel(tax));
		table.setWidget(finalRow(),6, new MoneyGrandTotalLabel(gross));
	}
	
}
