package uk.co.platosys.platax.client.forms.tasks;



import com.google.gwt.core.client.GWT;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.RadioText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
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
	//widgets
	Platax platax;
    PTab callingTab;
	
	
    
    public PaymentsOut() {
		super();
		setTabHead(TabTops.PAYMENTS_OUT);
		
		ListField paymentTo= new ListField (FieldText.PAYMENT_TO,  2000, this, true);
		
		ListField paymentFrom= new ListField (FieldText.PAYMENT_FROM, 1000, this, true);
		MoneyField amount = new MoneyField(FieldText.PAYMENT_AMOUNT, 3000, this, true);
	 //AbstractFormField canWork= new AbstractFormField (StringText.CAN_WORK, StringText.CAN_WORK_INFO, new TextBox(), 9000, this);
		SubmitField sub= new SubmitField(12000, this);
	    
		
	 this.platax=Platax.getCurrentInstance();
		
		
	 setTitle(StringText.PAYMENT_OUT_HEAD);
		setSubTitle(StringText.PAYMENT_OUT_SUBHEAD);
		render();
    }

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}
