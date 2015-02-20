package uk.co.platosys.pws.widgets;



import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HasValue;

/**
 *  Extends FileUpload to implement HasValue<String>.
 *  The value is the filename of the file to be uploaded. 
 * @author edward
 *
 */
public class ValueFileUpload extends FileUpload implements 	HasValue<String> {
	boolean valueChangeHandlerInitialised=false;
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		 if (!valueChangeHandlerInitialised) {
				addChangeHandler(new ChangeHandler() {
					public void onChange(ChangeEvent event) {
						//Window.alert("list value changed to "+getValue());
						ValueChangeEvent.fire(ValueFileUpload.this, getValue());
				}
			});
			valueChangeHandlerInitialised = true;
		}
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public String getValue() {
		return getFilename();
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		// TODO Auto-generated method stub
		
	}

}
