package uk.co.platosys.boox.cash;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.db.ColumnNotFoundException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.db.Table;
import uk.co.platosys.db.jdbc.JDBCRow;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.ShortHash;

/**
 * The Cash class models devices and places used to store and handle cash.
 * In particular it is for cash-registers, but a petty-cash tin is also 
 * modeled by an instance of this class. 
 * 
 * Note that on the sales side, each instance can handle only one tax-band.  For 
 * multi-taxbands, we handle this recursively by means of child instances of Cash
 * 
 * @author edward
 *
 * <h2>Handling cash under SDE</h2>
 * <dl>
 *  <dt>Sale:</dt>
 *  <dd>Debit register, credit Sales</dd>
 *  <dt>Departments</dt>
 *  <dd>Debit register, credit department</dd>
 *  <dd>Debit department, credit deptSales untaxed amount</dd>
 *  <dd>Debit department, credit outputVat tax amount</dd>
 *  
 *  <dt>Paid-Out</dt>
 *  <dd>Debit purchases, credit register.</dd>
 *  
 *  <dt>Shorts</dt>
 *  <dd>Debit difference, credit register</dd>
 *  (charged to staff? -> check legality?)
 *  <dd>Debit cashier, credit difference</dd>
 *  
 *  <dt>Overs</dt>
 * <dd> Debit register, credit Difference</dd>
 *  
 *  
 *  
 *  <dt>Banking </dt>
 *  (cash goes from register, to bank)
 *  <dd>Debit Bank, credit register.</dd>
 *  
 *  <dt>Taxed Sales:</dt>
 * <dd> Debit register, credit Sales untax amount</dd>
 *  <dd>Debit register, credit outputVAT tax amount</dd>
 *  
 *  <dt>Paid-Out transferred to petty cash:</dt>
 * <dd> Debit petty-cash, credit Register</dd>
 *  <dd>Debit purchases, credit petty-cash</dd>
 *  
 *  <dt>Correcting/reversing over-ring</dt>
 * <dd> if not done using machine "void" function
 *  Debit Sales, credit Register</dd>
 *  
 *  <dt>Credit/Debit card sales</dt>
 *  <dd>Debit merchant, credit register</dd>
 *  <dd>Debit finance-charge, credit merchant transaction fee.</dd>
 *  </dl>
 *  
 *  <h2>A Petty Cash Tin</h2>
 *  <dt>Top-up with cash from bank</dt>
 * <dd> Debit petty-cash, credit bank</dd>
 * 
 * <dt>For each purchase</dt>
 * <dd>Debit purchases, credit petty-cash</dd>
 *  
 * <h2>Multi-department registers</h2>
 * Most modern cash-registers have multiple departments. This is useful, say, for distinguishing taxed and untaxed sales.
 * Even the smallest typically has eight. The sales for each department are credited to different sales accounts.
 * 
 * We manage this recursively.  For the root machine, the actual machine, we record the number of departments.
 * A separate instance of Cash is created for each department, each with its own sales account and tax-band. It records its parent column.
 * A null in the parent column indicates that the machine has no parent, therefore is a parent machine.
 * 
 *
 */
public class Cash {
	//the registers table
	public static final String TABLE_NAME="bx_cash";
	public static final String NAME_COLNAME="name";
	public static final String SYSNAME_COLNAME="sysname";
	public static final String LEDGER_COLNAME="ledger";
	public static final String ACCOUNT_COLNAME="account";
	public static final String DIFF_ACCOUNT_COLNAME="diff_account";
	public static final String PO_ACCOUNT_COLNAME="po_account";
	public static final String SALES_ACCOUNT_COLNAME="sales_account";
	public static final String PARENT_COLNAME="parent";
	public static final String DEPTS_COLNAME="depts";
	public static final String TAXBAND_COLNAME="taxband";
	
	
	public static final String MODEL_COLNAME="model";
	public static final String REPORTNO_COLNAME="report";
	public static final String GT_COLNAME="runningtotal";
	public static final String FLOAT_COLNAME="float";
	
