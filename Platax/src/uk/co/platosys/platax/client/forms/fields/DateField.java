package uk.co.platosys.platax.client.forms.fields;

import java.util.Date;

import uk.co.platosys.platax.client.forms.AbstractForm;
import uk.co.platosys.platax.client.widgets.PDateBox;

import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.datepicker.client.DateBox;

public class DateField extends AbstractFormField<Date> {

	public DateField(String[] labelText,  int position,	AbstractForm parent) throws IllegalArgumentException {
		super(labelText, new PDateBox(), position, parent);
		
	}

	@Override
	public boolean validate() {
		
		return true;
	}

}
