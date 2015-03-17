package uk.co.platosys.platax.client.forms.tasks;



import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.RadioText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.platax.client.services.BankService;
import uk.co.platosys.platax.client.services.BankServiceAsync;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.platax.shared.boox.GWTBank;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.pws.fieldsets.AddressField;
import uk.co.platosys.pws.fieldsets.DateField;
import uk.co.platosys.pws.fieldsets.ListField;
import uk.co.platosys.pws.fieldsets.MoneyField;
import uk.co.platosys.pws.fieldsets.NationalityField;
import uk.co.platosys.pws.fieldsets.RadioField;
import uk.co.platosys.pws.fieldsets.SubmitField;
import uk.co.platosys.pws.fieldsets.TextField;



public class PaymentsOut extends TTab {
	
	final ProductServiceAsync productService = (ProductServiceAsync) GWT.create(ProductService.class);
	final BankServiceAsync bankService = (BankServiceAsync) GWT.create(BankService.class);
	AsyncCallback<List<GWTBank>> bankscallback = new AsyncCallback<List<GWTBank>>(){
		public void onFailure(Throwable caught) {
			Window.alert("Server Error - Bank info");
		}
		@Override
		public void onSuccess(List<GWTBank> result) {
		    updateFromField(result); 	
		}
	};
	AsyncCallback<Long> paycallback=new AsyncCallback<Long>(){
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("There was a problem "+caught.getMessage());
			
		}
		@Override
		public void onSuccess(Long result) {
			if(result>0){
				if(Window.confirm("Payment posted with ID "+result+" \n do another?")){
					refresh();
				}else{
					close();
				}
			}else{
				Window.alert("There was a problem on the server and the payment wasn't recorded");
			}
			
		}
		
	};
	//widgets
	Platax platax;
   PTab callingTab;
	
    ListField paymentFrom;//= new ListField (FieldText.PAYMENT_FROM, 1000, this, true);
  	ListField paymentTo;//= new ListField (FieldText.PAYMENT_TO,  2000, this, true);
  	MoneyField amount ;//= new MoneyField(FieldText.PAYMENT_AMOUNT, 3000, this, true);
  	TextField note;
	
    
    public PaymentsOut() {
		super();
		setTabHead(TabTops.PAYMENTS_OUT);
		
		paymentTo= new ListField (FieldText.PAYMENT_TO,  2000, this, true);
		
		paymentFrom= new ListField (FieldText.PAYMENT_FROM, 1000, this, true);
		amount = new MoneyField(FieldText.PAYMENT_AMOUNT, 3000, this, true);
		note=new TextField(FieldText.PAYMENT_NOTE, 4000, this, false);
		SubmitField sub= new SubmitField(12000, this);
	    sub.addClickHandler(new ClickHandler(){
	    	@Override
			public void onClick(ClickEvent event) {
	    		submit();
			}
	    });
		
	 this.platax=Platax.getCurrentInstance();
		
		
	 setTitle(StringText.PAYMENT_OUT_HEAD);
		setSubTitle(StringText.PAYMENT_OUT_SUBHEAD);
		render();
    }

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
	private void updateFromField(List<GWTBank> banks){
		paymentFrom.addItems(banks);
	}
	private void submit(){
		bankService.pay(getEnterprise().getSysname(), paymentTo.getValue(), paymentFrom.getValue(), amount.getValue(), note.getValue(), paycallback);
	}
}
