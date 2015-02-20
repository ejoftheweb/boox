package uk.co.platosys.platax.client.forms.tasks;



import java.util.ArrayList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import uk.co.platosys.platax.client.Constants;
import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.platax.client.services.CustomerService;
import uk.co.platosys.platax.client.services.CustomerServiceAsync;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.pws.fieldsets.ListField;
import uk.co.platosys.pws.fieldsets.MoneyField;
import uk.co.platosys.pws.fieldsets.SubmitField;

public class PaymentsIn extends BasicTask {
	
	final ProductServiceAsync productService = (ProductServiceAsync) GWT.create(ProductService.class);
	final CustomerServiceAsync customerService=(CustomerServiceAsync) GWT.create(CustomerService.class);
	//widgets
	Platax platax;
    PTab callingTab;
	//fields.
    ListField paymentFrom;//= new ListField (FieldText.PAYMENT_FROM, 1000, this, true);
	ListField paymentTo;//= new ListField (FieldText.PAYMENT_TO,  2000, this, true);
	MoneyField amount ;//= new MoneyField(FieldText.PAYMENT_AMOUNT, 3000, this, true);
	
	//callbacks
	AsyncCallback<ArrayList<GWTCustomer>> custcallback = new AsyncCallback<ArrayList<GWTCustomer>>(){
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Server Error - PI");
		}
		@Override
		public void onSuccess(ArrayList<GWTCustomer> result) {
		    updateFromField(result); 	
		}
	};
    public PaymentsIn() {
		super();
		setTabHead(TabTops.PAYMENTS_IN);
		
		paymentFrom= new ListField (FieldText.PAYMENT_FROM, 1000, this, true);
		paymentTo= new ListField (FieldText.PAYMENT_TO,  2000, this, true);
		amount = new MoneyField(FieldText.PAYMENT_AMOUNT, 3000, this, true);
		SubmitField sub= new SubmitField(12000, this);
		this.platax=Platax.getCurrentInstance();
		this.enterprise=platax.getCurrentEnterprise();
		
		
		
		setTitle(StringText.PAYMENT_IN_HEAD);
		setSubTitle(StringText.PAYMENT_IN_SUBHEAD);
		customerService.listCustomers(enterprise.getEnterpriseID(), Constants.ALL_CUSTOMERS, custcallback);
		render();
    }

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
	private void updateFromField(ArrayList<GWTCustomer> customers){
		paymentFrom.addItems(customers);
	}
}
