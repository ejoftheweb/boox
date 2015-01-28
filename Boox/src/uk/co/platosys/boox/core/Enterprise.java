/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.platosys.boox.core;

import java.text.ParseException;

import uk.co.platosys.boox.Body;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.CredentialsException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.money.Currency;
import uk.co.platosys.boox.money.CurrencyException;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.db.DBTools;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.jdbc.DatabaseProperties;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.ShortHash;
//import uk.co.platosys.xuser.Xaddress;

/**
 * In Boox, an Enterprise is the root entity for a set of accounts. 
 * 
 * An Enterprise has one General Ledger and one Journal.
 * 
 * The General Ledger is the root of the Ledger system and the Journal is a list of Transactions
 * carried out by the Enterprise.
 * 
 * This allows an application to host the accounts for several Enterprises. 
 * 
 * Every Enterprise stores its accounts in its own named database. By using a separate
 * database, rather than a schema in a single database, enterprises can store 
 * their accounts on a server of their choosing. All that matters is that the Boox 
 * engine can obtain JDBC connections to that database.
 * 
 * There is a separate, Boox database that stores the details of the enterprises on the system,
 * including the name of the database where they store their accounts. This is the Directory.
 *
 * @author edward
 */
public class Enterprise extends Body {
	protected static final String TABLENAME="bx_enterprise";
	static final String KEY_COLNAME="key";
	static final String VALUE_COLNAME="value";
	static final String NAME_PARAMNAME="Name";
	static final String LEGALNAME_PARAMNAME="LegalName";
	static final String ACCDATE_PARAMNAME="AccDate";
	static final String CURRENT_LEDGERNAME="Root:XBX:Current";
	static final String OPERATIONS_LEDGERNAME="Root:XBX:Operations";
	static final String ASSETS_LEDGERNAME="Root:XBX:Fixed:Assets";
    // private Journal journal;
    private Ledger generalLedger;
    private JDBCTable enterpriseTable;
    private boolean isVatRegistered;
    private ISODate accountingDate;
    public static Currency DEFAULT_CURRENCY=Currency.getCurrency(Boox.DEFAULT_CURRENCY);
    private static Logger logger=Logger.getLogger("boox");
    private Clerk supervisor;
    /**
     * Instantiates an Enterprise object given its sysname
     * 
     * @param sysname
     * @throws BooxException
     */
    protected Enterprise(String sysname) throws BooxException{
	      setSysname(sysname);
	      setTrade(true);
	      try{
		       setName(Directory.getName(sysname));
		       setLegalName(Directory.getLegalName(sysname));
		       setDatabaseName(Directory.getDatabaseName(sysname));
		       this.generalLedger=Ledger.getLedger(this, Ledger.ROOT_LEDGER_NAME);
		       if (getName()==null){throw new BooxException("enterprise ID"+sysname+" seems to have no name");}
		       if (getLegalName()==null){throw new BooxException("enterprise ID"+sysname+" seems to have no legalName");}
		       if (getDatabaseName()==null){throw new BooxException("enterprise ID"+sysname+" seems to have no dbname");}
	     }catch(Exception x){
	    	 logger.log("E(EID) error reading details ", x);
	     	throw new BooxException("could not read enterprise details from master database", x);
	     }
    }
    
    protected void setTable(JDBCTable JDBCTable){
        this.enterpriseTable=JDBCTable;
    }
   
    private void setSupervisor(Clerk supervisor){
    	this.supervisor=supervisor;
    }
    //this method can only return null atm?
    public Ledger getGeneralLedger(){
        return this.generalLedger; //trap null!
    }
    public Currency getDefaultCurrency(){
    	return DEFAULT_CURRENCY;
    }
  
    /**Deprecated Use setParameter instead
     * @param key
     * @param value
     */
    @Deprecated
    public void setValue(String key, String value){
        try {
            enterpriseTable.amend(key, "value", value);
        }catch(Exception ex){
            logger.log("problem amending enterprise details", ex);
        }
    }
    public boolean isVatRegistered(){
    	return isVatRegistered;
    }
    public Money getNetAssetValue(Clerk clerk) throws PermissionsException, CurrencyException{
    	Ledger current = Ledger.getLedger(this, CURRENT_LEDGERNAME);
    	Ledger assets = Ledger.getLedger(this, ASSETS_LEDGERNAME);
    	Money currentAssets=current.getBalance(this, clerk);
    	Money fixedAssets = assets.getBalance(this, clerk);
    	return Money.add(currentAssets, fixedAssets);
    }
  
