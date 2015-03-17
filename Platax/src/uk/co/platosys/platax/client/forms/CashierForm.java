package uk.co.platosys.platax.client.forms;


import java.util.Iterator;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.EFTab;
import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.platax.client.services.CashService;
import uk.co.platosys.platax.client.services.CashServiceAsync;
import uk.co.platosys.platax.client.services.StaffService;
import uk.co.platosys.platax.client.services.StaffServiceAsync;
import uk.co.platosys.platax.client.widgets.TaxBandChooser;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTItem;
import uk.co.platosys.pws.fieldsets.ListField;
import uk.co.platosys.pws.fieldsets.SubmitField;
import uk.co.platosys.pws.inputfields.MoneyBox;
import uk.co.platosys.pws.labels.FieldInfoLabel;
import uk.co.platosys.pws.labels.FieldLabel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class CashierForm extends EFTab { 
	//declare variables
	
	//services
	final CashServiceAsync cashService = (CashServiceAsync) GWT.create(CashService.class);
	final StaffServiceAsync staffService = (StaffServiceAsync) GWT.create(StaffService.class);
	//widgets
	ListField staffField;
	SubmitField sub;
    //callbacks
	
  	//regular constructor	
	public CashierForm() {
		super();
		setTabHead(TabTops.NEW_CASHIER);
	staffField=new ListField(FieldText.NEWCASHIER_NAME, 1000, this, true);
	sub=new SubmitField(12000,this);
	setTitle(StringText.NEW_CASHIER_HEAD);
	setSubTitle(StringText.NEW_CASHIER_SUBHEAD);
	
	render();
	    //add change handlers
	    /*productNameBox.addChangeHandler(new ChangeHandler(){
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
						cashierNameBox.getValue(), 
						//familyDescBox.getValue(), 
						productPriceBox.getMoney().getAmount(), 
						productTaxChooser.getTaxBand(), 
						taxInclusiveBox.getValue(), finalCallback);
			}
	     });*/
	}
   
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void add(IsWidget widget) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Iterator<Widget> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean remove(Widget widget) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
		
		
	
	}


