package uk.co.platosys.boox.trade;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.co.platosys.boox.Body;
import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Directory;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.money.CurrencyException;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.db.ColumnNotFoundException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.db.Table;
import uk.co.platosys.db.jdbc.JDBCRow;
import uk.co.platosys.db.jdbc.JDBCSerialTable;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.ShortHash;
/**
 * Customer represents a customer, obvs. We distinguish between trade and private customers.
 * Trade customers' details are added to the Directory, which is an installation-wide directory
 * of business-information. 
 * 
 * 
 * @author edward
 *
 */

public class Customer extends CounterParty{
	public static final String TABLE_NAME="bx_customers";
	public static final String ID_COLNAME="id";
	public static final String NAME_COLNAME="name";
	public static final String SYSNAME_COLNAME="sysname";
	public static final String LEDGER_COLNAME="ledger";
	public static final String ACCOUNT_COLNAME="account";
	public static final String INVOICE_ADDRESS_COLNAME="invoice_add";
	public static final String DELIVERY_ADDRESS_COLNAME="delivery_add";
	public static final String CONTACT_ADDRESS_COLNAME="contact_add";
	public static final String LEGAL_ADDRESS_COLNAME="legal_add";
	public static final String SALES_CONTACT_COLNAME="sales_contact";
	public static final String ACCOUNTS_CONTACT_COLNAME="accounts_contact";
	public static final String TERMS_COLNAME="terms";
	public static final String TODO_COLNAME="todo";
	public static final String SYSNAME_PREFIX="c";//configurable
	public static final String CUSTOMERS_LEDGER_NAME="Root:XBX:Current:Assets:Debtors:Customers"; //make configurable from props file?
	public static final int SELECTION_ALL=0;
	
	private long customerID;
	private String name;
	private String sysname;
	private Account account;
	private Ledger ledger;
	private Terms terms;
	/*private Xaddress invoice_add;
	private Xaddress delivery_add;
	private Xaddress contact_add;
	private Xaddress legal_add;
	private Xcontact sales_contact;
	private Xcontact accounts_contact;*/
	private static Logger logger = Logger.getLogger(Boox.APPLICATION_NAME);
	private Customer(Enterprise enterprise, Clerk clerk, long customerID){
		logger.log("Customer-init: "+customerID);
		 try {
			
			JDBCSerialTable customersTable=JDBCSerialTable.openTable(enterprise.getDatabaseName(), TABLE_NAME, ID_COLNAME);
			JDBCRow row = customersTable.getRow(customerID);
			this.customerID=customerID;
			this.name=row.getString(NAME_COLNAME);
			this.sysname=row.getString(SYSNAME_COLNAME);
			this.ledger=Ledger.getLedger(enterprise, row.getString(LEDGER_COLNAME));
			this.account=Account.getAccount(enterprise,  row.getString(ACCOUNT_COLNAME), clerk);
			this.terms=Terms.getTerms(enterprise, row.getString(TERMS_COLNAME));
		} catch (Exception e) {
			logger.log("Customer-init/id: exception thrown", e);
		}
	}
	private Customer(Enterprise enterprise, Clerk clerk, String sysname){
		 try {
			JDBCSerialTable customersTable=JDBCSerialTable.openTable(enterprise.getDatabaseName(), TABLE_NAME, ID_COLNAME);
			JDBCRow row = customersTable.getRow(SYSNAME_COLNAME, sysname);
			this.customerID=row.getLong(ID_COLNAME);
			this.name=row.getString(NAME_COLNAME);
			putInfo(Body.NAME, name);
			this.sysname=sysname;
			this.ledger=Ledger.getLedger(enterprise, row.getString(LEDGER_COLNAME));
			this.account=Account.getAccount(enterprise,  row.getString(ACCOUNT_COLNAME),clerk);
			this.terms=Terms.getTerms(enterprise, row.getString(TERMS_COLNAME));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log("Customer-init/sysname: exception thrown", e);
		}
	}
	private Customer (Enterprise enterprise, Clerk clerk, long customerID, String name, String sysname, String ledgerName, String accountName) throws PermissionsException{
		this.customerID=customerID;
		this.name=name;
		putInfo(Body.NAME, name);
		this.sysname=sysname;
		this.ledger=Ledger.getLedger(enterprise,ledgerName);
		this.account=Account.getAccount(enterprise,  accountName, clerk);
		this.terms=Terms.getDefaultTerms(enterprise);
	}
	@Override
	public Ledger getLedger() {
		return this.ledger;
	}

