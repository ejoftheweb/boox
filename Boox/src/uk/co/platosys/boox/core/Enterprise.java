/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.platosys.boox.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.co.platosys.boox.Body;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.CredentialsException;
import uk.co.platosys.boox.money.Currency;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.boox.trade.*;
import uk.co.platosys.db.DBTools;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.jdbc.DatabaseProperties;
import uk.co.platosys.db.jdbc.JDBCRow;
import uk.co.platosys.db.jdbc.JDBCTable;
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
 * including the name of the database where they store their accounts. 
 *
 * @author edward
 */
public class Enterprise extends Body {
	protected static final String TABLENAME="bx_enterprise";
	static final String KEY_COLNAME="key";
	static final String VALUE_COLNAME="value";
    private String name;
    private String databaseName;//this is the name of the database where the enterprise stores its accounts. It is basically the enterpriseID, prepended with a letter if needed.
    private String legalName;
   // private Journal journal;
    private Ledger generalLedger;
    private Clerk supervisor;
    private JDBCTable enterpriseTable;
    private boolean isVatRegistered;
    
    public static Currency DEFAULT_CURRENCY=Currency.getCurrency(Boox.DEFAULT_CURRENCY);
    private static Logger logger=Logger.getLogger("boox");
    private String enterpriseID;
    
    
    /**
     * Instantiates an Enterprise object given its enterpriseID
     * 
     * @param enterpriseID
     * @throws BooxException
     */
    protected Enterprise(String enterpriseID) throws BooxException{
      setId(enterpriseID);
      setTrade(true);
       try{
       
       setName(Directory.getName(enterpriseID));
       putInfo(Body.NAME, name);
       setLegalName(Directory.getLegalName(enterpriseID));
       putInfo(Body.LEGAL_NAME, legalName);
       setDatabaseName(Directory.getDatabaseName(enterpriseID));
       this.generalLedger=Ledger.getLedger(this, Ledger.ROOT_LEDGER_NAME);
       if (this.name==null){throw new BooxException("enterprise ID"+enterpriseID+" seems to have no name");}
       if (this.legalName==null){throw new BooxException("enterprise ID"+enterpriseID+" seems to have no legalName");}
       if (this.databaseName==null){throw new BooxException("enterprise ID"+enterpriseID+" seems to have no dbname");}
        
     }catch(Exception x){
    	 logger.log("E(EID) error reading details ", x);
     	throw new BooxException("could not read enterprise details from mster database", x);
     }
    }
    public String getName(){
        return this.name;
    }
    public String getDatabaseName(){
        return this.databaseName;
    }
    public String getEnterpriseID(){
    	return enterpriseID;
    }
    public String getLegalName(){
        return this.legalName;
    }
    protected void setLegalName(String legalName){
        this.legalName=legalName;
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
    public Money getNetAssetValue(){
    	//TODO
    	return null;
    
    }
  
    
    
    
    
    
   
/**
     * This method returns an Enterprise with an authenticated Supervisor.
     * @param enterpriseID
     * @param databaseName
     * @param supervisorName
     * @param supervisorPassword
     * @return
     * @throws CredentialsException 
     */
    public static Enterprise getEnterprise (String enterpriseID,  String supervisorName, String supervisorPassword) throws CredentialsException, BooxException{
       Enterprise enterprise=new Enterprise(enterpriseID);
      // enterprise.setJournal(Boox.loadJournal(enterprise));
       enterprise.setSupervisor(Boox.createSupervisor(enterprise, supervisorName, supervisorPassword));
       return enterprise;
    }
    /**
     * returns an Enterprise with no authenticated supervisor.
     * @param name
     * @return
     */
    public static Enterprise getEnterprise(String enterpriseID) throws BooxException{
       Enterprise enterprise=new Enterprise(enterpriseID);
      // enterprise.setJournal(Boox.loadJournal(enterprise));
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
     * This method generates the enterpriseID which is a hash of the name and legal name.
     * 
     * the Enterprise is listed in the master database, in the enterprises table; it also has its own database (which holds the accounts).
     * 
     */
    public static Enterprise createEnterprise(String name, String legalName, String databaseName) throws BooxException {
    	//need to 
    	 logger.log(2, "creating enterprise "+name);
    	
    	 String enterpriseID = ShortHash.hash(name+legalName);
    	if (databaseName==null){
    		//so we need to create a new database for this enterprise
    		databaseName=DBTools.removeFunnyCharacters(enterpriseID);//ensures it is legal (prepends a letter)
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
    	}
        
      
        JDBCTable enterpriseTable=null;
         try{
         if(!(JDBCTable.tableExists(databaseName, Enterprise.TABLENAME))){
        	    logger.log("creating enterprise table in db "+databaseName);
                enterpriseTable = JDBCTable.createTable(databaseName, Enterprise.TABLENAME, Enterprise.KEY_COLNAME, JDBCTable.TEXT_COLUMN);
                enterpriseTable.addColumn(Enterprise.VALUE_COLNAME, JDBCTable.TEXT_COLUMN);
         }else{
                enterpriseTable=new JDBCTable(databaseName, Enterprise.TABLENAME, Enterprise.KEY_COLNAME);
                String[] cols={Enterprise.KEY_COLNAME, Enterprise.VALUE_COLNAME};
                String[] vals={"Enterprise Name", name};
                enterpriseTable.addRow(cols, vals);
                String[] valsw={"Legal Name", legalName};
                enterpriseTable.addRow(cols, valsw);
        }
        }catch(Exception x){
        	logger.log("problem initialising enterprise table in its own database", x);
            throw new BooxException("Couldn't initialise enterprise JDBCTable in its own database",x);
        }
        Enterprise enterprise = Enterprise.getEnterprise(enterpriseID);
        if (enterprise!=null){
        	logger.log("Enterprise created enterprise:"+enterprise.getName());
        	Boox.createNewJournal(enterprise);
        	try {
				Directory.addBody(enterprise);
			} catch (PlatosysDBException e) {
				throw new BooxException("problem adding enterprise to directory", e);
			}
        	return enterprise;
        }else{
        	logger.log(1, "ECE-null enterprise returned from EgetE(ID)");
        	return null;
        }
        
    }
    
    
}