	public static final String SYSNAME_PREFIX="h";//configurable
	public static final String CASH_LEDGER_NAME="Root:XBX:Current:Assets:Cash:Registers"; //make configurable from props file?
	public static final String DIFF_ACCOUNT_NAME="bx_Difference"; //make configurable from props file?
	public static final String PO_ACCOUNT_NAME="bx_PaidOuts"; //make configurable from props file?
	
	private static Logger logger = Logger.getLogger("boox");
	Table table;
	String name;
	String model;
	String sysname;
	Ledger ledger;
	Account account;
	Account diffAccount;
	Account poAccount;
	Account salesAccount;
	int reportNo;
	int depts;
	String parentSysname;
	int taxband;
	Money gt;
	Money floatbal;
	Cash[] children;
	
	private Cash(Enterprise enterprise, Clerk clerk, String sysname) throws PlatosysDBException {
		table=JDBCTable.getTable(enterprise.getDatabaseName(), TABLE_NAME, SYSNAME_COLNAME);
		 Row row;
		try {
			row = getRow(enterprise,sysname);
			this.name=row.getString(NAME_COLNAME);
			this.sysname=row.getString(SYSNAME_COLNAME);
			this.ledger=Ledger.getLedger(enterprise, row.getString(LEDGER_COLNAME));
			this.account=Account.getAccount(enterprise, row.getString(ACCOUNT_COLNAME), clerk);
			this.diffAccount=Account.getAccount(enterprise, row.getString(DIFF_ACCOUNT_COLNAME), clerk);
			this.poAccount=Account.getAccount(enterprise, row.getString(PO_ACCOUNT_COLNAME), clerk);
			this.salesAccount=Account.getAccount(enterprise, row.getString(SALES_ACCOUNT_COLNAME), clerk);
			this.reportNo=row.getInt(REPORTNO_COLNAME);
			this.depts=row.getInt(DEPTS_COLNAME);
			this.taxband=row.getInt(TAXBAND_COLNAME);
			this.gt=new Money(enterprise.getDefaultCurrency(), row.getBigDecimal(GT_COLNAME));
			this.floatbal=new Money(enterprise.getDefaultCurrency(),row.getBigDecimal(FLOAT_COLNAME));
		 	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log("error:",e);
		}
	}
	private Cash(Enterprise enterprise, Clerk clerk, Row row) throws ClassCastException, PermissionsException, ColumnNotFoundException, PlatosysDBException{
		table=JDBCTable.getTable(enterprise.getDatabaseName(), TABLE_NAME, SYSNAME_COLNAME);
		this.name=row.getString(NAME_COLNAME);
		this.sysname=row.getString(SYSNAME_COLNAME);
		this.ledger=Ledger.getLedger(enterprise, row.getString(LEDGER_COLNAME));
		this.account=Account.getAccount(enterprise, row.getString(ACCOUNT_COLNAME), clerk);
		this.diffAccount=Account.getAccount(enterprise, row.getString(DIFF_ACCOUNT_COLNAME), clerk);
		this.reportNo=row.getInt(REPORTNO_COLNAME);
		this.gt=new Money(enterprise.getDefaultCurrency(), row.getBigDecimal(GT_COLNAME));
		this.floatbal=new Money(enterprise.getDefaultCurrency(),row.getBigDecimal(FLOAT_COLNAME));
	}

