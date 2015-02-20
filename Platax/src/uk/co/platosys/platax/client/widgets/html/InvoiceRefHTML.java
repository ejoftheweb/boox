package uk.co.platosys.platax.client.widgets.html;

import uk.co.platosys.platax.shared.boox.GWTInvoice;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.InlineHTML;

public class InvoiceRefHTML extends InlineHTML {

	public InvoiceRefHTML(GWTInvoice invoice) {
		super(invoice.getUserno());
		setStyleName("px_invoice_ref");
		addClickHandler(new ClickHandler(){
         
			@Override
			public void onClick(ClickEvent event) {
				// TODO Open a new page with the customer details
				
			}
			 
		});
		 
	}

}
