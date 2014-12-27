package uk.co.platosys.platax.client.forms.tasks;



import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.forms.fields.FieldText;
import uk.co.platosys.platax.client.forms.fields.AbstractFormField;
import uk.co.platosys.platax.client.forms.fields.TextField;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.platax.client.utils.Nations;
import uk.co.platosys.platax.client.widgets.PListBox;
import uk.co.platosys.platax.client.widgets.PTab;
import uk.co.platosys.platax.client.widgets.labels.FieldInfoLabel;
import uk.co.platosys.platax.client.widgets.labels.FieldLabel;
import uk.co.platosys.platax.shared.FieldVerifier;

public class HireStaff extends BasicTask {
	
	final ProductServiceAsync productService = (ProductServiceAsync) GWT.create(ProductService.class);
	//widgets
	Platax platax;
    PTab callingTab;
	
	
    
    public HireStaff(Platax platax) {
		super(platax, LabelText.HIRE_STAFF);
		//also need: pay rate, pay frequency, bank details
		TextField givenName= new TextField (FieldText.GIVEN_NAME, 1000, this);
		TextField familyName= new TextField (FieldText.FAMILY_NAME, 2000, this);
		TextField email= new TextField (FieldText.EMAIL, 3000, this);
		TextField phoneNo= new TextField (FieldText.PHONE, 4000, this);
		AbstractFormField address= new AbstractFormField (StringText.PHONE_NO, StringText.PHONE_NO_INFO, new TextBox(), 5000, this);
		AbstractFormField natInsNo= new AbstractFormField (StringText.NAT_INS, StringText.NAT_INS_INFO, new TextBox(), 6000, this);
		AbstractFormField dob= new AbstractFormField (StringText.DOB, StringText.DOB_INFO, new TextBox(), 7000, this);
		AbstractFormField nationality= new AbstractFormField (StringText.NATIONALITY, StringText.NATIONALITY_INFO, new TextBox(), 8000, this);
		AbstractFormField canWork= new AbstractFormField (StringText.CAN_WORK, StringText.CAN_WORK_INFO, new TextBox(), 9000, this);
	 Button submitButton = new Button(ButtonText.CONFIRM);
	    
		
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
