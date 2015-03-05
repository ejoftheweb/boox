package uk.co.platosys.platax.client.forms.popups;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.forms.bills.BTab;
import uk.co.platosys.platax.client.forms.bills.InvoiceForm;
import uk.co.platosys.platax.client.services.InvoiceService;
import uk.co.platosys.platax.client.services.InvoiceServiceAsync;
import uk.co.platosys.platax.client.widgets.buttons.ActionButton;
import uk.co.platosys.platax.client.widgets.labels.TabPageTitleLabel;
import uk.co.platosys.platax.client.widgets.labels.InlineLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyGrandTotalLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyLabel;
import uk.co.platosys.platax.client.widgets.labels.MoneyTotalLabel;
import uk.co.platosys.platax.shared.boox.GWTBill;
import uk.co.platosys.platax.shared.boox.GWTInvoice;
import uk.co.platosys.platax.shared.boox.GWTItem;

public class ConfirmBillPopupForm extends AbstractPopupForm {

	public ConfirmBillPopupForm(final BTab parent) {
		super(LabelText.CONFIRM_BILL);
		GWTBill gwtBill=parent.getGWTBill();
		ActionButton cancelButton= new ActionButton(ButtonText.CANCEL);
		table.setWidget(5,0, cancelButton);
		cancelButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				 
				ConfirmBillPopupForm.this.hide();
				
			} 
		});
		ActionButton confirmButton=new ActionButton(ButtonText.CONFIRM);
		table.setWidget(5,1, confirmButton);
		if(gwtBill instanceof GWTInvoice){
			final InvoiceServiceAsync invoiceService = (InvoiceServiceAsync) GWT.create(InvoiceService.class);
			final GWTInvoice gwtInvoice = (GWTInvoice) gwtBill;
			table.setWidget(0, 1,new TabPageTitleLabel("Confirm Invoice"));
			table.setWidget(1, 0, new InlineLabel(gwtInvoice.getCustomer().getName()));
			table.setWidget(1, 1, new InlineLabel(gwtInvoice.getCreatedDate().toString()));
			table.setWidget(2, 2, new InlineLabel(LabelText.ITEM_NET_HEADER));
			table.setWidget(2, 3, new MoneyTotalLabel(gwtInvoice.getNet()));
			table.setWidget(3,2, new InlineLabel(LabelText.ITEM_TAX_HEADER));
			table.setWidget(3,3, new MoneyLabel(gwtInvoice.getTax()));
			table.setWidget(4,2, new InlineLabel(LabelText.ITEM_GROSS_HEADER));
			table.setWidget(4,3, new MoneyGrandTotalLabel(gwtInvoice.getGross()));
			final AsyncCallback<GWTInvoice> callback = new AsyncCallback<GWTInvoice>(){
				ConfirmBillPopupForm popupForm = ConfirmBillPopupForm.this;
				public void onSuccess(GWTInvoice result){
					
					popupForm.hide();
					 parent.close();
				}
				public void onFailure(Throwable cause){
				   StackTraceElement[] st = cause.getStackTrace();
				   String error = "addProduct failed\n";
				   error = error+cause.getClass().getName()+"\n";
				   for (int i=0; i<st.length; i++){
					   error = error + st[i].toString()+ "\n";
				   }
					Window.alert(error);
				}
			 };
			
			 confirmButton.addClickHandler(new ClickHandler(){
			    @Override
				public void onClick(ClickEvent event) {
					invoiceService.raiseInvoice(gwtInvoice, callback);
				} 
			});
		}else{
			
		}
	}//end of constructor;
    
}//end of class
