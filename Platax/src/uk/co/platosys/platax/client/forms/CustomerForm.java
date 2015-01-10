package uk.co.platosys.platax.client.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.services.CustomerService;
import uk.co.platosys.platax.client.services.CustomerServiceAsync;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.pws.fieldsets.SubmitField;
import uk.co.platosys.pws.fieldsets.TextField;
import uk.co.platosys.pws.fieldsets.TickBoxField;

public class CustomerForm extends AbstractForm  {

	public CustomerForm(final Platax platax, final GWTEnterprise enterprise) {
		super(platax, enterprise.getName());
		final CustomerServiceAsync customerService = (CustomerServiceAsync) GWT.create(CustomerService.class);
		
		setTitle(StringText.NEW_CUSTOMER);
		setSubTitle(StringText.NEW_CUSTOMER_INFO);
		final TextField customerName = new TextField(FieldText.NAME, 1000, this, true);
		final TextField customerLegalName= new TextField(FieldText.LEGALNAME, 2000, this, true);
		final TickBoxField isTrade=new TickBoxField(FieldText.IS_TRADE_CUSTOMER, 3000, this, true);
		SubmitField sub=new SubmitField(12000, this);
		
		
		
		final AsyncCallback<GWTCustomer> checkCallback=new AsyncCallback<GWTCustomer>(){
			@Override
			public void onFailure(Throwable cause) {
				 //Debugging code
				StackTraceElement[] st = cause.getStackTrace();
			   String error = "check customer name failed\n";
			   error = error+cause.getClass().getName()+"\n";
			   for (int i=0; i<st.length; i++){
				   error = error + st[i].toString()+ "\n";
			   }
				Window.alert(error);
			}
		   @Override
			public void onSuccess(GWTCustomer result) {
				if(result!=null){
					//customerLegalnameBox.setValue(result.getLegalName());
				}
			}
	  };
	  final AsyncCallback<GWTCustomer> finalCallback=new AsyncCallback<GWTCustomer>(){
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("error creating customer");
			}
		   @Override
			public void onSuccess(GWTCustomer result) {
				if(result!=null){
					platax.removeTab(CustomerForm.this);
				}else{
					Window.alert("null customer returned");
				}
			}
	  };
		customerName.addValueChangeHandler(new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String customer = customerName.getValue();
				customerService.checkName(customer, checkCallback);
				setTabHeaderText(customer);
				setTitle(customer);
			}
		
		});
		
		
	  
		sub.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				String customer = customerName.getValue();
				boolean trade = isTrade.getValue();
				customerService.addCustomer(enterprise.getSysname(), customer, trade, finalCallback);
			}
		});
		render();
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}
