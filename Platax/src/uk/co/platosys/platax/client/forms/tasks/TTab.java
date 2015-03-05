package uk.co.platosys.platax.client.forms.tasks;

import uk.co.platosys.platax.client.components.EFTab;
import uk.co.platosys.platax.client.constants.TabTops;



public abstract class TTab extends EFTab {

	
	public TTab() {
		super();
		setTabHead(TabTops.BASIC_TASK);
	}
}
