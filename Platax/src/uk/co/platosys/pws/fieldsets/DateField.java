package uk.co.platosys.pws.fieldsets;

import java.util.Date;

import uk.co.platosys.platax.client.forms.AbstractForm;
import uk.co.platosys.pws.inputfields.PDateBox;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class DateField extends AbstractFormField<Date> {
PDateBox dateBox;
	public DateField(String[] labelText,  int position,	AbstractForm parent, boolean required) throws IllegalArgumentException {
		super(labelText,  position, parent, required);
		dateBox=new PDateBox();
		setWidget(dateBox);
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
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Date> handler) {
		return dateBox.addValueChangeHandler(handler);
	}

}