	@Override
	public Account getAccount() {
		return this.account;
	}

	@Override
	public String getName() {
		return this.name;
	}
   
	public String getSysname() {
		return sysname;
	}
    public Money getBalance(Enterprise enterprise, Clerk clerk) throws PermissionsException{
    	logger.log("Customer: getting the balance for ledger");
    	return this.ledger.getBalance(enterprise, clerk);
    }
    public Money getOverdueBalance(Enterprise enterprise, Clerk clerk) throws PermissionsException, PlatosysDBException, CurrencyException{
    	Money balance =  Money.zero();
    	List<Invoice> invoices = Invoice.getInvoices(enterprise, clerk, this, Invoice.SELECTION_OVERDUE);
    	Iterator<Invoice> it = invoices.iterator();
    	while(it.hasNext()){
    		balance.debit(it.next().getBalance(clerk));
    	}
    	return balance;
    }
    
  
    public Money getDisputedBalance(Enterprise enterprise, Clerk clerk) throws PermissionsException, PlatosysDBException, CurrencyException{
    	Money balance =  Money.zero();
    	List<Invoice> invoices = Invoice.getInvoices(enterprise, clerk, this, Invoice.SELECTION_DISPUTED);
    	Iterator<Invoice> it = invoices.iterator();
    	while(it.hasNext()){
    		balance.debit(it.next().getBalance(clerk));
    	}
    	return balance;
    }
    public Money getSales(Enterprise enterprise, Clerk clerk) throws PermissionsException{
    	return this.ledger.getBalance(enterprise, clerk);
    }
	/**
	 * Use to create a customer.
	 * 
	 * @param enterprise - the enterprise
	 * @param ledger - the enterprise's customers (sales, or City) ledger
	 * @param clerk - the clerk doing the creating
	 * @param customerName - the name of the customer to create
	 * @param isPrivate - whether or not this is a private customer (ie DP rules apply). 
	 * @return
	 * @throws PlatosysDBException
	 * @throws BooxException 
	 * @throws PermissionsException 
	 */
  public static Customer createCustomer(Enterprise enterprise,  Clerk clerk, Ledger customersLedger,  String customerName, boolean isTrade) throws PlatosysDBException, PermissionsException, BooxException{
	  JDBCSerialTable customersTable=null;
	  
	  if(!JDBCTable.tableExists(enterprise.getDatabaseName(), TABLE_NAME)){
		  logger.log("CustomerCC: creating customersTable");
		  try {
			customersTable = JDBCSerialTable.createTable(enterprise.getDatabaseName(), TABLE_NAME, ID_COLNAME);
			customersTable.addColumn(NAME_COLNAME, Table.TEXT_COLUMN);
			customersTable.addColumn(SYSNAME_COLNAME, Table.TEXT_COLUMN);
			customersTable.addColumn(LEDGER_COLNAME, Table.TEXT_COLUMN);
			customersTable.addColumn(ACCOUNT_COLNAME, Table.TEXT_COLUMN);
			customersTable.addColumn(INVOICE_ADDRESS_COLNAME, Table.TEXT_COLUMN);
			customersTable.addColumn(DELIVERY_ADDRESS_COLNAME, Table.TEXT_COLUMN);
			customersTable.addColumn(CONTACT_ADDRESS_COLNAME, Table.TEXT_COLUMN);
			customersTable.addColumn(LEGAL_ADDRESS_COLNAME, Table.TEXT_COLUMN);
			customersTable.addColumn(SALES_CONTACT_COLNAME, Table.TEXT_COLUMN);
			customersTable.addColumn(TERMS_COLNAME, Table.TEXT_COLUMN);
			customersTable.addColumn(TODO_COLNAME, Table.BOOLEAN_COLUMN);
			customersTable.addColumn(ACCOUNTS_CONTACT_COLNAME, Table.TEXT_COLUMN);
		} catch (PlatosysDBException e) {
			logger.log("CustomerCC: exception thrown creating customers table", e);
			throw new PlatosysDBException ("problem creating customer", e);
		}
	  }else{
		  logger.log("CustomerCC: customersTable exists, opening it");
		  customersTable=JDBCSerialTable.openTable(enterprise.getDatabaseName(), TABLE_NAME, ID_COLNAME);
		  
	  }
	  //name: should be unique.  
	  if(customersTable.rowExists(NAME_COLNAME, customerName)){
		  logger.log("CustomerCC: customer called "+customerName+" already exists");
		  //then a customer of this name already exists so we return it
		  JDBCRow row;
		try {
			row = customersTable.getRow(NAME_COLNAME, customerName);
		} catch (RowNotFoundException e) {
			logger.log("Customer.CC: RNF exception thrown", e);
			//eh? the row exists but exception thrown??
			throw new PlatosysDBException("Faulty Customer Database: customer row not found", e);
		}
		  try {
			return new Customer(enterprise, clerk, row.getLong(ID_COLNAME));
		} catch (Exception e) {
			
			logger.log("exception thrown", e);
			//col not found exception? Table is buggad. 
			throw new PlatosysDBException("Customer Database Fault: customer ID col not found", e);
		}
	  }else{
		  //
		  //Create the customer:
		  //CUSTOMER NUMBER=
		  //logger.log("CustomerCC: - creating new customer:" +customerName);
		  long id = customersTable.addSerialRow(ID_COLNAME, NAME_COLNAME,customerName);
		 // logger.log("CustomerCC: new customer allocated ID "+Long.toString(id));
		  String sysname=SYSNAME_PREFIX+ShortHash.hash(Long.toString(id)+customerName+enterprise.getName());
		 customersTable.amend(id, SYSNAME_COLNAME, sysname);
		  //Customer needs a Ledger: 
		  Ledger ledger = Ledger.createLedger(enterprise, sysname, customersLedger, enterprise.getDefaultCurrency(), clerk, true);
		  customersTable.amend(id, LEDGER_COLNAME, ledger.getFullName());
		  //And an Account:
		  Account account = Account.createAccount(enterprise, sysname,  clerk, ledger, enterprise.getDefaultCurrency(), customerName, true);
		  customersTable.amend(id, ACCOUNT_COLNAME, account.getSysname());
		  customersTable.amend(id, TERMS_COLNAME, Terms.DEFAULT_TERMS);
		  Customer customer = new Customer(enterprise, clerk, id);
		  logger.log("CustomerCC: created customer"+customer.getName());
		  if (isTrade){Directory.addBody(customer, false);}
		  return customer;
	  }
	  
  }
  public static Customer getCustomer(Enterprise enterprise, Clerk clerk, long customerID){
	  return new Customer(enterprise, clerk, customerID);
  }
  public static Customer getCustomer(Enterprise enterprise, Clerk clerk, String sysname){
	  return new Customer(enterprise, clerk, sysname);
  }
  