    public Money getCurrentProfit(Clerk clerk) throws PermissionsException, CurrencyException{
    	Ledger ops = Ledger.getLedger(this, OPERATIONS_LEDGERNAME);
    	return ops.getBalance(this, clerk);
    }
    public Money getNetCurrentAssets(Clerk clerk) throws PermissionsException, CurrencyException{
    	Ledger current = Ledger.getLedger(this, CURRENT_LEDGERNAME);
    	 return current.getBalance(this, clerk);
     }
    
    
    
   
/**
     * This method returns an Enterprise with an authenticated Supervisor.
     * @param sysname
     * @param databaseName
     * @param supervisorName
     * @param supervisorPassword
     * @return
     * @throws CredentialsException 
     */
    public static Enterprise getEnterprise (String sysname,  String supervisorName, String supervisorPassword) throws CredentialsException, BooxException{
       Enterprise enterprise=new Enterprise(sysname);
       enterprise.setSupervisor(Boox.createSupervisor(enterprise, supervisorName, supervisorPassword));
       return enterprise;
    }
    /**
     * returns an Enterprise with no authenticated supervisor.
     * @param name
     * @return
     */
    public static Enterprise getEnterprise(String sysname) throws BooxException{
       Enterprise enterprise=new Enterprise(sysname);
       return enterprise;
    }
    
    /**
     * Creates a new Enterprise and returns it. 
     * @param name - the short name of the enterprise;
     * @param legalName - the long-form, legal name of the enterprise;
     * @throws BooxException 
     * @databaseName - the name of database where its accounts are stored. This should
     * be the name of an existing Platosys SQL database where the connection information
     * is stored in the databases.xml config file, or null. If a null is given as
     * the database name, Boox will try to create a new database using the 
     * enterprise's enterprise ID as the name. 
     * This method generates the sysname which is a hash of the name and legal name.
     * 
     * the Enterprise is listed in the master database, in the enterprises table; it also has its own database (which holds the accounts).
     * 
     */
    public static Enterprise createEnterprise(String name, String legalName, String databaseName, ISODate accDate) throws BooxException {
    	//need to 
    	 logger.log(2, "creating enterprise "+name);
    	
    	 String sysname = ShortHash.hash(name+legalName);
    	if (databaseName==null){
    		//so we need to create a new database for this enterprise
    		databaseName=DBTools.removeFunnyCharacters(sysname);//ensures it is legal (prepends a letter)
    		logger.log(2, "creating enterprise database with name "+databaseName);
    		
    		DatabaseProperties dbProps = new DatabaseProperties();
    		try {
    			logger.log("Boox calling dbProps to create db "+databaseName+ " in masterDB "+Boox.APPLICATION_DATABASE);
			
    			String dbN=dbProps.createDatabase(Boox.APPLICATION_DATABASE, databaseName);//which it does ok
				if (!dbN.equals(databaseName)){
					throw new PlatosysDBException("Boox error creating enterprise database: "+dbN+":"+databaseName);
				}
			} catch (PlatosysDBException e) {
				logger.log("problem creating new database", e);
				throw new BooxException("could not create new database for enteprise "+name, e);
			}
    		 setParameter(databaseName, NAME_PARAMNAME, name);
             setParameter(databaseName, LEGALNAME_PARAMNAME, legalName);
             try {
				Directory.addBody(sysname, name, legalName,databaseName, true);
			} catch (PlatosysDBException e) {
				// TODO Auto-generated catch block
				logger.log("error:",e);
			}
         	
    	}
         
        Enterprise enterprise = Enterprise.getEnterprise(sysname);
        if (enterprise!=null){
        	logger.log("Enterprise created enterprise:"+enterprise.getName());
        	Boox.createNewJournal(enterprise);
        	try{
        		 enterprise.setParameter(NAME_PARAMNAME, name);
                 enterprise.setParameter(LEGALNAME_PARAMNAME, legalName);
            enterprise.setAccountingDate( accDate);
            }catch(Exception ex){
        		logger.log("ECE problem setting enterprise parameter or addint to directory", ex);
        	}
        	return enterprise;
        }else{
        	logger.log(1, "ECE-null enterprise returned from EgetE(ID)");
        	return null;
        }
        
    }
    
