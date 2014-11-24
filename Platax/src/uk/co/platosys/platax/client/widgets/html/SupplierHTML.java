package uk.co.platosys.platax.client.widgets.html;

import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTSupplier;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;

public class SupplierHTML extends InlineHTML {

	public SupplierHTML(GWTSupplier supplier) {
		super(supplier.getName());
		addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Open a new page with the customer details
				
			}
			 
		});
		 
	}

}
