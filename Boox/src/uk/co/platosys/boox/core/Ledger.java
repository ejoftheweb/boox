/*
 * Ledger.java
 *  * 
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
 
 * Ledger.java
 *
 * Created on 24 March 2007, 14:02
 *
 * 
 *

 * 
 */

package uk.co.platosys.boox.core;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.postgresql.util.PSQLException;


import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.core.exceptions.TimingException;
import uk.co.platosys.boox.money.Currency;
import uk.co.platosys.boox.money.CurrencyException;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.db.jdbc.ConnectionSource;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;

/**
 * Boox arranges Accounts in a hierarchy of Ledgers, like a file-system with its
 * directories, sub-directories and files. The root Ledger of a Boox system is the
 * General Ledger, and this is the only Ledger which can have accounts in multiple 
 * Currencies. 
 * 
 * A Ledger can therefore contain Ledgers and Accounts, and it knows its own balance,
 * which is the sum of the balances of the Ledgers and Accounts it contains.
 * 
 * Ledgers are the unit by which Boox manages its permissions system.
 *
 * @author edward
 */
public final class Ledger implements Budgetable, Auditable {
	static final String TABLENAME="bx_ledgers";
	static final String NAME_COLNAME="name";
	static final String FULLNAME_COLNAME="fullname";
	static final String PARENT_COLNAME="parent";
	static final String	CURRENCY_COLNAME="currency";
	static final String	OWNER_COLNAME="owner";
	static final String ISPRIVATE_COLNAME="isprivate";
	public static final String DELIMITER=":";
	public static final String ROOT_LEDGER_NAME="Root";
	public static final String DEFAULT_CURRENCY_LEDGER_NAME="XBX";
    private String name;
    private String fullName;
    private String parentName;
    private Clerk owner;
    private String databaseName;
    private Currency currency=Currency.DEFAULT_CURRENCY;
    private static Logger logger=Logger.getLogger("boox");
    private boolean isPrivate;
    private boolean initialised=false;
    private Enterprise enterprise;
    
    public Ledger (Enterprise enterprise, String fullname) throws BooxException{
    	if (enterprise==null){throw new BooxException("L-initL: enterprise cannot be null" );}
        if (fullname==null){throw new BooxException("L-initL: fullName  cannot be null" );}
    	this.enterprise=enterprise;
        this.databaseName=enterprise.getDatabaseName();
        this.fullName=fullname;
        init();
     }

/**
     * Constructor instantiate a Ledger object from the database; the ledger must
     * have been created first (use Ledger.createLedger())  or a BooxException will be thrown
     *
     * @param name
     * @throws BooxException
     */
    private Ledger (Enterprise enterprise, String fullname, boolean full) throws BooxException {
        if (enterprise==null) {throw new BooxException("Linit - enterprise cannot be null");}
    	this.databaseName=enterprise.getDatabaseName();
        if (fullname==null){throw new BooxException("ledger name cannot be null");}
        
        this.fullName=fullname;
        
        if (full){
            init();
        }
        
    }
   
    private void init() throws BooxException{
        Connection connection=null;
        if (JDBCTable.tableExists(databaseName, TABLENAME)){
        try{
            connection=ConnectionSource.getConnection(databaseName);
            Statement statement=connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM "+TABLENAME+" WHERE "+FULLNAME_COLNAME+" = \'"+fullName+"\'");
            if(rs.next()){
            	name=rs.getString(NAME_COLNAME);
                parentName=rs.getString(PARENT_COLNAME);
                owner=new Clerk(rs.getString(OWNER_COLNAME));
                currency=Currency.getCurrency(rs.getString(CURRENCY_COLNAME));
                initialised=true;
            }else{
                logger.log(1, "Ledger-Init: couldn\'t find ledger fullname:" +fullName+ " in database "+databaseName);
                throw new BooxException("couldn\'t find ledger fullname:" +fullName+ " in database "+databaseName);
            }
         logger.log("ledger "+fullName+" initialised OK");   
        }catch(Exception e){
            logger.log( "Ledger-init: had issues ", e);
        }finally{
            try{connection.close();}catch(Exception p){logger.log("ledger-init exception closing connection");}
        }
        }else{
            logger.log(2, "Didn't create ledger "+name+", database not initialised");
        }
    }

 
    public String getName(){
        return name;
    }
    public Clerk getOwner(){
    	if (owner==null){
    		try {
				init();
			} catch (BooxException e) {
				// TODO Auto-generated catch block
				logger.log("exception thrown", e);
			}
    	}
        return owner;
    }
    
