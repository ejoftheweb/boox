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
 *
 * 
 */

package uk.co.platosys.boox.core;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;


import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.core.exceptions.TimingException;
import uk.co.platosys.boox.money.Currency;
import uk.co.platosys.boox.money.CurrencyException;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.db.jdbc.ConnectionSource;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.ShortHash;

/**
 * "Account" strictly models a full double-entry T-account.
 * 
 * An Account, therefore, is a list of transactions, which may be either Credit or Debit transactions. 
 * An Account has a Balance, which is the sum of credits and debits with credits treated as negative. 
 * 
 * Transactions are listed in an Account with 
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
 * 
 * 
 * 
 * 
 * @author edward
 */

public class Account implements Budgetable,  Auditable {
    Money balance;
    Clerk clerk;
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
     static final String TID_COLNAME="transactionID";
     static final String CONTRA_COLNAME="contra";
     static final String AMOUNT_COLNAME="amount";
     static final String BALANCE_COLNAME="balance";
     static final String DATE_COLNAME="date";
     static final String CLERK_COLNAME="clerk";
     static final String NOTES_COLNAME="notes";
     static final String PREFIX="ac"; //ensures legality for the tablename
     static final String DELIMITER="#";
    //Journal journal;
   /**
     * package-protected constructor creates an account object by reading its metadata and current value from the SQL database
     * @param sysname the account sysname   
     * @param clerk The Clerk opening the account.
     */
   
   
   protected  Account(Enterprise enterprise, String sysname, Clerk clerk)  {
        Connection connection=null;
        try{
            sysname=sysname.toLowerCase();
            this.databaseName=enterprise.getDatabaseName();
            this.enterprise=enterprise;
            //this.journal=enterprise.getJournal();
            this.sysname=sysname;
            this.clerk=clerk;
            connection=ConnectionSource.getConnection(databaseName);
            java.sql.Statement statement = connection.createStatement();
            String sqlString = ("SELECT * FROM  "+CHART_TABLE_NAME+"  WHERE "+Chart.SYSNAME_COLNAME+" = \'"+sysname+"\'");
            logger.log(5, sqlString);
            ResultSet resultSet=statement.executeQuery(sqlString);
            if (resultSet.next()){
            	name=resultSet.getString(Chart.NAME_COLNAME);
                fullName=resultSet.getString(Chart.FULLNAME_COLNAME);
                ownerName=resultSet.getString(Chart.OWNER_COLNAME);
                ledgerName=resultSet.getString(Chart.LEDGER_COLNAME);
                logger.log("ledgerName ="+ledgerName);
                this.ledger=Ledger.getLedger(enterprise, ledgerName);
                logger.log(5, "Account "+name+" opened in ledger "+ledger.getName());
                type=resultSet.getString(Chart.TYPE_COLNAME);
                currency=Currency.getCurrency(resultSet.getString(Chart.CURRENCY_COLNAME));
                description=resultSet.getString(Chart.DESCRIPTION_COLNAME);
            }else{
                logger.log(1, "ACC-init: read chart issues: for accname:" +name);
            }
            
            balance=getBalance(enterprise, clerk);
               
            
            
             connection.close();
        }catch(Exception e){
            logger.log("Account - problem initialising "+name+" in db "+databaseName, e);
            this.description=e.getMessage();
            
        }finally{
           try{connection.close();}catch(Exception x){}
            
        }
    }
   protected  Account(Enterprise enterprise, String name, Ledger ledger, Clerk clerk)  {
       Connection connection=null;
       try{
           //sysname=sysname.toLowerCase();
           this.databaseName=enterprise.getDatabaseName();
           this.enterprise=enterprise;
           //this.journal=enterprise.getJournal();
           //this.sysname=sysname;
           this.clerk=clerk;
           String ledgerFullName = ledger.getFullName();
           this.fullName=ledgerFullName+DELIMITER+name;
           connection=ConnectionSource.getConnection(databaseName);
           java.sql.Statement statement = connection.createStatement();
           String sqlString = ("SELECT * FROM  "+CHART_TABLE_NAME+"  WHERE "+Chart.FULLNAME_COLNAME+" = \'"+fullName+"\'");
           logger.log(5, sqlString);
           ResultSet resultSet=statement.executeQuery(sqlString);
           if (resultSet.next()){
           	name=resultSet.getString(Chart.NAME_COLNAME);
               sysname=resultSet.getString(Chart.SYSNAME_COLNAME);
               ownerName=resultSet.getString(Chart.OWNER_COLNAME);
               ledgerName=resultSet.getString(Chart.LEDGER_COLNAME);
               //logger.log("ledgerName ="+ledgerName);
               this.ledger=Ledger.getLedger(enterprise, ledgerName);
               //logger.log(5, "Account "+name+" opened in ledger "+ledger.getName());
               type=resultSet.getString(Chart.TYPE_COLNAME);
               currency=Currency.getCurrency(resultSet.getString(Chart.CURRENCY_COLNAME));
               description=resultSet.getString(Chart.DESCRIPTION_COLNAME);
           }else{
               logger.log(1, "ACC-init: read chart issues: for accname:" +name);
           }
           
           balance=getBalance(enterprise, clerk);
              
           
           
            connection.close();
       }catch(Exception e){
           logger.log("Account - problem initialising "+name+" in db "+databaseName, e);
           this.description=e.getMessage();
           
       }finally{
          try{connection.close();}catch(Exception x){}
           
       }
   }
   
