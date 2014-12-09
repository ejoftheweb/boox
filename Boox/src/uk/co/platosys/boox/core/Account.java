/*
 *     
 *  uk.co.platosys.boox.core.Account
 * 
    Copyright (C) 2008  Edward Barrow

    This class is free software; you can redistribute it and/or
    modify it under the terms of the GNU  General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this program; if not, write to the Free
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * 
 * for more information contact edward.barrow@platosys.co.uk
 
 * Account.java
 *
 * Created on 24 March 2007, 14:02
 * 6DEC14: rewritten to use only PlatosysDB, no direct SQL calls made.
 * 
 */

package uk.co.platosys.boox.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.naming.OperationNotSupportedException;

import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.core.exceptions.TimingException;
import uk.co.platosys.boox.money.Currency;
import uk.co.platosys.boox.money.CurrencyException;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.db.ColumnNotFoundException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.Table;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.db.RowNotFoundException;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.ShortHash;

/**
 * "Account" strictly models a full double-entry T-account.
 * 
 * An Account, therefore, is a list of transactions, which may be either Credit or Debit transactions. 
 * An Account has a Balance, which is the sum of credits and debits with credits treated as negative. 
 * 
 * the balance is recorded in a row with the transaction id of 0. 
 * 
 * Transactions are listed with a TransactionID, unique with system scope. The account listing
 * also identifies the contra account - the account on which the other side of the transaction is recorded.
 * There is also space for recording the clerk who posted the transaction, the date/time of posting, and
 * a free-form note explaining it. 
 * 
 * With appropriate access to the underlying database, accounts can be read directly. Access should therefore
 * be restricted because this approach bypasses the Boox permissions system.
 * <h2>Account metadata: the Chart<h2>
 * Account metadata is all recorded in the Chart table (the Chart of Accounts). 
 * 
 * <h2>Account Naming Conventions</h2>
 *  Accounts have three names.
 *  - the sysname is unique with enterprise scope and identifies the account's table in the database. It is a shorthash of
 *  -
 *  - the fullName is also unique with enterprise scope and is a concatenation of the account name and the fully-qualified name of its parent ledger.
 *   the name(display name) is unique with ledger scope.
 *  Ledger names are colon-delimited; the account name (accounts are always leaf nodes in the ledger tree) is identified by a #.
 *  
 *  Thus: General:CurrentLiabilities:Tax is a ledger, while General:CurrentLiabilities#Tax would be an account. 
 * 
 * <h2>Changing Ledgers<h2>
 * Ledger transactions are not recorded in the database and ledgers do not therefore keep a live record of their balance;
 *  a call to Ledger.getBalance()  makes the ledger calculate its balance from scratch.
 * It is therefore possible to move an account from on ledger to another without affecting the overall
 * balance of the system; it is merely a matter of changing the metadata about that account in the Chart
 * table. It will, however, change the account fullName. 
 * The likely usecase for changing ledgers is adding more enterprise-specific detail to the accounting structure
 * than is provided in the initial modules, which, although sector-specific, are inherently generic. 
 * 
 * Ledger changing is not yet implemented (Nov2014)
 * 
 * 
 * 
 * 
 * @author edward
 */