    /**
     * Note this method returns the names of accounts held in child ledgers as well as those directly held.
     * @return a list of account names. 
     */
    public List <String> getAccountNames(){
        List <String> accountNames = new ArrayList<String>();
            try{
            Connection connection = ConnectionSource.getConnection(databaseName);
            
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM "+Chart.TABLENAME+" WHERE "+Chart.LEDGER_COLNAME+" = \'"+name+"\'");
            while (rs.next()){
                accountNames.add(rs.getString(Chart.NAME_COLNAME));
            }
            connection.close();
           }catch(Exception e){
            logger.log("Ledger - getAccountNames() error" ,e);
            
            return null;
        }
        List <String> ledgerNames = getLedgerNames();
        Iterator <String> it = ledgerNames.iterator();
        while (it.hasNext()){
        
        try{
            String ledgername=(String) it.next();
            Connection connection = ConnectionSource.getConnection(databaseName);
            
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM "+Chart.TABLENAME+" WHERE "+Chart.LEDGER_COLNAME+" = \'"+ledgername+"\'");
            while (rs.next()){
                accountNames.add(rs.getString(Chart.NAME_COLNAME));
            }
            connection.close();
           }catch(Exception e){
            logger.log("Ledger - getAccountNames() error" ,e);
            return null;
           }
        }
        return accountNames;
    }
    /**
     * Note that this returns only the accounts held directly in this ledger
     * It does not return accounts held in child ledgers.
     * @return a List of closed (read-only) Accounts.
     */
    public List <Account> getAccounts(Enterprise enterprise, Clerk clerk){
        List<Account> accountsList = new ArrayList<Account>();
        Connection connection=null;
            try{
                connection = ConnectionSource.getConnection(enterprise.getDatabaseName());
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM "+Chart.TABLENAME+" WHERE "+Chart.LEDGER_COLNAME+" = \'"+name+"\'");
                while (rs.next()){
                    String accountName=(rs.getString(Chart.NAME_COLNAME));
                    Account account = Account.getAccount(enterprise, accountName, clerk, Permission.READ);
                    account.close();
                    accountsList.add(account);
                }
                connection.close();
                return accountsList;
           }catch(Exception e){
                logger.log("Ledger - getAccounts() error" ,e);
                try {connection.close();}catch(Throwable t){}
                return null;
           }
    }
  
