package uk.co.platosys.platax.client.forms.fields;

import uk.co.platosys.platax.client.forms.AbstractForm;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;

public class TextField extends AbstractFormField {

	public TextField(String[] labelText, int position, AbstractForm parent)throws IllegalArgumentException {
		super(labelText, new TextBox(), position, parent);
		// TODO Auto-generated constructor stub
	}

	

}
