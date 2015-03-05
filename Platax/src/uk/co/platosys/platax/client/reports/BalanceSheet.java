package uk.co.platosys.platax.client.reports;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.ETab;
import uk.co.platosys.platax.client.constants.TabTops;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;

public class BalanceSheet extends ETab {

	public BalanceSheet(Platax platax, GWTEnterprise enterprise) {
		super(enterprise);
		setTabHead(TabTops.BALANCE_SHEET);
	}
	public BalanceSheet() {
		super();
		setTabHead(TabTops.BALANCE_SHEET);
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}
