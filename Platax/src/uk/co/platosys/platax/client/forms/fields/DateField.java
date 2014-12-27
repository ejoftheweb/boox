package uk.co.platosys.platax.client.forms.fields;

import uk.co.platosys.platax.client.forms.AbstractForm;

import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.datepicker.client.DateBox;

public class DateField extends AbstractFormField {

	public DateField(String[] labelText,  int position,	AbstractForm parent) throws IllegalArgumentException {
		super(labelText, new DateBox(), position, parent);
		// TODO Auto-generated constructor stub
	}

}
