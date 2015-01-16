package uk.co.platosys.pws.fieldsets;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import uk.co.platosys.pws.Form;
import uk.co.platosys.pws.inputfields.TickBox;

public class TickBoxField extends AbstractFormField<Boolean> {
TickBox tickBox;
	public TickBoxField(String[] labelText, int position, Form parent, boolean required) throws IllegalArgumentException {
		super(labelText, position, parent, required);
		tickBox=new TickBox();
		setWidget(tickBox);
		start();
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return tickBox.addValueChangeHandler(handler);
	}

	

}