  /**
   * returns a list of customers.
   * note that as of 141204 the selection parameter does nothing yet
   * @param enterprise
   * @param clerk
   * @param selection
   * @return
   * @throws PermissionsException
   */
  public static List<Customer> getCustomers(Enterprise enterprise, Clerk clerk, int selection) throws PermissionsException{
  	List<Customer> customers = new ArrayList<Customer>();
  	try{
	  	JDBCTable customersTable=JDBCTable.getTable(enterprise.getDatabaseName(), Customer.TABLE_NAME);
	  	for (Row row:customersTable.getRows()){
	  		long customerID=row.getLong(ID_COLNAME);
	  		String name=row.getString(NAME_COLNAME);
	  		String sysname=row.getString(SYSNAME_COLNAME);
	  		String ledgerName=row.getString(LEDGER_COLNAME);
	  		String accountName=row.getString(ACCOUNT_COLNAME);
	  		Customer customer = new Customer(enterprise, clerk,customerID, name, sysname, ledgerName, accountName);
	  		logger.log("CgCs: adding to list customer with sysname: "+customer.getSysname());
	  		customers.add(customer);
	  	}
  	}catch(PlatosysDBException pdx){
  		return customers;//no customers table, return an empty List. 
  	}catch(PermissionsException px){
  		throw px;
  	}catch(Exception ex){
  		logger.log("Customer: problem with the customers list for enterprise "+enterprise.getName(), ex);
  	}
  	logger.log("CgCs returning customer list with "+customers.size()+" customers");
  	return customers;
  }
public Terms getTerms() {
	return terms;
}
public void setTerms(Terms terms) {
	this.terms = terms;
}

  
}