	/**
	 * Use this method to set up a machine on the system for the first time
	 * @param enterprise
	 * @param clerk
	 * @param cashmcsLedger
	 * @param name
	 * @return
	 * @throws PlatosysDBException
	 * @throws PermissionsException
	 * @throws BooxException
	 */
	public static Cash addCashmc(Enterprise enterprise,  Clerk clerk, Ledger cashmcsLedger,  String name) throws PlatosysDBException, PermissionsException, BooxException{
		  JDBCTable cashmcsTable=null;
		   if(!JDBCTable.tableExists(enterprise.getDatabaseName(), TABLE_NAME)){
			  logger.log("Cash: creating cashmcsTable");
			  try {
				cashmcsTable = JDBCTable.createTable(enterprise.getDatabaseName(), TABLE_NAME, SYSNAME_COLNAME, Table.TEXT_COLUMN);
				cashmcsTable.addColumn(NAME_COLNAME, Table.TEXT_COLUMN);
				cashmcsTable.addColumn(MODEL_COLNAME, Table.TEXT_COLUMN);
				cashmcsTable.addColumn(LEDGER_COLNAME, Table.TEXT_COLUMN);
				cashmcsTable.addColumn(ACCOUNT_COLNAME, Table.TEXT_COLUMN);
				cashmcsTable.addColumn(DIFF_ACCOUNT_COLNAME, Table.TEXT_COLUMN);
				cashmcsTable.addColumn(PO_ACCOUNT_COLNAME, Table.TEXT_COLUMN);
				cashmcsTable.addColumn(SALES_ACCOUNT_COLNAME, Table.TEXT_COLUMN);
				cashmcsTable.addColumn(DEPTS_COLNAME, Table.INTEGER_COLUMN);//not sure we actually need this?
				cashmcsTable.addColumn(PARENT_COLNAME, Table.TEXT_COLUMN);
				cashmcsTable.addColumn(REPORTNO_COLNAME, Table.INTEGER_COLUMN);
				cashmcsTable.addColumn(GT_COLNAME, Table.DECIMAL_COLUMN);
				cashmcsTable.addColumn(FLOAT_COLNAME, Table.DECIMAL_COLUMN);
			} catch (PlatosysDBException e) {
				logger.log("Cash.addCashMc: exception thrown creating cashmc table", e);
				throw new PlatosysDBException ("problem creating cashmc", e);
			}
		  }else{
			  logger.log("cash: mccs table exists, opening it");
			  cashmcsTable=JDBCTable.getTable(enterprise.getDatabaseName(), TABLE_NAME, SYSNAME_COLNAME);
		  }
		  //name: should be unique.  
		  if(cashmcsTable.rowExists(NAME_COLNAME, name)){
			  logger.log("Cash: machine called "+name+" already exists");
			  //then a customer of this name already exists so we return it
			  JDBCRow row;
			try {
				row = cashmcsTable.getRow(NAME_COLNAME, name);
			} catch (RowNotFoundException e) {
				logger.log("Customer.CC: RNF exception thrown", e);
				//eh? the row exists but exception thrown??
				throw new PlatosysDBException("Faulty CashMc Database: machine row not found", e);
			}
			  try {
				return new Cash(enterprise, clerk, row.getString(SYSNAME_COLNAME));
			} catch (Exception e) {
				
				logger.log("exception thrown", e);
				//col not found exception? Table is buggad. 
				throw new PlatosysDBException("CashMc Database Fault: sysname col not found", e);
			}
		  }else{
			  //
			  
			  String sysname=SYSNAME_PREFIX+ShortHash.hash(name+enterprise.getName());
			  cashmcsTable.addRow(SYSNAME_COLNAME, sysname);
			  cashmcsTable.amend(sysname, NAME_COLNAME, name);
			  //machine needs a Ledger: 
			  Ledger ledger = Ledger.createLedger(enterprise, sysname, cashmcsLedger, enterprise.getDefaultCurrency(), clerk, true);
			  cashmcsTable.amend(sysname, LEDGER_COLNAME, ledger.getFullName());
			  //And an Account:
			  Account account = Account.createAccount(enterprise, sysname,  clerk, ledger, enterprise.getDefaultCurrency(), name, true);
			  cashmcsTable.amend(sysname, ACCOUNT_COLNAME, account.getSysname());
			  //And a Difference Account:
			  Account diffAccount = Account.createAccount(enterprise, sysname,  clerk, ledger, enterprise.getDefaultCurrency(), DIFF_ACCOUNT_NAME, false);
			  cashmcsTable.amend(sysname,DIFF_ACCOUNT_COLNAME, diffAccount.getSysname());
			  //And a paid-outs Account:
				  Account poAccount = Account.createAccount(enterprise, sysname,  clerk, ledger, enterprise.getDefaultCurrency(), PO_ACCOUNT_NAME, false);
				  cashmcsTable.amend(sysname,PO_ACCOUNT_COLNAME, poAccount.getSysname());	  
			  Cash cash = new Cash(enterprise, clerk, sysname);
			  logger.log("Ccash: created cashmc"+cash.getName());
			  return cash;
		  }
	}
	 
