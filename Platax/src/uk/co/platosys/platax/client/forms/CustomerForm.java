package uk.co.platosys.platax.client.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.services.CustomerService;
import uk.co.platosys.platax.client.services.CustomerServiceAsync;
import uk.co.platosys.platax.client.services.InvoiceService;
import uk.co.platosys.platax.client.services.InvoiceServiceAsync;
import uk.co.platosys.platax.client.widgets.labels.FieldInfoLabel;
import uk.co.platosys.platax.client.widgets.labels.FieldLabel;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;

public class CustomerForm extends AbstractForm  {

	public CustomerForm(final Platax platax, final GWTEnterprise enterprise) {
		super(platax, enterprise.getName());
		final CustomerServiceAsync customerService = (CustomerServiceAsync) GWT.create(CustomerService.class);
		
		setTitle(StringText.NEW_CUSTOMER);
		setSubTitle(StringText.NEW_CUSTOMER_INFO);
		
		final TextBox customerNameBox = new TextBox();
		FieldLabel customerNameLabel = new FieldLabel(LabelText.CUSTOMER_NAME);
		FieldInfoLabel customerNameInfoLabel = new FieldInfoLabel(LabelText.CUSTOMER_NAME_INFO);
		
		FieldLabel customerLegalnameLabel = new FieldLabel(LabelText.CUSTOMER_LEGALNAME);
		FieldInfoLabel customerLegalnameInfoLabel = new FieldInfoLabel(LabelText.CUSTOMER_LEGALNAME_INFO);
		final TextBox customerLegalnameBox = new TextBox();
		
		FieldLabel isTradeLabel = new FieldLabel(LabelText.IS_TRADE);
		FieldInfoLabel isTradeInfoLabel=new FieldInfoLabel(LabelText.IS_TRADE_INFO);
		final CheckBox isTradeBox = new CheckBox();
		
		
		table.setWidget(0,0, customerNameLabel);
		table.setWidget(0,1, customerNameBox);
		table.setWidget(0,2, customerNameInfoLabel);
		
		table.setWidget(1,0, customerLegalnameLabel);
		table.setWidget(1,1, customerLegalnameBox);
		table.setWidget(1,2, customerLegalnameInfoLabel);
		
		table.setWidget(2,0, isTradeLabel);
		table.setWidget(2,1, isTradeBox);
		table.setWidget(2,2, isTradeInfoLabel);
		

		Button button = new Button("submit");
		table.setWidget(3,2, button);
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
					customerLegalnameBox.setValue(result.getLegalName());
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
		customerNameBox.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent event) {
				String customerName = customerNameBox.getValue();
				customerService.checkName(customerName, checkCallback);
				setTabHeaderText(customerName);
				setTitle(customerName);
			}
		});
		
		
	  
		button.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				String customerName = customerNameBox.getValue();
				boolean isTrade = isTradeBox.getValue();
				customerService.addCustomer(enterprise.getSysname(), customerName, isTrade, finalCallback);
			}
		});
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}
