package uk.co.platosys.platax.client.forms.tasks;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.shared.boox.GWTTask;

public class VATReturn extends TTab {

	public VATReturn(Platax platax, GWTTask task) {
		super();
		//data comes from task description.
		setTitle("VAT Return for "+task.getEnterprise().getName());
		setSubTitle("Do your VAT Return");
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
	}

}