public class Account implements Budgetable,  Auditable {
    Money balance;
    Clerk clerk;
    JDBCTable table;
    //account metadata as held in chart of accounts:
    String sysname; // the system name, used to name the table in the database. A shorthash of the fully-qualified name, unique with enterprise scope
    String name;//the display name;  Short, generally user-friendly, not necessarily uniqe
    String fullName;//the fully-qualified colon-delimited name, unique with enterprise scope. 
    String ownerName;//held as string
    String ledgerName;//held as string
    private String type;
    Ledger ledger; //the ledger to which this account belongs.
    String permissions;
    Currency currency;
    String description;//A description of the account and its functions in the system
    boolean locked;
    static  Logger logger=Logger.getLogger("boox");
    String databaseName;
    Enterprise enterprise; //the journal to which this account is attached;
    ISODate date = new ISODate();
    TreeMap<Date, Money> budget=new TreeMap<Date, Money>();
     static final String CHART_TABLE_NAME=Chart.TABLENAME;//to rename later.
     static final String TID_COLNAME="transactionid";
     static final String CONTRA_COLNAME="contra";
     static final String AMOUNT_COLNAME="amount";
     static final String BALANCE_COLNAME="balance";
     static final String DATE_COLNAME="date";
     static final String CLERK_COLNAME="clerk";
     static final String NOTES_COLNAME="notes";
     static final String LINE_COLNAME="line";
     static final String PREFIX="ac"; //ensures legality for the tablename
     static final String DELIMITER="#";
     static final String BASIC_TYPE="basic";
    //Journal journal;
   /**
     * package-protected constructor creates an account object by reading its metadata and current value from the SQL database
     * @param sysname the account sysname   
     * @param clerk The Clerk opening the account.
     */
   
   
   protected  Account(Enterprise enterprise, String sysname1, Clerk clerk)  {
        try{
            this.sysname=sysname1.toLowerCase();
            this.databaseName=enterprise.getDatabaseName();
            this.enterprise=enterprise;
            this.clerk=clerk;
            this.table=JDBCTable.getTable(databaseName, sysname, TID_COLNAME, Table.INTEGER_COLUMN);
            JDBCTable chartTable =JDBCTable.getTable(databaseName, CHART_TABLE_NAME);
            Row row=null;
            try{
               row= chartTable.getRow(Chart.SYSNAME_COLNAME, sysname);
            }catch(RowNotFoundException rnfe){
            	logger.log(1, "ACC-init:  account:" +name+ " not found in chart of accounts");
            	throw new BooxException("Account "+sysname1+ " not found");
            }
            name=row.getString(Chart.NAME_COLNAME);
            fullName=row.getString(Chart.FULLNAME_COLNAME);
            ownerName=row.getString(Chart.OWNER_COLNAME);
            ledgerName=row.getString(Chart.LEDGER_COLNAME);
            this.ledger=Ledger.getLedger(enterprise, ledgerName);
            type=row.getString(Chart.TYPE_COLNAME);
            currency=Currency.getCurrency(row.getString(Chart.CURRENCY_COLNAME));
            description=row.getString(Chart.DESCRIPTION_COLNAME);
            logger.log(2, "Account "+name+" opened in ledger "+ledger.getName());
            try{
            	logger.log(2, "now getting balance for account "+name);
            	balance=getBalance(enterprise, clerk);
            }catch(Exception x){
            	logger.log("Account issue getting balance for account"+name, x);
            }
       }catch(Exception e){
            logger.log("Account - problem initialising "+name+" in db "+databaseName, e);
            this.description=e.getMessage();
       }
    }
  
