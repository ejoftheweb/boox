package uk.co.platosys.platax.client.forms.fields;

import uk.co.platosys.platax.client.forms.AbstractForm;
import uk.co.platosys.platax.client.widgets.MoneyBox;

public class MoneyField extends AbstractFormField {

	protected MoneyField(String[] labelText, int position, AbstractForm parent)	throws IllegalArgumentException {
		super(labelText, new MoneyBox(), position, parent);
		// TODO Auto-generated constructor stub
	}

}
