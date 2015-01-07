package uk.co.platosys.platax.client.forms.fields;

import uk.co.platosys.platax.client.forms.AbstractForm;
import uk.co.platosys.platax.client.widgets.MoneyBox;
import uk.co.platosys.platax.shared.boox.GWTMoney;

public class MoneyField extends AbstractFormField<GWTMoney> {

	protected MoneyField(String[] labelText, int position, AbstractForm parent)	throws IllegalArgumentException {
		super(labelText, new MoneyBox(), position, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean validate() {
		return true;
	}

}
