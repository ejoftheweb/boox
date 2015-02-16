package uk.co.platosys.platax.client.forms.tasks;



import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.RadioText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.pws.constants.Nations;
import uk.co.platosys.pws.fieldsets.AddressField;
import uk.co.platosys.pws.fieldsets.DateField;
import uk.co.platosys.pws.fieldsets.ListField;
import uk.co.platosys.pws.fieldsets.MoneyField;
import uk.co.platosys.pws.fieldsets.RadioField;
import uk.co.platosys.pws.fieldsets.SubmitField;
import uk.co.platosys.pws.fieldsets.TextField;

public class IssueEquity extends BasicTask {
	
	final ProductServiceAsync productService = (ProductServiceAsync) GWT.create(ProductService.class);
	//widgets
	Platax platax;
    PTab callingTab;
	
	
    
    public IssueEquity() {
		super();
		setTabHead(TabTops.ISSUE_EQUITY);
		//Add form fields (from uk.co.platosys.pws.fieldsets)  here.
		
		
		
		//AbstractFormField canWork= new AbstractFormField (StringText.CAN_WORK, StringText.CAN_WORK_INFO, new TextBox(), 9000, this);
		SubmitField sub= new SubmitField(12000, this);
	    
		
	 this.platax=platax;
		
		
		setTitle(StringText.NEW_STAFF);
		setSubTitle(StringText.NEW_STAFF_INFO);
		render();
    }

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}
