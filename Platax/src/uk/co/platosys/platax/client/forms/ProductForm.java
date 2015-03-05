package uk.co.platosys.platax.client.forms;


import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.EFTab;
import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.widgets.TaxBandChooser;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTItem;
import uk.co.platosys.pws.inputfields.MoneyBox;
import uk.co.platosys.pws.labels.FieldInfoLabel;
import uk.co.platosys.pws.labels.FieldLabel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;

public class ProductForm extends EFTab { 
	//declare variables
	
	//services
	final ProductServiceAsync productService = (ProductServiceAsync) GWT.create(ProductService.class);
	//widgets
	FieldLabel productName = new FieldLabel(StringText.PRODUCT_NAME);
	FieldInfoLabel productNameInfo = new FieldInfoLabel(StringText.PRODUCT_NAME_INFO);
    final TextBox productNameBox= new TextBox();
    FieldLabel productDesc = new FieldLabel(StringText.PRODUCT_DESC);
	FieldInfoLabel productDescInfo = new FieldInfoLabel(StringText.PRODUCT_DESC_INFO);
    final TextArea productDescBox= new TextArea();
    FieldLabel productPrice = new FieldLabel(StringText.PRODUCT_PRICE);
  	FieldInfoLabel productPriceInfo = new FieldInfoLabel(StringText.PRODUCT_PRICE_INFO);
    final MoneyBox productPriceBox= new MoneyBox("GBP");
    FieldLabel productTax = new FieldLabel(StringText.PRODUCT_TAX);
  	FieldInfoLabel productTaxInfo = new FieldInfoLabel(StringText.PRODUCT_TAX_INFO);
    final TaxBandChooser productTaxChooser= new TaxBandChooser("tbc");
    FieldLabel taxInclusive = new FieldLabel(StringText.TAX_INCLUSIVE);
  	FieldInfoLabel taxInclusiveInfo = new FieldInfoLabel(StringText.TAX_INCLUSIVE_INFO);
    final CheckBox taxInclusiveBox= new CheckBox();
    Button submitButton = new Button(ButtonText.CONFIRM);
    final Platax platax;
    PTab callingTab;
  //callbacks
  		final AsyncCallback<GWTItem> finalCallback=new AsyncCallback<GWTItem>(){
  			@Override
  			public void onFailure(Throwable caught) {
  				Window.alert(StringText.SERVER_ERROR+"PF0");
  			}
  			@Override
  			public void onSuccess(GWTItem result) {
  				String name = result.getName();
  				if (Window.confirm (name+ StringText.SUCCESS_ANOTHER)){
  					pageReset();
  				}else{
  					pageClose();
  				}
  			}
  		}; 
  	//regular constructor	
	public ProductForm(Platax platax, final GWTEnterprise enterprise) {
		super();
		this.platax=platax;
		setTitle(StringText.NEW_PRODUCT);
		setSubTitle(StringText.NEW_PRODUCT_INFO);
		//layout page
	    table.setWidget(0,0, productName);
	    table.setWidget(0,1, productNameBox);
	    table.setWidget(0,2, productNameInfo);
	    table.setWidget(1,0, productDesc);
	    table.setWidget(1,1, productDescBox);
	    table.setWidget(1,2, productDescInfo);
	    table.setWidget(2,0, productPrice);
	    table.setWidget(2,1, productPriceBox);
	    table.setWidget(2,2, productPriceInfo);
	    table.setWidget(3,0, productTax);
	    table.setWidget(3,1, productTaxChooser);
	    table.setWidget(3,2, productTaxInfo);
	    table.setWidget(4,0, taxInclusive);
	    table.setWidget(4,1, taxInclusiveBox);
	    table.setWidget(4,2, taxInclusiveInfo);
	    table.setWidget(5,1, submitButton);
	    //add change handlers
	    productNameBox.addChangeHandler(new ChangeHandler(){
	    	@Override
			public void onChange(ChangeEvent event) {
				setTitle(productNameBox.getValue());
				//do a server check for this product name.
			}
	     });
	     submitButton.addClickHandler(new ClickHandler(){
	    	@Override
			public void onClick(ClickEvent event) {
				productService.addProduct(enterprise.getSysname(), 
						productNameBox.getValue(), 
						productDescBox.getValue(), 
						productPriceBox.getMoney().getAmount(), 
						productTaxChooser.getTaxBand(), 
						taxInclusiveBox.getValue(), finalCallback);
			}
	     });
	}
    //overloaded constructor with reference to calling tab:
	public ProductForm(Platax platax, final GWTEnterprise enterprise, PTab callingTab) {
		this(platax, enterprise);
		this.callingTab=callingTab;
	}
	/**
	 * Clears the form	
	 */
	protected void pageReset() {
		setTitle(StringText.NEW_PRODUCT);
		setSubTitle(StringText.NEW_PRODUCT_INFO);
		 productNameBox.setValue(null);
		 productDescBox.setValue(null);
		 productPriceBox.setAmount(0);
	}
	/**
	 * closes the page and returns focus to the calling tab;
	 */
	protected void pageClose(){
		if(callingTab!=null){
			callingTab.refresh();
			platax.setSelectedTab(callingTab);
		}
		platax.removeTab(this);
	}
	@Override
	public void refresh() {
		pageReset();
	}
	
	
		
		
	
	}


