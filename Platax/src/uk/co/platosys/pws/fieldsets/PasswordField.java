package uk.co.platosys.pws.fieldsets;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.inputfields.PPasswordBox;
import uk.co.platosys.pws.inputfields.PTextBox;

public class PasswordField extends AbstractFormField<String> implements HasValueChangeHandlers<String>{
   PPasswordBox textBox=new PPasswordBox();
	public PasswordField(String[] labelText, int position, Form parent, boolean required)throws IllegalArgumentException {
		super(labelText, position, parent, required);
		setWidget(textBox);
		start();
	}
    
	@Override
	public boolean validate() {
		if(validationRegex==null){
			return true;
		}else{
			return (getValue().matches(validationRegex));
		}
	}
	@Override
	public void fireEvent(GwtEvent<?> event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return textBox.addValueChangeHandler(handler);
	}
	public void setValue(String value){
		textBox.setValue(value);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		// TODO Auto-generated method stub
		
	}

}
