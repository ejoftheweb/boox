package uk.co.platosys.boox.money;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.boox.Booxil;
import uk.co.platosys.boox.constants.Prefixes;
import uk.co.platosys.boox.constants.Tablenames;
import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.stock.Catalogue;
import uk.co.platosys.boox.stock.Product;
import uk.co.platosys.boox.trade.TaxedTransaction;
import uk.co.platosys.db.ColumnNotFoundException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.Table;
import uk.co.platosys.db.jdbc.JDBCSerialTable;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.ShortHash;

public class Bank extends Booxil {
	

private String accno;
private String sortcode;
private String iban;
private String accname;
private String addressID;
private Money balance;
private Money lowlimit;
private Money highlimit;
private Currency currency;

private static final String TABLENAME = Tablenames.BANK;

public static final String ACCNO_COLNAME="accno";
public static final String SORTCODE_COLNAME="sortcode";
public static final String IBAN_COLNAME="iban";
public static final String ACCNAME_COLNAME="accname";
public static final String ADDRESS_COLNAME="addressID";
public static final String LOWLIMIT_COLNAME="lowlimit";
public static final String HIGHLIMIT_COLNAME="highlimit";

public static final String ACCNO_COLTYPE=Table.TEXT_COLUMN;
public static final String SORTCODE_COLTYPE=Table.TEXT_COLUMN;
public static final String IBAN_COLTYPE=Table.TEXT_COLUMN;
public static final String ACCNAME_COLTYPE=Table.TEXT_COLUMN;
public static final String ADDRESS_COLTYPE=Table.TEXT_COLUMN;
public static final String LOWLIMIT_COLTYPE=Table.DECIMAL_COLUMN;
public static final String HIGHLIMIT_COLTYPE=Table.DECIMAL_COLUMN;

public static final String LEDGER_NAME="Root:XBX:Current:Assets:Cash:Bank";

private static final String[] colnames={   
									ACCNO_COLNAME,
									SORTCODE_COLNAME,
									IBAN_COLNAME, 
									ACCNAME_COLNAME,
									ADDRESS_COLNAME,
									LOWLIMIT_COLNAME, 
									HIGHLIMIT_COLNAME
									};
private static final String[] coltypes = {  
									  ACCNO_COLTYPE,
		                              SORTCODE_COLTYPE,
		                              IBAN_COLTYPE, 
		                              ACCNAME_COLTYPE,
		                              ADDRESS_COLTYPE,
		                              LOWLIMIT_COLTYPE, 
		                              HIGHLIMIT_COLTYPE
									};
	
private Bank(Enterprise enterprise, Clerk clerk, String sysname) throws PermissionsException{
	super(sysname);
	try {
		this.table=getTable(enterprise, clerk);
	} catch (PlatosysDBException e) {
		// TODO Auto-generated catch block
		logger.log("Bank constructor: error:",e);
	}
	initvals(enterprise, clerk, table);
	initvals(enterprise, clerk, row);
}

private void initvals(Enterprise enterprise, Clerk clerk, Row row) throws PermissionsException {
	try{
	 this.currency=account.getCurrency();
	 this.balance=account.getBalance(enterprise, clerk);
	 this.accno=row.getString(ACCNO_COLNAME);
	 this.sortcode=row.getString(SORTCODE_COLNAME);
	 this.iban=row.getString(IBAN_COLNAME);
	 this.accname=row.getString(ACCNAME_COLNAME);
	 this.addressID=row.getString(ADDRESS_COLNAME);
	 this.lowlimit=new Money(currency,row.getBigDecimal(LOWLIMIT_COLNAME) );
	 this.highlimit=new Money(currency,row.getBigDecimal(HIGHLIMIT_COLNAME));
	}catch(ColumnNotFoundException cnfe){
		logger.log("Fatal exception: bank table in database broken", cnfe );
	}
 }

public static boolean checkName(Enterprise enterprise, Clerk clerk, String name) throws PlatosysDBException{
	Table table=getTable(enterprise, clerk);
	if (checkName(name, table)==null){
		return true;
	}else{return false;}
}

public static Bank createBank (Enterprise enterprise, Clerk clerk, String name, String sortcode, String accno,  String iban) throws PermissionsException{
	try{
		Table table= getTable(enterprise, clerk);
		String sysname = checkName(name, table);
		if(sysname==null){
			sysname=Prefixes.BANK+ShortHash.hash(enterprise.getName()+iban);
		    table.addRow(SYSNAME_COLNAME, sysname);
		    table.amend(sysname, NAME_COLNAME, name);
		    table.amend(sysname, SORTCODE_COLNAME, sortcode);
		    table.amend(sysname, ACCNO_COLNAME, accno);
		    table.amend(sysname, IBAN_COLNAME, name);
		}
		return new Bank(enterprise, clerk, sysname);
	}catch(PlatosysDBException pdx){
		logger.log("Bank.createBank:error creating Bank", pdx);
		return null;
	}
}
protected static  Table getTable(Enterprise enterprise, Clerk clerk) throws PlatosysDBException{
	Table table;
	String databaseName=enterprise.getDatabaseName();
	if(!JDBCTable.tableExists(databaseName, TABLENAME)){
		table=createTable(databaseName, TABLENAME, colnames, coltypes);
		return table;
	}else{
		table=new JDBCTable(databaseName, TABLENAME, Booxil.SYSNAME_COLNAME);
		return table;
	}
}

public static List<Bank> getBanks(Enterprise enterprise, Clerk clerk){
	List<Bank> banks = new ArrayList<Bank>();
	try{
	Table table=getTable(enterprise, clerk);
	List<Row> rows = table.getRows();
	for (Row row:rows){
		String sysname=row.getString(SYSNAME_COLNAME);
		banks.add(new Bank(enterprise, clerk, sysname));
	}
	return banks;
	}catch(Exception x){
		logger.log("Bank.getBanks():Error getting the banks", x);
		return null;
	}
}
	@Override
	public void doStuff() {
		// TODO Auto-generated method stub

	}

	public String getAccno() {
		return accno;
	}

	

	public String getSortcode() {
		return sortcode;
	}


	public String getAccname() {
		return accname;
	}

	public void setAccname(String accname) {
		try {
			table.amend(sysname, ACCNAME_COLNAME, accname);
		} catch (PlatosysDBException e) {
			logger.log("error:",e);
		}
		this.accname = accname;
	}

	public String getAddressID() {
		return addressID;
	}

	public void setAddressID(String addressID) {
		try {
			table.amend(sysname, ADDRESS_COLNAME, addressID);
		} catch (PlatosysDBException e) {
			logger.log("error:",e);
		}
		this.addressID=addressID;
	}

	public Money getLowlimit() {
		return lowlimit;
	}

	public void setLowlimit(Money lowlimit) {
		try {
			table.amend(sysname, LOWLIMIT_COLNAME, lowlimit.amount);
		} catch (PlatosysDBException e) {
			logger.log("error:",e);
		}
		this.lowlimit = lowlimit;
	}

	public Money getHighlimit() {
		return highlimit;
	}

	public void setHighlimit(Money highlimit) {
		try {
			table.amend(sysname, HIGHLIMIT_COLNAME, highlimit.amount);
		} catch (PlatosysDBException e) {
			logger.log("error:",e);
		}
		this.highlimit = highlimit;
	}

	public String getIban() {
		return iban;
	}

	public Money getBalance() {
		return balance;
	}

	public Currency getCurrency() {
		return currency;
	}

}