    /**
     * Returns a Money object representing the current balance of this account
     * @return The balance
     */
     public Money getBalance(Enterprise enterprise, Clerk clerk) throws PermissionsException{
    	 logger.log("AccountGB started");
        if(clerk.canRead(enterprise, ledger)){
        	logger.log("AccountGB - read permission is ok");
        	 try{ 
        		 if(table!=null){
        			 logger.log("AccountGB - table exists");
        			 Row row = table.getRow(0);
        			 logger.log("AccountGB - got the zero row");
        			 BigDecimal amount=row.getBigDecimal(AMOUNT_COLNAME);
	                 logger.log(5, "account "+ name +" currency ="+currency);
	                 balance= new Money(currency, amount);
	                 logger.log(5, name + "balance currency is "+balance.getCurrency().getTLA());
	                 logger.log(5, "acc "+name+" bal is "+balance.getCurrency().getTLA()+":"+balance.toPlainString());
	 	         }else{
	 	        	 logger.log("issue getting the balance on account "+fullName);
	 	        	 throw new BooxException("Balance line not found in account "+sysname);
	 	         }
        	 }catch(Exception x){
        		 logger.log("exception reading balance", x);
        	 }
             return balance;
        }else{
            logger.log("Permissions Exception thrown on account "+name);
            throw new PermissionsException("Clerk "+clerk.getName()+ " does not have read permissions on ledger: "+ledger.getName()+
                    " so cannot read the balance of account "+getName());
        }
        
    }
     /**
     * Returns a Money object representing the  balance of this account immediately before the 
      * given date. 
      * This requires the canAudit permission;
     * @return The balance
     */
     public Money getBalance(Enterprise enterprise, Clerk clerk, ISODate date) throws PermissionsException{
        if(clerk.canAudit(enterprise, getLedger())){
        	Money audBal=null;
            try{
               String queryString = "SELECT balance,date FROM "+name+" WHERE date < \'"+date.toString()+"\' ORDER BY date DESC";
               List<Row> rows = table.query(queryString);
               for(Row row:rows){
            	    BigDecimal amount=row.getBigDecimal(AMOUNT_COLNAME);
	                 logger.log(5, "account "+ name +" currency ="+currency);
	                 audBal= new Money(currency, amount); 
               }
               return audBal;
            }catch(Exception e){
                 logger.log("Account-getBalance(date) error", e);
                return null;
            }
        }else{
            throw new PermissionsException("Clerk "+clerk.getName()+ " does not have audit permissions on ledger: "+getLedger().getName()+
                    " so cannot audit the balance history of account "+getName());
        }
        
    }
     /**
     * Returns a Money object representing the balance of this account after the given
      * transaction ID.
      * requires the canAudit permission;
     * @return The balance
     */
     public Money getBalance(Enterprise enterprise, Clerk clerk, long transactionID) throws PermissionsException, BooxException{
        if(clerk.canAudit(enterprise, getLedger())){
            try{
                Row row = table.getRow(transactionID);
            	Money bal = new Money(currency, row.getBigDecimal(BALANCE_COLNAME));
                return bal;
               
            
            }catch(Exception e){
            	logger.log("Account-getBalance(tid) error", e);
                throw new BooxException("AccountGB(tid):Exception", e);
                
            }
        }else{
            throw new PermissionsException("Clerk "+clerk.getName()+ " does not have audit permissions on ledger: "+getLedger().getName()+
                    " so cannot read the transaction balance history of account "+getName());
        }
        
    }
    protected final Money getBalance(){
        return balance;
    }
    /**
     * Returns a List of transactions on this account
     * Requires the canAudit permission
     * @return a List of Transactions
     */
    public List<AuditElement> audit(Enterprise enterprise, Clerk clerk) throws PermissionsException{
        if (!clerk.canAudit(enterprise, getLedger())){
            throw new PermissionsException("Clerk "+clerk.getName()+" does not have audit permissions on ledger:" +getLedger().getCurrencyName()+
                    " so cannot audit account "+getName());
        }
            try{
                List<AuditElement> auditList = new ArrayList<AuditElement>();
                for (Row rs:table.getRows()){
                    String debitAccountName;
                    String creditAccountName;
                    long transactionID = rs.getLong(TID_COLNAME);
                    if (transactionID!=0){
                    String contra = rs.getString(CONTRA_COLNAME);
                    BigDecimal amount = rs.getBigDecimal(AMOUNT_COLNAME);
                    Date timestamp=rs.getTimeStamp(DATE_COLNAME);
                    String clerkName = rs.getString(CLERK_COLNAME);
                    String notes=rs.getString(NOTES_COLNAME);
                    int sign=amount.signum();
                    if(sign!=-1){
                        debitAccountName=this.name;
                        creditAccountName=contra;
                    }else{
                        debitAccountName=contra;
                        creditAccountName=this.name;
                    }
                    Clerk transactionClerk = new Clerk(clerkName);
                    Money money = new Money(currency, amount);
                    Transaction transaction = new Transaction(transactionClerk, enterprise, money,creditAccountName,debitAccountName,notes, timestamp, transactionID);
                    ISODate transactionDate = new ISODate(timestamp.getTime());
                    if (transactionDate.after(date)){date=transactionDate;}
                    	auditList.add(transaction);
                    }
                }
                  return auditList;
            }catch(Exception e){
                logger.log("problem generating audit list for account "+ name, e);
               
            
                return null;
            }
       
        
    }
    /**
     * As of Nov2014, this is not yet supported and a call will automatically throw an ONSE.
     * 
     * @param ledger
     * @param clerk
     * @return
     * @throws PermissionsException
     * @throws OperationNotSupportedException
     */
    public boolean setLedger(Ledger ledger, Clerk clerk)throws PermissionsException, OperationNotSupportedException{
    	//TODO implement Account.setLedger()
    	throw new OperationNotSupportedException();
    }
    public String getName(){
        return name;
    }

