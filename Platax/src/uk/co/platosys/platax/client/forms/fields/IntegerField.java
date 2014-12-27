package uk.co.platosys.platax.client.forms.fields;

import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.ValueBox;
import com.ibm.icu.text.NumberFormat;

import uk.co.platosys.platax.client.forms.AbstractForm;

public class IntegerField extends AbstractFormField {

	protected IntegerField(String[] labelText, int position, AbstractForm parent)throws IllegalArgumentException {
		super(labelText, new IntegerBox(),  position, parent);
		// TODO Auto-generated constructor stub
	}

}
