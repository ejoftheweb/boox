package uk.co.platosys.platax.client.forms.tasks;



import com.google.gwt.core.client.GWT;
import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.pws.fieldsets.SubmitField;

public class Payroll extends TTab {
	
	final ProductServiceAsync productService = (ProductServiceAsync) GWT.create(ProductService.class);
	//widgets
	Platax platax;
    PTab callingTab;
	
	
    
    public Payroll(Platax platax) {
		super();
		
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
