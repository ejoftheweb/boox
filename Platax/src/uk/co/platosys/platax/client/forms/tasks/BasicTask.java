package uk.co.platosys.platax.client.forms.tasks;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.platax.client.forms.AbstractForm;
import uk.co.platosys.platax.client.widgets.PlataxTabPanel;


public abstract class BasicTask extends AbstractForm {

	public BasicTask(Platax parent, String header) {
		super(parent, header);
		
	}
	public BasicTask(Platax parent) {
		super(parent);
		setTabHead(TabTops.BASIC_TASK);
	}
	public BasicTask() {
		super();
		setTabHead(TabTops.BASIC_TASK);
	}
}
