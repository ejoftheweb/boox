package uk.co.platosys.platax.client.forms.tasks;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTabPanel;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.platax.client.forms.AbstractForm;


public abstract class BasicTask extends AbstractForm {

	@Deprecated
	public BasicTask(Platax parent, String header) {
		super(parent, header);
		
	}
	@Deprecated
	public BasicTask(Platax parent) {
		super(parent);
		setTabHead(TabTops.BASIC_TASK);
	}
	public BasicTask() {
		super();
		setTabHead(TabTops.BASIC_TASK);
	}
}
