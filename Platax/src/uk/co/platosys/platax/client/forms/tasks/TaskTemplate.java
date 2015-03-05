package uk.co.platosys.platax.client.forms.tasks;



import com.google.gwt.core.client.GWT;
import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.constants.StringText;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.platax.client.services.ProductService;
import uk.co.platosys.platax.client.services.ProductServiceAsync;
import uk.co.platosys.pws.fieldsets.SubmitField;

public class TaskTemplate extends TTab {
	
	final ProductServiceAsync productService = (ProductServiceAsync) GWT.create(ProductService.class);
	//widgets
	PTab callingTab;
	
	
    
    public TaskTemplate() {
		super();
		setTabHead(TabTops.BASIC_TASK);
		//Add form fields (from uk.co.platosys.pws.fieldsets)  here.
		
		//Add callbacks to populate lists etc
		
		SubmitField sub= new SubmitField(12000, this);
	    //Add handlers as needed
		
	 	
		
		setTitle("Task Title");
		setSubTitle("Information About the Task");
		render();
    }

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}
