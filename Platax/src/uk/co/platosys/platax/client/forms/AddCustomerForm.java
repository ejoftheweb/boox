package uk.co.platosys.platax.client.forms;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;

public class AddCustomerForm extends AbstractForm  {

	public AddCustomerForm(Platax platax, GWTEnterprise enterprise) {
		super(platax, enterprise.getName());
		Label customerNameLabel = new Label(LabelText.CUSTOMER);
		TextBox customerNameBox = new TextBox();
		Label customerInfoLabel = new Label(LabelText.CUSTOMER_NAME);
		table.setWidget(0,0, customerNameLabel);
		table.setWidget(0,1, customerNameBox);
		table.setWidget(0,2, customerInfoLabel);
		table.setWidget(1,0, new Label("Main Contact"));
		table.setWidget(2,0, new Label("Invoice Address"));
		Button button = new Button("submit");
		table.setWidget(3,2, button);
		button.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				 
				
			}
			
		});
	}

}
