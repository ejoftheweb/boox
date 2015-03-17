package uk.co.platosys.platax.client.forms.tasks;



import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.platax.client.services.BankService;
import uk.co.platosys.platax.client.services.BankServiceAsync;
import uk.co.platosys.platax.shared.boox.GWTBank;
import uk.co.platosys.pws.fieldsets.AddressField;
import uk.co.platosys.pws.fieldsets.MoneyField;
import uk.co.platosys.pws.fieldsets.SubmitField;
import uk.co.platosys.pws.fieldsets.TextField;

public class NewBankAccount extends TTab {
	
	final BankServiceAsync bankService = (BankServiceAsync) GWT.create(BankService.class);
	//widgets
	PTab callingTab;
	
	TextField accountName;
	TextField accountHolder;
	TextField accountNumber;
	TextField accountSortCode;
	TextField accountIban; 
	AddressField address;
	MoneyField lowLimit;
	MoneyField highLimit;
    
	AsyncCallback<GWTBank> callback=new AsyncCallback<GWTBank>(){


		@Override
		public void onFailure(Throwable caught) {
			Window.alert("there was a problem adding the bank account "+ caught.getMessage());
		}

		@Override
		public void onSuccess(GWTBank result) {

			if(result==null){
				Window.alert("there was a server problem adding the bank account");
			}else{
				if(Window.confirm("Bank account "+result.getName()+" added OK \n add Another?")){
					refresh();
				}else{
					close();
				}
			}
			
		}
		
	};
	
    public NewBankAccount() {
    	super();
    	//Window.alert("new bank form started");
		setTabHead(TabTops.NEW_BANK);
		accountName=new TextField(FieldText.BANKAC_NAME, 1000, this, true);
		accountHolder=new TextField(FieldText.BANKAC_HOLDER, 2000, this, true);
		accountNumber= new TextField(FieldText.BANKAC_NUMBER, 3000, this, true);
		accountSortCode=new TextField(FieldText.BANCAC_SORTCODE, 4000, this, true);
		accountIban=new TextField(FieldText.BANCAC_IBAN, 5000, this, true);
		address= new AddressField(FieldText.BANKAC_ADDRESS, 6000, this, true);
		lowLimit=new MoneyField(FieldText.BANKAC_LOWLIMIT, 7000, this, false);
		highLimit=new MoneyField(FieldText.BANKAC_HIGHLIMIT, 8000, this, false);
		//Add callbacks to populate lists etc
		
		SubmitField sub= new SubmitField(12000, this);
	    //Add handlers as needed
		sub.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				submit();
			}
			
		});
	 	
		
		setTitle(StringText.NEW_BANK_ACCOUNT_TITLE);
		setSubTitle(StringText.NEW_BANK_ACCOUNT_SUBTITLE);
		render();
    }
    private void submit(){
    	GWTBank bank=new GWTBank();
    	bank.setName(accountName.getValue());
    	bank.setAccno(accountNumber.getValue());
    	bank.setSortcode(accountSortCode.getValue());
    	bank.setIban(accountIban.getValue());
    	bank.setAddress(address.getValue());
    	Window.alert("addressid is "+bank.getAddress().getxAddressID());
    	bank.setLowlimit(lowLimit.getValue());
    	bank.setHighlimit(highLimit.getValue());
    	try{
    	bankService.addBankAccount(bank, getEnterprise().getSysname(), callback);
    	}catch(Exception x){
    		Window.alert("NBA -submit error" +x.getMessage());
    	}
    }
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}