    void setParameter(String name, String value) throws BooxException{
    	 JDBCTable enterpriseTable=null;
         try{
         if(!(JDBCTable.tableExists(getDatabaseName(), Enterprise.TABLENAME))){
        	    logger.log("creating enterprise table in db "+getDatabaseName());
                enterpriseTable = JDBCTable.createTable(getDatabaseName(), Enterprise.TABLENAME, Enterprise.KEY_COLNAME, JDBCTable.TEXT_COLUMN);
                enterpriseTable.addColumn(Enterprise.VALUE_COLNAME, JDBCTable.TEXT_COLUMN);
         }else{
                enterpriseTable=new JDBCTable(getDatabaseName(), Enterprise.TABLENAME, Enterprise.KEY_COLNAME);
                String[] cols={Enterprise.KEY_COLNAME, Enterprise.VALUE_COLNAME};
                String[] vals={name, value};
                enterpriseTable.addRow(cols, vals);
                
        }
         }catch(Exception x){
         	logger.log("problem setting parameter in table in its own database", x);
             throw new BooxException("Couldn't set enterprise parameter in its own database",x);
         }
    }
    static void setParameter(String databaseName, String name, String value) throws BooxException{
   	 JDBCTable enterpriseTable=null;
        try{
        if(!(JDBCTable.tableExists(databaseName, Enterprise.TABLENAME))){
       	    logger.log("creating enterprise table in db "+databaseName);
               enterpriseTable = JDBCTable.createTable(databaseName, Enterprise.TABLENAME, Enterprise.KEY_COLNAME, JDBCTable.TEXT_COLUMN);
               enterpriseTable.addColumn(Enterprise.VALUE_COLNAME, JDBCTable.TEXT_COLUMN);
        }else{
               enterpriseTable=new JDBCTable(databaseName, Enterprise.TABLENAME, Enterprise.KEY_COLNAME);
               String[] cols={Enterprise.KEY_COLNAME, Enterprise.VALUE_COLNAME};
               String[] vals={name, value};
               enterpriseTable.addRow(cols, vals);
               
       }
        }catch(Exception x){
        	logger.log("problem setting parameter in table in its own database", x);
            throw new BooxException("Couldn't set enterprise parameter in its own database",x);
        }
   }
    
    /** Returns a parameter from the parameters table
     * Note this only stores data as key-value pairs, both of which must be Strings. 
     *  @param name
     * @return
     * @throws BooxException 
     */
    String getParameter(String name) throws BooxException{
    	try{
	    	if(JDBCTable.tableExists(getDatabaseName(), Enterprise.TABLENAME)){
	    	 enterpriseTable=new JDBCTable(getDatabaseName(), Enterprise.TABLENAME, Enterprise.KEY_COLNAME);
	         return enterpriseTable.readString(name, Enterprise.VALUE_COLNAME);
	    	}else{
	    		throw new BooxException("EgetParam: Parameters table doesn't exist");
	    	}
	    }catch(Exception e ){
    		logger.log("Enterprise issue getting parameter "+name, e);
    		throw new BooxException("problem getting parameter "+name, e);
    	}
    }
    
    /**Returns the accounting date.
     * 
     * @return
     * @throws BooxException 
     */
	public ISODate getAccountingDate() {
		
		if (accountingDate==null){
			try {
				String date = getParameter(ACCDATE_PARAMNAME);
			
				accountingDate=new ISODate(date);
			} catch (Exception e) {
				logger.log("enterprise getAccounting date, date  error:",e);
			}
		}
		return accountingDate;
	}

	private void setAccountingDate( ISODate accountingDate) throws BooxException {
        this.accountingDate=accountingDate;
		setParameter(ACCDATE_PARAMNAME, accountingDate.dateTimeMs());
    }
	
	/**Sets the accounting date. The clerk  must be the authenticated supervisor.
	 * 	 * @param clerk
	 * @param accountingDate
	 * @throws BooxException
	 * @throws PermissionsException
	 */
	public void setAccountingDate( Clerk clerk, ISODate accountingDate) throws BooxException, PermissionsException {
		if ((clerk.equals(supervisor))&&(clerk.isAuthenticated())){
		   setAccountingDate(accountingDate);
		}else{
			throw new PermissionsException("not authorised to reset accounting date");
		}
    }
    
}
