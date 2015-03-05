package uk.co.platosys.platax.client.forms.tasks;



import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;

import uk.co.platosys.platax.client.Commands;
import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.pws.fieldsets.OneButtonField;

public class Bank extends TTab {
	
	//final ProductServiceAsync productService = (ProductServiceAsync) GWT.create(ProductService.class);
	//widgets
	PTab callingTab;
	
	OneButtonField newBank;
    
    public Bank() {
		super();
		setTabHead(TabTops.BANK);
		newBank = new OneButtonField(FieldText.NEW_BANKAC, 1000, this, false);
		render();
		try{
			newBank.addClickHander(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("button clicked");
					Commands.NEW_BANK_ACCOUNT.execute();
				}
			 });
		}catch(Exception x){
			Window.alert("bank tab problem "+ x.getMessage());
		}
		//SubmitField sub= new SubmitField(12000, this);
		setTitle(StringText.BANK_ACCOUNTS_TITLE);
		setSubTitle(StringText.BANK_ACCOUNTS_SUBTITLE);
		
    }

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}
