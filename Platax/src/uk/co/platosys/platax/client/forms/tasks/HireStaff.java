package uk.co.platosys.platax.client.forms.tasks;



import com.google.gwt.core.client.GWT;
import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.constants.FieldText;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.RadioText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.pws.fieldsets.AddressField;
import uk.co.platosys.pws.fieldsets.DateField;
import uk.co.platosys.pws.fieldsets.MoneyField;
import uk.co.platosys.pws.fieldsets.NationalityField;
import uk.co.platosys.pws.fieldsets.RadioField;
import uk.co.platosys.pws.fieldsets.SubmitField;
import uk.co.platosys.pws.fieldsets.TextField;

public class HireStaff extends BasicTask {
	
	final ProductServiceAsync productService = (ProductServiceAsync) GWT.create(ProductService.class);
	//widgets
	Platax platax;
    PTab callingTab;
	
	
    
    public HireStaff(Platax platax) {
		super(platax, LabelText.HIRE_STAFF);
		
		TextField givenName= new TextField (FieldText.GIVEN_NAME, 1000, this, true);
		AddressField address= new AddressField (FieldText.ADDRESS,  1500, this, true);
		
		TextField familyName= new TextField (FieldText.FAMILY_NAME, 2000, this, true);
		TextField email= new TextField (FieldText.EMAIL, 3000, this, true);
		TextField phoneNo= new TextField (FieldText.PHONE, 4000, this, true);
		NationalityField nationality= new NationalityField (FieldText.NATIONALITY,  5500, this, true);
		
		TextField natInsNo= new TextField (FieldText.NAT_INS,  6000, this, true);
		DateField dob= new DateField (FieldText.DOB,  7000, this, true);
		RadioField payFreq = new RadioField("pay", FieldText.PAY_FREQ, RadioText.PAY_PER_LIST, RadioText.PAY_PER_DEFAULT, 8000, this, true);
		MoneyField rate = new MoneyField(FieldText.PAY_RATE, 9000, this, true);
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