   /**
    * Returns a list of first-generation child ledger names; 
    * it doesn't return their children.
    * @return a List of first-generation child ledger names in this Ledger
    */
    public List <String> getLedgerNames(){
        List <String> ledgerNames= new ArrayList<String>();
        try{
            Connection connection = ConnectionSource.getConnection(databaseName);
            
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM "+TABLENAME+" WHERE "+PARENT_COLNAME+" = \'"+name+"\'");
            while (rs.next()){
                ledgerNames.add(rs.getString(FULLNAME_COLNAME));
            }
            connection.close();
            return ledgerNames;
        }catch(Exception e){
            logger.log("Ledger - getLedgerNames() error" ,e);
            return null;
        }
    }
    //this is mental. Don't need it. 
    public List <Ledger> getLedgers(Enterprise enterprise) throws BooxException {
        List<String> ledgerNames = getLedgerNames();
        Iterator<String> lnit = ledgerNames.iterator();
        List<Ledger> ledgers = new ArrayList<Ledger>();
        while(lnit.hasNext()){
            Ledger ledger = new Ledger(enterprise, (String) lnit.next());
            ledgers.add(ledger);
        }
        return ledgers;
    }
    /**
     * 
     * @return the balance of this ledger.
     * @throws CurrencyException if the ledger contains accounts in different currencies.
     *  
     */
    private Money getLedgerBalance(Enterprise enterprise, Clerk clerk)throws CurrencyException {

       Money balance= new Money(getCurrency(), BigDecimal.ZERO);
       try{
	       List<Account> accounts = getAccounts(enterprise, clerk);
	       for (Account account: accounts){
	            logger.log(5, "account "+account.getName()+ " currency is "+ account.getCurrency().getTLA());
	            Money accBal=account.getBalance();
	            logger.log(5, "debiting ledger "+name+"in "+getCurrency().getTLA()+" with account: "+account.getName()+" in "+accBal.getCurrency().getTLA());
	            balance.debit(accBal);
	       }
       }catch(Exception e){
           logger.log("Ledger-gLB: getting account balances",e);
       }

       try{
	       List <String> ledgerNames=getLedgerNames();
	       for(String ledgerName:ledgerNames){
	            Ledger ledger=null;
	            try {
	                ledger=new Ledger(enterprise, ledgerName);
	            }catch(Exception bex){
	                logger.log("Unexpected Boox Exception in getLedgerBalance", bex);
	            }
	            Money lbalance=ledger.getLedgerBalance(enterprise, clerk);
	            //logger.log(5, "debiting ledger "+name+" with ledger: "+ledger.getName());
	            balance.debit(lbalance);
	        }
       }catch(Exception e){
           logger.log("Ledger-gLB: getting subledger balances",e);
       }
        return balance;
    }
    public Money getBalance(Enterprise enterprise, Clerk clerk)throws PermissionsException{
    	logger.log("Ledger getting balance for "+name);
        if (! clerk.canBalance(enterprise, this)){
             throw new PermissionsException("Clerk "+ clerk.getName()+" does not have balance rights on ledger "+getName());
        }
        logger.log(5, "LGB: trying to balance ledger "+name);
        try{
            if(name.equals(enterprise.getName())){
                //General ledger balance is always zero; this pre-empts the
                //CurrencyException by cheating.
                return Money.zero(Currency.getCurrency(Boox.DEFAULT_CURRENCY));
            }else{
                Money balance = getLedgerBalance(enterprise, clerk);
                logger.log(5, "LGB: got balance on ledger "+name);
                return balance;
            }
        }catch(Exception e){
           logger.log("Ledger.getBalance had issues on ledger "+name, e);
           return null;    
        }        
    }
    /**
     * 
     * @return a List of AuditElements - that is, the accounts and ledgers that make up this Ledger
     * @throws BooxException 
     * @throws PermissionsException 
     * @throws uk.co.platosys.boox.money.CurrencyException
     */
    public List <AuditElement>audit(Enterprise enterprise, Clerk clerk) throws BooxException, PermissionsException {
        if(! clerk.canAudit(enterprise, this)){
            throw new PermissionsException("Clerk "+ clerk.getName()+" does not have audit rights on ledger "+getName());
        }
        List<AuditElement> auditList = new ArrayList<AuditElement>();
        List <String> ledgerNames=getLedgerNames();
        Iterator <String> it=ledgerNames.iterator();
        while(it.hasNext()){
            String ledgerName=(String)it.next();
            Ledger ledger=new Ledger(enterprise, ledgerName);
            auditList.add(ledger);
       }
         
       List <Account> accounts = getAccounts(enterprise, clerk);
        Iterator <Account> ita = accounts.iterator();
        while(ita.hasNext()){
            Account account = (Account) ita.next();
            auditList.add(account);
        }
        
        return auditList;
    }
    /**
     * 
     * @return a List of AuditLines - these are more lightweight and suitable
     * for serialization in an AJAX environment than AuditElements.
     * @throws uk.co.platosys.boox.money.CurrencyException
     */
    public ArrayList <AuditLine>lineAudit(Enterprise enterprise, Clerk clerk) throws PermissionsException {
        if(! clerk.canAudit(enterprise, this)){
            throw new PermissionsException("Clerk "+ clerk.getName()+" does not have audit rights on ledger "+getName());
        }
        ArrayList<AuditLine> auditList = new ArrayList<AuditLine>();
        List <String> ledgerNames=getLedgerNames();
        Iterator <String> it=ledgerNames.iterator();
        while(it.hasNext()){
            String ledgerName=(String)it.next();
            try {
            Ledger ledger=new Ledger(enterprise, ledgerName );
            auditList.add(new AuditLine(ledger.getBalance(enterprise, clerk),ledgerName,new ISODate()));
            }catch(BooxException bex){
                logger.log("Missing Ledger name Error", bex);
            }
       }
         
       List <Account> accounts = getAccounts(enterprise, clerk);
        Iterator <Account> ita = accounts.iterator();
        while(ita.hasNext()){
            Account account = (Account) ita.next();
            auditList.add(new AuditLine(account.getBalance(), account.fullName, new ISODate()));
        }
        
        return auditList;
    }
    public boolean close(){
        try{
             return true;
        }catch(Exception e){
            return false;
        }
    }

