package uk.co.platosys.platax.client.forms.fields;

import uk.co.platosys.platax.client.forms.AbstractForm;
import uk.co.platosys.platax.client.widgets.PIntegerBox;

public class IntegerField extends AbstractFormField<Integer> {

	protected IntegerField(String[] labelText, int position, AbstractForm parent)throws IllegalArgumentException {
		super(labelText, new PIntegerBox(),  position, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