    public String getSysname(){
        return sysname;
    }
    public Currency getCurrency(){
        return currency;
    }
    public String getFullName(){
        //logger.log(5,"Account "+name+" returning fullname = "+fullName);
                
        return fullName;
    }
    public String getOwnerName(){
        return ownerName;
    }
    public String getLedgerName(){
        return ledgerName;
    }
    public Ledger getLedger(){
        if (ledger!=null){
            return ledger;
        }else{
            try{
                this.ledger = new Ledger(enterprise, ledgerName);
                return ledger;
            }catch(Exception bex){
                logger.log(5, "problem getting ledger for account "+name);
                return null;
            }
        }
    }
    public String getDescription(){
        return description;
    }
    public boolean close(){
        return true;
    }
    /**
     * this is currently broken; do not use it.
     * 
     * @param accountName
     * @return
     */
    public static String getFullName(String accountName){
        /*FIX ME
        Connection connection =null;
        try{
            connection=ConnectionSource.getConnection(Books.DATABASE_NAME)
        }
         * */
        return accountName;
    }

    /**
     * @return the type
     */
    public //held as string
    String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Checks for the existence of an account with this sysname
     * @param databaseName
     * @param sysname
     * @return
     * @throws PlatosysDBException 
     */
    public static boolean exists(Enterprise enterprise, String sysname) throws BooxException{
            try{
            	Table table= JDBCTable.getTable(enterprise.getDatabaseName(), Chart.TABLENAME);
                Row row = table.getRow(Chart.SYSNAME_COLNAME, sysname);
    		}catch(RowNotFoundException rnfe){
            	   return false;
            }catch(PlatosysDBException pdbe){
            	throw new BooxException("AccountExists error", pdbe);
            }
            return true;
    }
    
    public static Account getAccount(Enterprise enterprise, String sysname, Clerk clerk ) throws PermissionsException {
    	logger.log("Account getting account "+sysname);
    	return new Account(enterprise, sysname, clerk);
    }
    
    public static Account getAccount(Enterprise enterprise, String sysname, Clerk clerk, Permission permission) throws PermissionsException {
    	     Account account = new Account(enterprise, sysname, clerk);
    	      Ledger ledger=account.getLedger();

    	      if (clerk.hasPermission(enterprise, ledger, permission)){
    	          return account;
    	      }else{
    	          throw new PermissionsException("Clerk "+clerk.getName()+" does not have "+permission.getName()+ " permission  on ledger "+ledger.getName());
    	      }
    	    }
    /**
     * Creates an Account. The account created will have its own sysname.
     * 
     * @param enterprise
     * @param name
     * @param owner
     * @param ledger
     * @param currency
     * @param description
     * @return
     * @throws BooxException
     * @throws PermissionsException
     */
    public static Account createAccount(Enterprise enterprise, String name, Clerk owner, Ledger ledger, Currency currency, String description) throws BooxException, PermissionsException{
    	return createAccount(enterprise, name, owner, ledger, currency, description, false);
    }
    