    public String getCurrencyName() {
        return currency.getSymbol();
    }
    public Currency getCurrency(){
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = Currency.getCurrency(currency);
    }
    public boolean isPrivate(){
        return isPrivate;
    }
    public void setPrivate(boolean isPrivate){
        this.isPrivate=isPrivate;
    }
    
    
    /**
     * 
     * @return this ledger's parent ledger, or this if this is the general(root) ledger.
     * @throws BooxException if the parent name is somehow missing from the list of ledger names.
     *    //
     * currently broken and returns null because we no longer have the constructor that 
     * works on the single name due to them no longer being unique. The name constructor works on the 
     * fullname. Fixed; parentName is now parent fullname.
     * 
     * Deprecated because we probably don't need it due to using fullnames
     * 
     * UnDeprecated because we need it for permissions checking //all can go
     * 
     * 
     */
    
    public Ledger getParent(Enterprise enterprise) throws BooxException{
        if (this.hasParent(enterprise)){
        	return new Ledger(enterprise, parentName);
        }else{
        	return this;
        }
    }
    /**
     * Every Ledger, bar the root Ledger, has a parent. This method
     * simply checks to see if it's the root ledger.
     * 
     * @return false if it's the root ledger; true otherwise.
     */
 
    public boolean hasParent(Enterprise enterprise){
    	if(this.fullName==null){return false;}//the name should never be null.
       if (this.getFullName().equals(ROOT_LEDGER_NAME)){
            return false;
        }else{
            return true;
        }
    }
   /**
    * Returns the date of the last transaction on this ledger
    */

	@Override
	public ISODate getDate() {
		// TODO Auto-generated method stub
		return new ISODate();
	}
/**
 * 
 * @return the name of this ledger's parent
 */
	public String getParentName() {
		return parentName;
	}
	
	/**
	 * @return The budgeted balance to be shown by the this Ledger at the given date. 
	 */
	@Override
	public Money getBudget(Clerk clerk, Date date) throws PermissionsException,
			TimingException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *  sets the budget for this ledger at the given date
	 */
	 @Override
	public void setBudget(Clerk clerk, Money money, Date date)
			throws PermissionsException, CurrencyException, TimingException {
		// TODO Auto-generated method stub
		
	}
    /**
     * sets the interpolation mode. At present, only linear interpolation
     * is supported. 
     * @throws BooxException 
     */
	@Override
	public void setInterpolationMode(Clerk clerk, int interpolationMode)
			throws PermissionsException, BooxException {
		if( interpolationMode!=Budgetable.LINEAR_INTERPOLATION_MODE){throw new BooxException("unsupported interpolation mode parameter");}
		
	}
    /**
     * returns the interpolation mode - always 
     */
	@Override
	public int getInterpolationMode() {
		// TODO Auto-generated method stub
		return Budgetable.LINEAR_INTERPOLATION_MODE;
	}

