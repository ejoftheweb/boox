package uk.co.platosys.pws.fieldsets;

import uk.co.platosys.platax.client.forms.AbstractForm;
import uk.co.platosys.pws.inputfields.MoneyBox;
import uk.co.platosys.pws.values.GWTMoney;

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