    /**
     * USE THIS METHOD WITH CARE: better to use the previous one. 
     * This method is called from within BOOX to create Accounts having the sysname of their parent object - eg invoices, customers or products.
     * 
     * @param enterprise
     * @param name will be the account's sysname, if keepname is true. Must be a valid SQL tablename.
     * @param owner
     * @param ledger
     * @param currency
     * @param description
     * @param keepname if true, the account sysname=name; if false, a new sysname will be generated.
     * @return
     * @throws BooxException
     * @throws PermissionsException
     */
    public static Account createAccount(Enterprise enterprise, String name, Clerk owner, Ledger ledger, Currency currency, String description, boolean keepname) throws BooxException, PermissionsException{

      
       String ledgerName = ledger.getFullName();
       String fullname = ledgerName+DELIMITER+name;
       String sysname;
       if(keepname){
    	   sysname=name;
       }else{
    	   sysname = PREFIX+ShortHash.hash(fullname);
       }
        String sqlString="";
        try {
        	//TODO redo this logic
            name=name.toLowerCase();
            description=description.replace("\'","\'\'");
            Table chartTable = Chart.getTable(enterprise);
            Row rs;
            //first check to see whether this account exists.
            try{
            	rs = chartTable.getRow(Chart.FULLNAME_COLNAME, fullname);
	            if((rs.getString(Chart.OWNER_COLNAME)).equals(owner.getName())){
	                return new Account(enterprise, sysname, owner);
	            }else{
	            	if (!(rs.getString(Chart.OWNER_COLNAME)).equals(owner.getName())){
			            logger.log("Account "+name+" exists with different owner: renaming as "+owner.getName()+"_"+name);
			            name=owner.getName()+"_"+name;
			            fullname = ledgerName+DELIMITER+name;
			            sysname = PREFIX+ShortHash.hash(fullname);
	            	}
	            }
	        }catch (RowNotFoundException rnfe){
            	//which is fine, so we do nothing. 
            }
	        try{
	        	//put the info into the chart of accounts.	
	            String cols[]=Chart.COLS;	
	           /*String[] COLS={SYSNAME_COLNAME,
						NAME_COLNAME, 
						FULLNAME_COLNAME, 
						OWNER_COLNAME, 
						LEDGER_COLNAME, 
						CURRENCY_COLNAME, 
						TYPE_COLNAME, 
						DESCRIPTION_COLNAME};*/
	            
	            String [] vals={sysname,
	            		        name,
	            		        fullname,
	            		        owner.getName(),
	            		        ledgerName,
	            		        currency.getTLA(),
	            		        BASIC_TYPE,
	            		        description};
	             chartTable.addRow(cols, vals);
	           
	            //now create the account table.
	             Table accountTable = JDBCTable.createForeignKeyTable(enterprise.getDatabaseName(), sysname,TID_COLNAME, Journal.TABLENAME);
	            accountTable.addColumn(CONTRA_COLNAME, Table.TEXT_COLUMN);
	            accountTable.addColumn(AMOUNT_COLNAME, Table.DECIMAL_COLUMN);
	            accountTable.addColumn(BALANCE_COLNAME, Table.DECIMAL_COLUMN);
	            accountTable.addColumn(DATE_COLNAME, Table.TIMESTAMP_COLUMN);
	            accountTable.addColumn(CLERK_COLNAME, Table.TEXT_COLUMN);
	            accountTable.addColumn(NOTES_COLNAME, Table.TEXT_COLUMN);
	            accountTable.addColumn(LINE_COLNAME, Table.INTEGER_COLUMN);
	            //
	            accountTable.addRow(TID_COLNAME, 0);
	            accountTable.amend(0, CONTRA_COLNAME, "balance");
	            accountTable.amend(0, AMOUNT_COLNAME, new BigDecimal(0));
	            accountTable.amend(0, BALANCE_COLNAME, new BigDecimal(0));
	            accountTable.amend(0, DATE_COLNAME, new ISODate());
	            accountTable.amend(0, CLERK_COLNAME,  owner.getName());
	            accountTable.amend(0, NOTES_COLNAME, "balance");
	            	
	            return new Account(enterprise, sysname, owner);
            
        }catch(Exception e){
           logger.log("error creating account: \n"+sqlString, e);
           return null;
        }
	    }catch(Exception e){
	           logger.log("error creating account: \n"+sqlString, e);
	           return null;
	    }
    }
 