	@Override
	public Money getVariance(Clerk clerk, Date date) throws PermissionsException, TimingException {
		// TODO Auto-generated method stub
		return null;
	}

	
    ////Static methods associated with the Ledger object//////
	/**
	 * Returns a Ledger object. Note that in the current implementation, this is identical to using
	 * the public constructor. However, you should use this static call in preference to the public
	 * constructor because future implementations may allow for more efficient resource management.
	 * Note that the argument must be the fully-qualified name.
	 * @param enterprise
	 * @param fullname
	 * @return
	 */
	  public static Ledger getLedger(Enterprise enterprise, String fullname){
          try{
              return new Ledger(enterprise, fullname, true);
          }catch(Exception e ){
              logger.log("Ledger-getLedger("+fullname+") problem  ", e);
              return null;
          }
      }
	  public static Ledger getAccountLedger(Enterprise enterprise, String accountFullname){
		  String ledgerName= accountFullname.split("#")[0];
		  return getLedger(enterprise, ledgerName);
     }
	 /**
     * Creates a Ledger. If one of the same name, parent, owner and currency already exists,
     * it is returned; [if one of the same name, but different parent, owner and/or currency,
     * a new name is made by concatenation.]  .
     *
     * @param databaseName
     * @param name
     * @param parent
     * @param currency
     * @param owner
     * @param isPrivate
     * @return a Ledger
     * @throws BooxException
     */
    public static Ledger createLedger(Enterprise enterprise, String name, Ledger parent, Currency currency, Clerk owner, boolean isPrivate) throws BooxException, PermissionsException{
    	 if (enterprise==null){throw new BooxException("Ledger-CL: enterprise  cannot be null");}
 
    	if(! owner.canCreateLedgers()){
            logger.log(3, "clerk "+owner.getName()+"cannot create ledgers");
            throw new PermissionsException("clerk "+owner.getName()+"cannot create ledgers");
        }
       Connection connection=null;
        try{ 
             connection = ConnectionSource.getConnection(enterprise.getDatabaseName());
            Statement statement = connection.createStatement();
            String parentName="none";
            String fullName=name;
            if (parent!=null){
                parentName=parent.getFullName();
                fullName=parent.getFullName()+DELIMITER+name;
            }
            String SQLString = ("SELECT * FROM "+TABLENAME+" WHERE ("+NAME_COLNAME+" = \'"+name+"\')");
            ResultSet rs = statement.executeQuery(SQLString);
            
            //TODO rework this logic.
            if (rs.next()){
            	if((rs.getString(FULLNAME_COLNAME)).equals(fullName)){
            		connection.close();
                    return new Ledger(enterprise, fullName, true);
            	}
            }
            String sqlString;
            try{
            logger.log(5, "Boox-cL: creating "+name+" ledger");
            sqlString = ("INSERT INTO "+TABLENAME+" ("
            		+NAME_COLNAME+","
            		+PARENT_COLNAME+","
            		+FULLNAME_COLNAME+","
            		+CURRENCY_COLNAME+","
            		+OWNER_COLNAME+","
            		+ISPRIVATE_COLNAME+")" +
            				" VALUES (\'"+name+"\',\'"+parentName+"\',\'"+fullName+"\',\'"+currency.getTLA()+"\',\'"+owner.getName()+"\',"+Boolean.toString(isPrivate)+")");
            logger.log(sqlString);
            statement.execute(sqlString);
            }catch (PSQLException psqle){//THIS IS A KLUDGY FIX TO A CONCURRENCY PROBLEM BEST ADDRESSED IN THE db///}
            	
            }
            sqlString=("INSERT INTO "+Permission.TABLENAME+" ("
            		+ Permission.CLERK_COLNAME+","
            		+ Permission.LEDGER_COLNAME+","
            		+ Permission.ACCOUNTS_COLNAME+","
            		+ Permission.CREDIT_COLNAME+","
            		+ Permission.DEBIT_COLNAME+","
            		+ Permission.BALANCE_COLNAME+","
            		+ Permission.READ_COLNAME+","
            		+ Permission.AUDIT_COLNAME+"," 
            		+ Permission.SET_BUDGET_COLNAME+","
            		+ Permission.GET_BUDGET_COLNAME+")"+
            				" VALUES(\'"+owner.getName()+"\',\'"+fullName+"\', true, true, true, true, true, true, true, true)");
            logger.log(sqlString);
            statement.execute(sqlString);
            connection.close();
           Ledger ledger = new Ledger(enterprise, fullName, true);
            return ledger;
        }catch(Exception e){
            logger.log("Boox-createLedger: error ", e);
            return null;
        }finally{
        	try {connection.close();} catch (SQLException e) {}
        }
      }
    /**
     * Returns a List of the names of the ledgers existing in this database.
     * @param databaseName
     * @return a java.util.List of ledger names
     */
    public static List<String> listLedgerNames(Enterprise enterprise){
        List<String> ledgerList=new ArrayList<String>();
        Connection connection=null;
        try{
         connection = ConnectionSource.getConnection(enterprise.getDatabaseName());
                
         Statement statement = connection.createStatement();
         ResultSet rs= statement.executeQuery("SELECT * from "+TABLENAME);
         while(rs.next()){
            ledgerList.add(rs.getString("name"));
         }
         connection.close();
        
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}

           logger.log("Ledger-llN: problem listing ledger names", e);
             
            
        }
           return ledgerList; 
   }

	public String getFullName() {
		return fullName;
	}

	
	
}
