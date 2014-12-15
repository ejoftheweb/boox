package uk.co.platosys.platax.client.forms.tasks;

import java.util.Locale;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.ButtonText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.platax.client.widgets.MoneyBox;
import uk.co.platosys.platax.client.widgets.PListBox;
import uk.co.platosys.platax.client.widgets.PTab;
import uk.co.platosys.platax.client.widgets.TaxBandChooser;
import uk.co.platosys.platax.client.widgets.labels.FieldInfoLabel;
import uk.co.platosys.platax.client.widgets.labels.FieldLabel;

public class HireStaff extends BasicTask {
	
	final ProductServiceAsync productService = (ProductServiceAsync) GWT.create(ProductService.class);
	//widgets
	FieldLabel givenName = new FieldLabel(StringText.GIVEN_NAME);
	FieldInfoLabel givenNameInfo = new FieldInfoLabel(StringText.GIVEN_NAME_INFO);
    final TextBox givenNameBox= new TextBox();
    FieldLabel familyName = new FieldLabel(StringText.FAMILY_NAME);
	FieldInfoLabel familyNameInfo = new FieldInfoLabel(StringText.FAMILY_NAME_INFO);
    final TextBox familyNameBox= new TextBox();
    FieldLabel email = new FieldLabel(LabelText.EMAIL);
  	FieldInfoLabel emailInfo = new FieldInfoLabel(LabelText.EMAIL_INFO);
    final TextBox emailBox= new TextBox();
    FieldLabel phoneNo = new FieldLabel(StringText.PHONE_NO);
  	FieldInfoLabel phoneNoInfo = new FieldInfoLabel(StringText.PHONE_NO_INFO);
    final TextBox phoneNoBox= new TextBox();
    FieldLabel natInsuranceNo = new FieldLabel(StringText.NAT_INS);
  	FieldInfoLabel natInsuranceInfo = new FieldInfoLabel(StringText.NAT_INS_INFO);
    final TextBox natInsuranceBox= new TextBox();
    FieldLabel dob = new FieldLabel(StringText.DOB);
  	FieldInfoLabel dobInfo = new FieldInfoLabel(StringText.DOB_INFO);
    final DateBox dobBox= new DateBox();
    FieldLabel nationality = new FieldLabel(StringText.NATIONALITY);
  	FieldInfoLabel nationalityInfo = new FieldInfoLabel(StringText.NATIONALITY_INFO);
    final PListBox nationalityBox= new PListBox();
    FieldLabel canWork = new FieldLabel(StringText.CAN_WORK);
  	FieldInfoLabel canWorkInfo = new FieldInfoLabel(StringText.CAN_WORK_INFO);
    final FileUpload canWorkBox= new FileUpload();
    Button submitButton = new Button(ButtonText.CONFIRM);
    Platax platax;
    PTab callingTab;
	
    
    public HireStaff(Platax platax) {
		super(platax, LabelText.HIRE_STAFF);
			this.platax=platax;
		setTitle(StringText.NEW_STAFF);
		setSubTitle(StringText.NEW_STAFF_INFO);
		//layout page
	    table.setWidget(0,0, givenName);
	    table.setWidget(0,1, givenNameBox);
	    table.setWidget(0,2, givenNameInfo);
	    table.setWidget(1,0, familyName);
	    table.setWidget(1,1, familyNameBox);
	    table.setWidget(1,2, familyNameInfo);
	    table.setWidget(2,0, email);
	    table.setWidget(2,1, emailBox);
	    table.setWidget(2,2, emailInfo);
	    table.setWidget(3,0, phoneNo);
	    table.setWidget(3,1, phoneNoBox);
	    table.setWidget(3,2, phoneNoInfo);
	    table.setWidget(4,0, natInsuranceNo);
	    table.setWidget(4,1, natInsuranceBox);
	    table.setWidget(4,2, natInsuranceInfo);
	    table.setWidget(5,0, dob);
	    table.setWidget(5,1, dobBox);
	    table.setWidget(5,2, dobInfo);
	    table.setWidget(6,0, nationality);
	    table.setWidget(6,1, nationalityBox);
	    table.setWidget(6,2, nationalityInfo);
	    table.setWidget(7,0, canWork);
	    table.setWidget(7,1, canWorkBox);
	    table.setWidget(7,2, canWorkInfo);
	    
	    table.setWidget(8,1, submitButton);
	    String [] locales = Locale.getISOCountries();
	    for (String locale:locales){
	    	nationalityBox.addItem(locale);
	    }
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}
