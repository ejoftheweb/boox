package uk.co.platosys.pws.fieldsets;

import java.util.List;
import java.util.Map;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;

import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.inputfields.FileUploadBox;
import uk.co.platosys.pws.inputfields.ListValueField;
import uk.co.platosys.pws.values.ValuePair;


/**
 * A field that shows a file upload box. There are no extraneous labels and the upload is fired when the form submit button is fired. 
 * 
 * @author edward
 *
 */
public class FileUploadField extends AbstractFormField<String> implements HasValueChangeHandlers<String> {
FileUploadBox box;
/**
 * 
 * @param labelText 
 * @param position
 * @param parent
 * @param required
 * @throws IllegalArgumentException
 */
	public FileUploadField(String[] labelText, int position, Form parent,	boolean required) throws IllegalArgumentException {
		super(labelText, position, parent, required);
		try{
		this.box=new FileUploadBox();
		setWidget(box);
		start();
		}catch(Exception x){
			Window.alert("FUP "+x.getMessage());
		}
	}
	
	
	

	@Override
 	public boolean validate() {
		  if(required){
			  if(box.getValue().equals("")){
				  return false;
			  }else{
				  return true;
			  }
		  }else{
			  return true;
		  }
	}
	@Override
	public void fireEvent(GwtEvent<?> event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return box.addValueChangeHandler(handler);
	}
	

}
