package uk.co.platosys.platax.client.forms.fields;

import uk.co.platosys.platax.client.forms.AbstractForm;
import uk.co.platosys.platax.client.widgets.PTextBox;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;

public class TextField extends AbstractFormField<String> {

	public TextField(String[] labelText, int position, AbstractForm parent)throws IllegalArgumentException {
		super(labelText, new PTextBox(), position, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	

}