    /**
     * Returns a Money object representing the current balance of this account
     * @return The balancee
     */
     public Money getBalance(Enterprise enterprise, Clerk clerk) throws PermissionsException{
        if(clerk.canRead(enterprise, ledger)){
        	 Connection connection=null;
        	 try{
	        	 connection=ConnectionSource.getConnection(databaseName);
	             java.sql.Statement statement = connection.createStatement();
	            
	           //logger.log(5, "acc "+name+" bal is "+balance.toPlainString()+balance.getCurrency().getTLA());
	        	 ResultSet resultSet = statement.executeQuery("SELECT amount FROM "+sysname+" WHERE contra = \'balance\'");
	             if (resultSet.next()){
	                 BigDecimal amount=resultSet.getBigDecimal("amount");
	                 //logger.log(5, "account "+ name +" currency ="+currency);
	                 balance= new Money(currency, amount);
	                 //logger.log(5, name + "balance currency is "+balance.getCurrency().getTLA());
	             }
        	 }catch(Exception x){
        		 logger.log("exception reading balance", x);
        	 }finally{
        		 try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.log("exception thrown", e);
				}
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
            Connection connection=null;
            try{
                connection = ConnectionSource.getConnection(databaseName);
                Statement statement = connection.createStatement();
                String queryString = "SELECT balance,date FROM "+name+" WHERE date < \'"+date.toString()+"\' ORDER BY date DESC";
                ResultSet rs = statement.executeQuery(queryString);
                if(rs.next()){
                    Money bal = new Money(currency, rs.getBigDecimal("balance"));
                    connection.close();
                    return bal;
                }else{
                    connection.close();
                    return Money.zero(currency);
                }
            
            }catch(Exception e){
                try{connection.close();}catch(Exception x){}
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
             Connection connection=null;
            try{
                connection = ConnectionSource.getConnection(databaseName);
                Statement statement = connection.createStatement();
                String queryString = "SELECT "+BALANCE_COLNAME+" FROM "+name+" WHERE "+TID_COLNAME+" = "+Long.toString(transactionID);
                ResultSet rs = statement.executeQuery(queryString);
                if(rs.next()){
                    Money bal = new Money(currency, rs.getBigDecimal(BALANCE_COLNAME));
                    connection.close();
                    return bal;
                }else{
                    connection.close();
                    throw new BooxException("TransactionID "+ Long.toString(transactionID)+" not found in account "+name);
                }
            
            }catch(Exception e){
                try{connection.close();}catch(Exception x){}
                logger.log("Account-getBalance(tid) error", e);
                return null;
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
        Connection connection=null;
            try{
                connection=ConnectionSource.getConnection(databaseName);
                Statement statement=connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM "+name+ " ORDER BY transactionID ASC");
                List<AuditElement> auditList = new ArrayList<AuditElement>();
                while(rs.next()){
                    String debitAccountName;
                    String creditAccountName;
                    long transactionID = rs.getLong(TID_COLNAME);
                    if (transactionID!=0){
                    String contra = rs.getString(CONTRA_COLNAME);
                    BigDecimal amount = rs.getBigDecimal(AMOUNT_COLNAME);
                    Timestamp timestamp=rs.getTimestamp(DATE_COLNAME);
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
                connection.close();
                return auditList;
            }catch(Exception e){
                logger.log("problem generating audit list for account "+ name, e);
                try{connection.close();}catch(Exception x){}
            
                return null;
            }
       
        
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
     */
    public static boolean exists(String databaseName, String sysname){
             Connection connection=null;
            try{
                connection = ConnectionSource.getConnection(databaseName);
                Statement statement = connection.createStatement();
                String queryString = "SELECT * FROM   "+CHART_TABLE_NAME+"   WHERE "+Chart.SYSNAME_COLNAME+" = '"+sysname+"'";
                ResultSet rs = statement.executeQuery(queryString);
                if(rs.next()){
                    connection.close();
                    return true;
                }else{
                    connection.close();
                    return false;
                }

            }catch(Exception e){
                try{connection.close();}catch(Exception x){}
                logger.log("Account-exists error", e);
                return false;
            }
   }
    public static Account getAccount(Enterprise enterprise, Clerk clerk, String sysname) throws PermissionsException {
    	return new Account(enterprise, sysname, clerk);
    }
    public static Account getAccount(Enterprise enterprise, String name, Ledger ledger, Clerk clerk, Permission permission) throws PermissionsException{
    	if (!(clerk.hasPermission(enterprise, ledger, permission))){
	        
	          throw new PermissionsException("Clerk "+clerk.getName()+" does not have "+permission.getName()+ " permission  on ledger "+ledger.getName());
	      }else{
	    	  return new Account(enterprise, name, ledger, clerk);
	      }
    }
    public static Account getAccount(Enterprise enterprise, String sysname, Clerk clerk, Permission permission) throws PermissionsException {
    	 //logger.log("Boa- Clerk: "+clerk.getName());
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

       /*/if(! owner.canCreateAccounts()){
            debugLogger.log(3, "clerk "+owner.getName()+"cannot create accounts");
            throw new PermissionsException( "clerk "+owner.getName()+"cannot create accounts");
        */
       String ledgerName = ledger.getFullName();
       String fullname = ledgerName+DELIMITER+name;
       String sysname;
       if(keepname){
    	   sysname=name;
       }else{
    	   sysname = PREFIX+ShortHash.hash(fullname);
       }
        String sqlString="";
        Connection connection=null;
        try {
        	//TODO redo this logic
            name=name.toLowerCase();
            description=description.replace("\'","\'\'");
            connection = ConnectionSource.getConnection(enterprise.getDatabaseName());
            Statement statement = connection.createStatement();
             String SQLString = ("SELECT * FROM  "+CHART_TABLE_NAME+"  WHERE ("+Chart.FULLNAME_COLNAME+" = \'"+fullname+"\')");
            ResultSet rs = statement.executeQuery(SQLString);
            if (rs.next()){
              
                
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
            }
            sqlString =("INSERT INTO  "+CHART_TABLE_NAME+" ("
            		+Chart.SYSNAME_COLNAME+","
            		+Chart.NAME_COLNAME+","
            		+Chart.FULLNAME_COLNAME+","
            		+Chart.OWNER_COLNAME+"," 
            		+Chart.LEDGER_COLNAME+","
            		+Chart.CURRENCY_COLNAME+","
            		+Chart.DESCRIPTION_COLNAME+") " +
            				"VALUES(\'"+sysname+"\',\'"+name+"\',\'"+fullname+"\',\'"+owner.getName()+"\',\'"+ledgerName+"\',\'"+currency.getTLA()+"\',\'"+description+"\')");
            statement.execute(sqlString);
            
            sqlString=("CREATE TABLE "+sysname+" ("+TID_COLNAME+" integer PRIMARY KEY REFERENCES "+Journal.TABLENAME+","
            		+CONTRA_COLNAME+" text,"
            		+AMOUNT_COLNAME+" numeric(20,2),"
            		+BALANCE_COLNAME+"  numeric (20,2),"
            		+DATE_COLNAME+ " timestamp,"
            		+CLERK_COLNAME+" text,"
            		+NOTES_COLNAME+" text)");
            statement.execute(sqlString);
            sqlString=("INSERT INTO "+sysname+" ("
            +TID_COLNAME+","
            +CONTRA_COLNAME+","
            +AMOUNT_COLNAME+","
            +BALANCE_COLNAME+","
            +DATE_COLNAME+","
            +CLERK_COLNAME+","
            +NOTES_COLNAME+")" +
            		" VALUES(0, \'balance\',  0, 0, \'"+ ((new java.sql.Timestamp(new java.util.Date().getTime())).toString())+"\',\'"+owner.getName()+"\',\'Current Balance\' )");
            statement.execute(sqlString);
            
            connection.close();
            return new Account(enterprise, sysname, owner);
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}

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
	       
	       Connection connection=null;
	       try{
	        connection = ConnectionSource.getConnection(enterprise.getDatabaseName());
	        Statement statement = connection.createStatement();
	        ResultSet rs= statement.executeQuery("SELECT * from "+Chart.TABLENAME);
	        while(rs.next()){
	            Ledger ledger = new Ledger(enterprise,rs.getString(Chart.LEDGER_COLNAME));
	           Account account = new Account(enterprise, rs.getString(Chart.NAME_COLNAME), clerk);
	           if (clerk.canAudit(enterprise,ledger)){
	                accountList.add(account);
	           }
	        }
	        connection.close();
	       
	       }catch(Exception e){
	           try{connection.close();}catch(Exception p){}

	           logger.log("Account-list accounts: problem listing accounts", e);
	            
	           
	       }
	          return accountList; 
	           
	       
	   }  
}