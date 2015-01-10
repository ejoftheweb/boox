package uk.co.platosys.pws.fieldsets;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import uk.co.platosys.platax.client.forms.AbstractForm;
import uk.co.platosys.pws.inputfields.PIntegerBox;

public class IntegerField extends AbstractFormField<Integer> {

	protected IntegerField(String[] labelText, int position, AbstractForm parent, boolean required)throws IllegalArgumentException {
		super(labelText, new PIntegerBox(),  position, parent, required);
		// TODO Auto-generated constructor stub
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
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Integer> handler) {
		// TODO Auto-generated method stub
		return null;
	}

}
