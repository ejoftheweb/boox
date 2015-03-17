package uk.co.platosys.boox.staff;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.platosys.boox.constants.Prefixes;
import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.money.Money;
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
 * The Staff class models a member of staff.
 * It will be used eventually for the payroll module but for the time 
 * being it is used to keep a track of employees and roles.
 * Also commission and wages.
 * 
 * 
 * @author edward
 *
 *
 *<h2>Handling staff payments under SDE</h2>
 * <dl>
 *  <dt>Wage</dt>
 *  <dd>Debit wages, credit Staff</dd>
 *  [with multi departments: credit sale1, sale2, sale3 etc]
 *  
 *  <dt>Commission</dt>
 *  <dd>Debit commission, credit staff</dd>
 *  
 *  
 */
public class Staff {
	public static final int PAY_HOURLY=1;
	public static final int PAY_WEEKLY=4;
	public static final int PAY_ANNUAL=8;
	
	public static final String TABLE_NAME="bx_staff";
	public static final String ID_COLNAME="id";
	public static final String NAME_COLNAME="name";
	public static final String FULLNAME_COLNAME="fullname";
	public static final String SYSNAME_COLNAME="sysname";
	public static final String LEDGER_COLNAME="ledger";
	public static final String ACCOUNT_COLNAME="account";
	public static final String HOME_ADDRESS_COLNAME="home_add";
	public static final String EMAIL_COLNAME="email";
	public static final String PHONE_COLNAME="phone";
	public static final String NINO_COLNAME="nat_ins";
	public static final String DOB_COLNAME="dob";
	public static final String DOCFILE_COLNAME="docfile";
	//public static final String TERMS_COLNAME="terms";
	//public static final String TODO_COLNAME="todo";
	public static final String SYSNAME_PREFIX=Prefixes.STAFF;
	public static final String STAFF_LEDGER_NAME="Root:XBX:Current:Liabilities:Creditors:Staff"; //make configurable from props file?
	public static final int SELECTION_ALL=0;
	private static Logger logger = Logger.getLogger(Boox.APPLICATION_NAME);
	private String name;
	private String sysname;
	private Account account;
	private Ledger ledger;
	
	private String givenName;
	private String familyName;
	private String email;
	private String nino;
	private String phone;
	private String address;
	private File docFile;
	private Date dob;
	private Money rate;
	private int payFreq=PAY_HOURLY;
	
