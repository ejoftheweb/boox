package uk.co.platosys.boox.stock;

import uk.co.platosys.boox.core.Ledger;

public abstract class ItemCategory {

	public abstract Ledger getLedger();
	public abstract String getName();
	public abstract String getID();
}