	/**
	 * Use this method to retrieve an instance of Cash
	 * @param enterprise
	 * @param clerk
	 * @param sysname
	 * @return
	 * @throws PlatosysDBException
	 */
	public static Cash getCash(Enterprise enterprise, Clerk clerk, String sysname) throws PlatosysDBException{
		  return new Cash(enterprise, clerk, sysname);
	  }
	/**
	 * for multi-department machines, i.e. most
	 * @return
	 * @throws PlatosysDBException 
	 * @throws ColumnNotFoundException 
	 * @throws PermissionsException 
	 * @throws ClassCastException 
	 */
	  public ArrayList<Cash> getChildren(Enterprise enterprise, Clerk clerk) throws ClassCastException, PermissionsException, ColumnNotFoundException, PlatosysDBException{
		  ArrayList<Cash> children=new ArrayList<Cash>();
		  if (depts==0){
			  children.add(this);
		  }else{
			  List<Row> rows = table.getRows(PARENT_COLNAME, sysname);
			  for(Row row:rows){
				  children.add(new Cash(enterprise, clerk, row));
			  }
		  }
		  return children;
	  }
	  /**
	   * Add a sub-department to this Cash
	   * @param name
	   * @param taxBand
	   * @return
	   */
	  public Cash addDept(String name, int taxBand){
		  return null;
	  }
	  public String getName(){
		 return name;
	 }
	 static Row getRow(Enterprise enterprise, String sysname)throws PlatosysDBException, RowNotFoundException{
		 JDBCTable cashmcsTable=null;
		 cashmcsTable=JDBCTable.getTable(enterprise.getDatabaseName(), TABLE_NAME, SYSNAME_COLNAME);
		 return cashmcsTable.getRow(sysname);
	 }
	 static List <Row> getRows(Enterprise enterprise) throws PlatosysDBException{
		 JDBCTable cashmcsTable=null;
		 cashmcsTable=JDBCTable.getTable(enterprise.getDatabaseName(), TABLE_NAME, SYSNAME_COLNAME);
		 return cashmcsTable.getRows();
	 }
	public static List<Cash> getCashes(Enterprise enterprise, Clerk clerk) throws PlatosysDBException, ClassCastException, PermissionsException, ColumnNotFoundException{
		 List<Row> rows = getRows(enterprise);
		 ArrayList<Cash> cashes=new ArrayList<Cash>();
		 for(Row row:rows){
			 if (row.getString(PARENT_COLNAME)==null){
				 cashes.add(new Cash(enterprise, clerk, row));
			 }
		 }
		 return cashes;
	 }
	public String getSysname() {
		return sysname;
	}
	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
	public Ledger getLedger() {
		return ledger;
	}
	public void setLedger(Ledger ledger) {
		try{
			table.amend(sysname, LEDGER_COLNAME, ledger.getFullName());
			this.ledger = ledger;
		}catch(Exception x){
			logger.log("CashSL problem", x);
		}
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		try{
			table.amend(sysname, ACCOUNT_COLNAME, account.getSysname());
			this.account = account;
		}catch(Exception x){
			logger.log("CashSA problem", x);
		}
	}
	public int getReportNo() {
		return reportNo;
	}
	public void setReportNo(int reportNo) {
		try{
			table.amend(sysname, REPORTNO_COLNAME, reportNo);
			this.reportNo = reportNo;
		}catch(Exception x){
			logger.log("CashSRN problem", x);
		}
	}
	public Money getGt() {
		return gt;
	}
	public void setGt(Money gt) {
		try{
			table.amend(sysname, GT_COLNAME, gt.getAmount());
			this.gt = gt;
		}catch(Exception x){
			logger.log("CashSGT problem", x);
		}
	}
	public Money getFloatbal() {
		return floatbal;
	}
	public void setFloatbal(Money floatbal) {
		try{
			table.amend(sysname, FLOAT_COLNAME, floatbal.getAmount());
			this.floatbal =floatbal;
		}catch(Exception x){
			logger.log("CashSFB problem", x);
		}
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		try{
			table.amend(sysname, MODEL_COLNAME, model);
			this.model = model;
		}catch(Exception x){
			logger.log("CashSM problem", x);
		}
	}
}
