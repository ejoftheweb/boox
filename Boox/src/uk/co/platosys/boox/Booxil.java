package uk.co.platosys.boox;

import java.util.List;

import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.Table;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.Logger;

/**
 * Objects extending Booxil have a sysname, a name, an account and a ledger.
 * They have a table.
 * 
 * @author edward
 *
 */

public abstract class Booxil {
	/*The table will be created with sysname as a primary key*/
	public final static String SYSNAME_COLNAME= "sysname";
	public final static String SYSNAME_COLTYPE=Table.TEXT_COLUMN;
	
	/*In general you don't need to worry about these colums*/
	public final static String NAME_COLNAME="name";
	public final static String ACCOUNT_COLNAME="account";
	public final static String LEDGER_COLNAME="ledger";
	public final static String NAME_COLTYPE=Table.TEXT_COLUMN;
	public final static String ACCOUNT_COLTYPE=Table.TEXT_COLUMN;
	public final static String LEDGER_COLTYPE=Table.TEXT_COLUMN;
	private static final String[] cnames={NAME_COLNAME, ACCOUNT_COLNAME, LEDGER_COLNAME};
    private static final String[] ctypes={NAME_COLTYPE, ACCOUNT_COLTYPE, LEDGER_COLTYPE};
    
    public static Logger logger=Logger.getLogger(Boox.APPLICATION_NAME);
	protected Table table;
	protected String sysname;
	protected String name;
	protected Account account;
	protected Ledger ledger;
	protected Row row;
	public boolean initvals=false;
	protected Booxil(String sysname){
		this.sysname=sysname;
		
	}
	/**
	 * this method should be called in the subclass constructor, after the table value is initialised.
	 * @param enterprise
	 * @param clerk
	 * @param table
	 */
	protected void initvals(Enterprise enterprise, Clerk clerk, Table table){
		try{
			this.table=table;
			this.row = table.getRow(sysname);
			this.name=row.getString(NAME_COLNAME);
			this.account=Account.getAccount(enterprise, row.getString(ACCOUNT_COLNAME), clerk);
			this.ledger=Ledger.getLedger(enterprise, row.getString(LEDGER_COLNAME));
			initvals=true;
		}catch(Exception e){
			logger.log("Booxil initvals: error", e);
			initvals=false;
		}
		
	}
	public static Table createTable(String databaseName, String tableName, String[] colnames, String coltypes[]){
		try {
			String[] ncolnames=concat(cnames, colnames);
			String[] ncoltypes=concat(ctypes, coltypes);
			Table table = JDBCTable.createTable(databaseName, tableName, Booxil.SYSNAME_COLNAME, Table.TEXT_COLUMN);
			table.addColumns(ncolnames, ncoltypes);
			return table;
		} catch (PlatosysDBException e) {
			// TODO Auto-generated catch block
			logger.log("error:",e);
			return null;
		}
	}
	
	public abstract void doStuff();
	
	private static String[] concat(String[] a, String[] b) {
		   int aLen = a.length;
		   int bLen = b.length;
		   String[] c= new String[aLen+bLen];
		   System.arraycopy(a, 0, c, 0, aLen);
		   System.arraycopy(b, 0, c, aLen, bLen);
		   return c;
		}

	/**
	 * Names, which on the whole are nice easy human-understandable names,  should be unique (sysnames will be as well)
	 * This method returns the sysname if the name is already there, otherwise null.
	 * 
	 * @param name
	 * @return
	 */
    protected static String checkName(String name, Table table){
    	try{
	    	List<Row> rows = table.getRows();
	    	for(Row row:rows){
	    		if (row.getString(NAME_COLNAME).equals(name)){
	    			return row.getString(SYSNAME_COLNAME);
	    		}
	    	}
	    	return null;
    	}catch(Exception ex){
    		logger.log("Booxil-checkName: database problem", ex);
    		return null;
    	}
    }
    
	public String getSysname() {
		return sysname;
	}


	public void setSysname(String sysname) {
		this.sysname = sysname;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Account getAccount() {
		return account;
	}


	public void setAccount(Account account) {
		this.account = account;
	}


	public Ledger getLedger() {
		return ledger;
	}


	public void setLedger(Ledger ledger) {
		this.ledger = ledger;
	}
}
