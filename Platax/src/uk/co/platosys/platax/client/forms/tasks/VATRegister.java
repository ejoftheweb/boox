package uk.co.platosys.platax.client.forms.tasks;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.forms.AbstractForm;
import uk.co.platosys.platax.shared.boox.GWTTask;

public class VATRegister extends AbstractForm {

	public VATRegister(Platax platax, GWTTask task) {
		super(platax);
		setTitle("Register for VAT");
		setSubTitle("Enter your VAT registration details");
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}
