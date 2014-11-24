package uk.co.platosys.platax.client.forms.popups;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.forms.bills.AbstractBill;
import uk.co.platosys.platax.client.services.CustomerService;
import uk.co.platosys.platax.client.services.CustomerServiceAsync;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;

public class AddCustomerPopupForm extends AbstractPopupForm  {
//Logger logger = Logger.getLogger("platax");
	public AddCustomerPopupForm(final AbstractBill parent, final GWTEnterprise enterprise) {
		super(LabelText.ADD_CUSTOMER_HEADER);
		final CustomerServiceAsync addCustomerService = (CustomerServiceAsync) GWT.create(CustomerService.class);
		Label customerNameLabel = new Label(LabelText.CUSTOMER);
		Label isPrivateLabel=new Label(LabelText.IS_PRIVATE);
		final TextBox customerNameBox = new TextBox();
		final CheckBox isPrivateCheckBox = new CheckBox();
		Label customerInfoLabel = new Label(LabelText.CUSTOMER_NAME);
		Label isPrivateInfoLabel = new Label(LabelText.IS_PRIVATE_INFO);
		Label doMoreLaterLabel = new Label(LabelText.DOMORELATER);
		table.getFlexCellFormatter().setColSpan(0, 0, 3);
		table.setWidget(0,0,header);
		table.setWidget(1,0, customerNameLabel);
		table.setWidget(1,1, customerNameBox);
		table.setWidget(1,2, customerInfoLabel);
		table.setWidget(2,0, isPrivateLabel);
		table.setWidget(2, 1, isPrivateCheckBox);
		table.setWidget(2,2, isPrivateInfoLabel);
		table.getFlexCellFormatter().setColSpan(3,0,3);
		table.setWidget(3,0, doMoreLaterLabel);
		Button button = new Button("submit");
		table.setWidget(4,2, button);
		final AsyncCallback<GWTCustomer> callback = new AsyncCallback<GWTCustomer>(){
			public void onSuccess(GWTCustomer result){
				parent.getContactListBox().addItem(result.getName(), result.getSysname());
				AddCustomerPopupForm.this.hide();
			}
			public void onFailure(Throwable cause){
			   //Debugging code
				StackTraceElement[] st = cause.getStackTrace();
			   String error = "addCustomer failed\n";
			   error = error+cause.getClass().getName()+"\n";
			   for (int i=0; i<st.length; i++){
				   error = error + st[i].toString()+ "\n";
			   }
				Window.alert(error);
		}};
		button.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				boolean isPrivate = isPrivateCheckBox.getValue();
				String customerName = customerNameBox.getText();
				addCustomerService.addCustomer(enterprise.getEnterpriseID(),customerName, isPrivate, callback);
				//logger.log(Level.ALL, "submit button clicked");
				//parent.refreshData();
				AddCustomerPopupForm.this.hide();
			}
			
		});
	}

}
