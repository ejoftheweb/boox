package uk.co.platosys.pws.fieldsets;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.inputfields.PIntegerBox;

public class IntegerField extends AbstractFormField<Integer> {
PIntegerBox integerBox;
	protected IntegerField(String[] labelText, int position, Form parent, boolean required)throws IllegalArgumentException {
		super(labelText,   position, parent, required);
		integerBox=new PIntegerBox();
		setWidget(integerBox);
		start();
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Integer> handler) {
		return integerBox.addValueChangeHandler(handler);
	}

}