	@Override
	public ISODate getDate() {
		// TODO Auto-generated method stub
		return date;
	}

	@Override
	public Money getBudget(Clerk clerk, Date date) throws PermissionsException,TimingException {
		if(date.getTime()<budget.firstKey().getTime()){throw new TimingException("Account"+name+" has no budget before "+date.toGMTString());}
		if(date.getTime()>budget.lastKey().getTime()){throw new TimingException("Account"+name+" has no budget after "+date.toGMTString());}
		Date before;
		Date after;
		 if (budget.get(date)!=null){
			 return budget.get(date);
		 }else{
			 return null;
		 }
			/* Iterator<Date> dit = budget.keySet().iterator();
			 while (dit.hasNext()){
				 if (dit<date){before=dit;}
			 }
				 
			 } 
		 }*/
	}

	@Override
	public void setBudget(Clerk clerk, Money money, Date date)
			throws PermissionsException, CurrencyException, TimingException {
		// TODO Auto-generated method stub
	}

	@Override
	public void setInterpolationMode(Clerk clerk, int interpolationMode)
			throws PermissionsException {
		// TODO Auto-generated method stub
	}

	@Override
	public int getInterpolationMode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Money getVariance(Clerk clerk, Date date)
			throws PermissionsException, TimingException {
		// TODO Auto-generated method stub
		return null;
	}


	 /**
	    * 
	    * @param databaseName
	    * @param clerk
	    * @return a List of those accounts  in the given Ledger readable by clerk.
	    */
	   public static List<Account> getAccounts(Enterprise enterprise,  Clerk clerk){
	       List<Account> accountList=new ArrayList<Account>();
	      try{
	       List<Row> rows = Chart.accountRows(enterprise);
	       for (Row rs:rows){
	    	  Ledger ledger = new Ledger(enterprise,rs.getString(Chart.LEDGER_COLNAME));
	           Account account = new Account(enterprise, rs.getString(Chart.NAME_COLNAME), clerk);
	           if (clerk.canAudit(enterprise,ledger)){
	                accountList.add(account);
	           }
	        }
	       }catch(Exception e){
	            logger.log("Account-list accounts: problem listing accounts", e);
	       }
	          return accountList; 
	   }  
	   /**
	    * Returns the account sysname given its fullname
	    * @param accountFullname
	    * @return
	    */
	   public static String getAccountSysname(Enterprise enterprise, String accountFullname){
		    Row row=null;
		    String taxAccountSysname=null;
		    try{
		   	 JDBCTable chartTable =JDBCTable.getTable(enterprise.getDatabaseName(), Chart.TABLENAME);
		      row= chartTable.getRow(Chart.FULLNAME_COLNAME, accountFullname);
		       taxAccountSysname=row.getString(Chart.SYSNAME_COLNAME);
		    }catch(RowNotFoundException rnfe){
		    	logger.log("TT-:  account:" +accountFullname+ " not found in chart of accounts", rnfe);
		    	//throw new BooxException("Account "+accountFullname+ " not found");
		    } catch (ClassCastException e) {
		   	 logger.log( "TT-:  account:ccde", e);
			} catch (ColumnNotFoundException e) {
				 logger.log( "TT-:  account:cnfe", e);
			} catch (PlatosysDBException e) {
				 logger.log( "TT-:  account:psdbe", e);
			}
		    logger.log("Account-gAS: "+accountFullname+" = "+taxAccountSysname);
		    return taxAccountSysname;
	    }
}