	private Staff(Enterprise enterprise, Clerk clerk, String sysname){
		 try {
			JDBCSerialTable customersTable=JDBCSerialTable.openTable(enterprise.getDatabaseName(), TABLE_NAME, ID_COLNAME);
			JDBCRow row = customersTable.getRow(SYSNAME_COLNAME, sysname);
			this.name=row.getString(NAME_COLNAME);
			this.sysname=sysname;
			this.ledger=Ledger.getLedger(enterprise, row.getString(LEDGER_COLNAME));
			this.account=Account.getAccount(enterprise,  row.getString(ACCOUNT_COLNAME),clerk);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log("Customer-init/sysname: exception thrown", e);
		}
	}
	/**
	 * Use to create an employee.
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
  public static Staff createStaff(Enterprise enterprise,  Clerk clerk, String staffName)throws PlatosysDBException, PermissionsException, BooxException{
	  JDBCSerialTable staffsTable=null;
	  
	  if(!JDBCTable.tableExists(enterprise.getDatabaseName(), TABLE_NAME)){
		  logger.log("StaffCC: creating staffsTable");
		  try {
			staffsTable = JDBCSerialTable.createTable(enterprise.getDatabaseName(), TABLE_NAME, ID_COLNAME);
			staffsTable.addColumn(NAME_COLNAME, Table.TEXT_COLUMN);
			staffsTable.addColumn(FULLNAME_COLNAME, Table.TEXT_COLUMN);
			staffsTable.addColumn(SYSNAME_COLNAME, Table.TEXT_COLUMN);
			staffsTable.addColumn(LEDGER_COLNAME, Table.TEXT_COLUMN);
			staffsTable.addColumn(ACCOUNT_COLNAME, Table.TEXT_COLUMN);
			staffsTable.addColumn(HOME_ADDRESS_COLNAME, Table.TEXT_COLUMN);
			staffsTable.addColumn(EMAIL_COLNAME, Table.TEXT_COLUMN);
			staffsTable.addColumn(PHONE_COLNAME, Table.TEXT_COLUMN);
			staffsTable.addColumn(NINO_COLNAME, Table.TEXT_COLUMN);
			staffsTable.addColumn(DOB_COLNAME, Table.DATE_COLUMN);
			staffsTable.addColumn(DOCFILE_COLNAME, Table.TEXT_COLUMN);
			//staffsTable.addColumn(TODO_COLNAME, Table.BOOLEAN_COLUMN);
			//staffsTable.addColumn(ACCOUNTS_CONTACT_COLNAME, Table.TEXT_COLUMN);
		} catch (PlatosysDBException e) {
			logger.log("StaffCC: exception thrown creating staffs table", e);
			throw new PlatosysDBException ("problem creating staff", e);
		}
	  }else{
		  logger.log("StaffCC: staffsTable exists, opening it");
		  staffsTable=JDBCSerialTable.openTable(enterprise.getDatabaseName(), TABLE_NAME, ID_COLNAME);
		  
	  }
	  //name: should be unique.  
	  if(staffsTable.rowExists(NAME_COLNAME, staffName)){
		  logger.log("StaffCC: staff called "+staffName+" already exists");
		  //then a staff of this name already exists so we return it
		  JDBCRow row;
		try {
			row = staffsTable.getRow(NAME_COLNAME, staffName);
		} catch (RowNotFoundException e) {
			logger.log("Staff.CC: RNF exception thrown", e);
			//eh? the row exists but exception thrown??
			throw new PlatosysDBException("Faulty Staff Database: staff row not found", e);
		}
		  try {
			return new Staff(enterprise, clerk, row.getString(SYSNAME_COLNAME));
		} catch (Exception e) {
			
			logger.log("exception thrown", e);
			//col not found exception? Table is buggad. 
			throw new PlatosysDBException("Staff Database Fault: staff ID col not found", e);
		}
	  }else{
		  //
		  //Create the staff:
		 //logger.log("StaffCC: - creating new staff:" +staffName);
		  long id = staffsTable.addSerialRow(ID_COLNAME, NAME_COLNAME,staffName);
		 // logger.log("StaffCC: new staff allocated ID "+Long.toString(id));
		  String sysname=SYSNAME_PREFIX+ShortHash.hash(Long.toString(id)+staffName+enterprise.getName());
		 staffsTable.amend(id, SYSNAME_COLNAME, sysname);
		  //Staff needs a Ledger: 
		  Ledger ledger = Ledger.createLedger(enterprise, sysname, Ledger.getLedger(enterprise, STAFF_LEDGER_NAME), enterprise.getDefaultCurrency(), clerk, true);
		  staffsTable.amend(id, LEDGER_COLNAME, ledger.getFullName());
		  //And an Account:
		  Account account = Account.createAccount(enterprise, sysname,  clerk, ledger, enterprise.getDefaultCurrency(), staffName, true);
		  staffsTable.amend(id, ACCOUNT_COLNAME, account.getSysname());
		  Staff staff = new Staff(enterprise, clerk, sysname);
		  logger.log("StaffCC: created staff"+staff.getName());
		 
		  return staff;
	  }
	  
  }
  public static Staff getStaff(Enterprise enterprise, Clerk clerk, String sysname){
	  return new Staff(enterprise, clerk, sysname);
  }
  
  
  /**
   * returns a list of staffs.
   * @param enterprise
   * @param clerk
   * @param selection
   * @return
   * @throws PermissionsException
   */
  public static List<Staff> getStaffs(Enterprise enterprise, Clerk clerk, int selection) throws PermissionsException{

	 	List<Staff> staffs = new ArrayList<Staff>();
	  	try{
		  	JDBCTable staffsTable=JDBCTable.getTable(enterprise.getDatabaseName(), Staff.TABLE_NAME);
		  	for (Row row:staffsTable.getRows()){
		  		String sysname=row.getString(SYSNAME_COLNAME);
		  		Staff staff = new Staff(enterprise, clerk, sysname);
		  		logger.log("SgSs: adding to list staff with sysname: "+staff.getSysname());
		  		staffs.add(staff);
		  	}
	  	}catch(PlatosysDBException pdx){
	  		return staffs;//no staffs table, return an empty List. 
	  	}catch(Exception ex){
	  		logger.log("Staff: problem with the staffs list for enterprise "+enterprise.getName(), ex);
	  	}
	  	logger.log("SgSs returning staff list with "+staffs.size()+" staffs");
	  	return staffs;
	  }
	  
	  
	  
	  
	  public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSysname() {
		return sysname;
	}
	public void setSysname(String sysname) {
		this.sysname = sysname;
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
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNino() {
		return nino;
	}
	public void setNino(String nino) {
		this.nino = nino;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public File getDocFile() {
		return docFile;
	}
	public void setDocFile(File docFile) {
		this.docFile = docFile;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public Money getRate() {
		return rate;
	}
	public void setRate(Money rate) {
		this.rate = rate;
	}
	public int getPayFreq() {
		return payFreq;
	}
	public void setPayFreq(int payFreq) {
		this.payFreq = payFreq;
	}
}
