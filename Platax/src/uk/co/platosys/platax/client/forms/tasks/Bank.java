package uk.co.platosys.platax.client.forms.tasks;



import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;

import uk.co.platosys.platax.client.Commands;
import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.pws.fieldsets.OneButtonField;
import uk.co.platosys.pws.fieldsets.SubmitField;

public class Bank extends BasicTask {
	
	final ProductServiceAsync productService = (ProductServiceAsync) GWT.create(ProductService.class);
	//widgets
	PTab callingTab;
	
	OneButtonField newBank;
    
    public Bank() {
		super();
		try{
		setTabHead(TabTops.BANK);
		newBank = new OneButtonField(FieldText.NEW_BANKAC, 1000, this, false);
		newBank.addClickHander(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("button clicked");
				Commands.NEW_BANK_ACCOUNT.execute();
			}
		 });
		
		SubmitField sub= new SubmitField(12000, this);
	    
		
	 	
		
		
		}catch(Exception x){
			Window.alert("bank tab problem "+ x.getMessage());
		}
		setTitle(StringText.BANK_ACCOUNTS_TITLE);
		setSubTitle(StringText.BANK_ACCOUNTS_SUBTITLE);
		render();
    }

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}