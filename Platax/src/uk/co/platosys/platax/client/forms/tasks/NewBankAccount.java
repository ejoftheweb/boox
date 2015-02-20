package uk.co.platosys.platax.client.forms.tasks;



import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.pws.fieldsets.AddressField;
import uk.co.platosys.pws.fieldsets.SubmitField;
import uk.co.platosys.pws.fieldsets.TextField;

public class NewBankAccount extends BasicTask {
	
	final ProductServiceAsync productService = (ProductServiceAsync) GWT.create(ProductService.class);
	//widgets
	PTab callingTab;
	
	TextField accountName;
	TextField accountHolder;
	TextField accountNumber;
	TextField accountSortCode;
	TextField accountIban; 
	AddressField address;
    
    public NewBankAccount() {
    	super();
    	Window.alert("new bank form started");
		setTabHead(TabTops.NEW_BANK);
		accountName=new TextField(FieldText.BANKAC_NAME, 1000, this, true);
		accountHolder=new TextField(FieldText.BANKAC_HOLDER, 2000, this, true);
		accountNumber= new TextField(FieldText.BANKAC_NUMBER, 3000, this, true);
		accountSortCode=new TextField(FieldText.BANCAC_SORTCODE, 4000, this, true);
		accountIban=new TextField(FieldText.BANCAC_IBAN, 5000, this, true);
		address= new AddressField(FieldText.BANKAC_ADDRESS, 6000, this, true);
		//Add callbacks to populate lists etc
		
		SubmitField sub= new SubmitField(12000, this);
	    //Add handlers as needed
		
	 	
		
		setTitle(StringText.NEW_BANK_ACCOUNT_TITLE);
		setSubTitle(StringText.NEW_BANK_ACCOUNT_SUBTITLE);
		render();
    }

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}
