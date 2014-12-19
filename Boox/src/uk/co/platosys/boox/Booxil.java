package uk.co.platosys.boox;

import uk.co.platosys.db.Table;

public abstract class Booxil {

	public abstract Table createTable(String[] colnames, String coltypes[]);
	public abstract void doStuff();
	
}
