/* 
 * Boox.java
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
 
 * Boox.java
 *
 * Created on 24 March 2007, 14:02
 *
 * 
 */

  
 
package uk.co.platosys.boox.core;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.postgresql.util.PSQLException;


import uk.co.platosys.boox.compliance.Role;
import uk.co.platosys.boox.compliance.Task;
//import uk.co.platosys.boox.compliance.BasicTask;
//import uk.co.platosys.boox.compliance.Role;
//import uk.co.platosys.boox.compliance.Task;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.BooxXMLException;
import uk.co.platosys.boox.core.exceptions.CredentialsException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.money.Currency;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.db.jdbc.ConnectionSource;
import uk.co.platosys.db.jdbc.JDBCSerialTable;
import uk.co.platosys.db.Table;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.DocMan;
import uk.co.platosys.util.HashPass;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.ShortHash;

/**
 * This class provides static methods for administration and setup.
 *
 * It needs a serious bit of tidying up!
 *
 *
 * 
 * @author edward
 */
public class Boox {
   
    public static String APPLICATION_NAME="boox";
    public static String DEFAULT_CURRENCY="XBX";
    public static String APPLICATION_DATABASE="platax";//OK this is a kluj for now
    public static Namespace NAMESPACE=Namespace.getNamespace("http://www.platosys.co.uk/boox");
    private static Namespace ns = NAMESPACE;

    public static int JOURNAL_SIZE=100;//number of journal transactions held in memory
    static Logger  debugLogger=Logger.getLogger("boox");
    static Logger logger=debugLogger;
    public static String GUEST_NAME="visitor";
    //some text constants
    static final String LEDGERS_ELEMENT_NAME = "Ledgers";
    static final String LEDGER_ELEMENT_NAME="Ledger";
    static final String TASKS_ELEMENT_NAME="Tasks";
    static final String TASK_ELEMENT_NAME="Task";
    static final String DESCRIPTION_ELEMENT_NAME="Description";
    static final String ACCOUNT_ELEMENT_NAME="account";
    static final String NAME_ATTRIBUTE_NAME="name";
    static final String PERMISSION_ATTRIBUTE_NAME="permission";
    static final String CASCADES_ATTRIBUTE_NAME="cascades";
    static final String IS_PRIVATE_ATTRIBUTE_NAME="isPrivate";
    static final String FREQUENCY_ATTRIBUTE_NAME="frequency";
    static final String FIRSTDATE_ATTRIBUTE_NAME="firstdate";
    static final String ROLE_ATTRIBUTE_NAME="role";
    static final String CURRENCY_ATTRIBUTE_NAME="currency";
    
    /**
    * this returns true if your accounting system has been set up.
    * @param databaseName
    * @param applicationName
    * @return true if the accounting system is set up.
    */
   public static boolean isSetup(String databaseName, String applicationName){
       return(JDBCTable.tableExists(databaseName, applicationName));
   }
   public static boolean markSetup(String databaseName, String enterpriseName){
        try{
                JDBCTable enterpriseTable=new JDBCTable(databaseName, Enterprise.TABLENAME, Enterprise.KEY_COLNAME);
                String[] cols={Enterprise.KEY_COLNAME, Enterprise.VALUE_COLNAME};
                String[] vals={"setup", "true"};
                enterpriseTable.addRow(cols, vals);
                return true;               
        }catch(Exception ex){
            debugLogger.log("problem marking setup", ex);
            return false;
        }
   }
    
    /**
     *@param databaseName the database holding the accounts
     TODO: refactor to avoid direct SQL work.
     *
     *This method is called internally if no journal has been set up and it creates the necessary tables on the database.
     */
   
    static void createNewJournal(Enterprise enterprise) {
        Connection connection=null;
        try{
            connection = ConnectionSource.getConnection(enterprise.getDatabaseName());
            Statement statement=connection.createStatement();
            //create clerks JDBCTable first
            try {
                statement.execute("CREATE TABLE "+Clerk.TABLENAME+" ("+Clerk.NAME_COLNAME+" text PRIMARY KEY,"
                													 	+Clerk.PASSWORD_COLNAME+" text, "
                													 	+Clerk.LEDGER_COLNAME+" text,"
                													 	+Clerk.SUPERVISOR_COLNAME +" text, "
                													 	+Clerk.EMAIL_COLNAME+" text,"
                													 	+Clerk.ROLE_COLNAME+" text,"
                													 	+Clerk.ACCOUNTS_COLNAME+"  boolean,"
                													 	+Clerk.LEDGERS_COLNAME+" boolean,"
                													 	+Clerk.CLERKS_COLNAME+" boolean)");
                debugLogger.log(5, "Bx-cNJ: clerks JDBCTable created OK");
            } catch (SQLException ex) {
                debugLogger.log("failed to create clerks JDBCTable", ex);
            }
            //then ledgers
            try {
                statement.execute("CREATE TABLE "+Ledger.TABLENAME+"("+Ledger.NAME_COLNAME+" text ," 
                													  +Ledger.FULLNAME_COLNAME+" text PRIMARY KEY, "
                													  +Ledger.PARENT_COLNAME+" text, "
                													  +Ledger.CURRENCY_COLNAME+" text,"
                													  + Ledger.OWNER_COLNAME+" text REFERENCES  " +Clerk.TABLENAME+" ("+Clerk.NAME_COLNAME+"),"
                													  + Ledger.ISPRIVATE_COLNAME +" boolean)");
                debugLogger.log(5, "Bx-cNJ: ledgers Table created OK");
            } catch (SQLException ex) {
                debugLogger.log("failed to create ledgers Table", ex);
            }
            //then permissions
            try {
                statement.execute("CREATE TABLE "+Permission.TABLENAME+" ("+Permission.CLERK_COLNAME+" text REFERENCES  "+Clerk.TABLENAME+" ("+Clerk.NAME_COLNAME+"),"
                														+ Permission.LEDGER_COLNAME+" text REFERENCES "+Ledger.TABLENAME+"("+Ledger.FULLNAME_COLNAME+"),"
                														+ Permission.ACCOUNTS_COLNAME +" boolean,"
                														+ Permission.CREDIT_COLNAME+" boolean,"
                														+ Permission.DEBIT_COLNAME+" boolean,"
                														+ Permission.BALANCE_COLNAME+" boolean,"
                														+ Permission.READ_COLNAME+" boolean,"
                														+ Permission.AUDIT_COLNAME+" boolean,"
                														+ Permission.GET_BUDGET_COLNAME+" boolean,"
                														+ Permission.SET_BUDGET_COLNAME+" boolean,"
                														+ Permission.ALL_COLNAME+" boolean,"
                														+ Permission.CASCADES_COLNAME+" boolean)");
                debugLogger.log(5, "Bx-cNJ: permissions JDBCTable created OK");
            }catch (SQLException ex) {
                debugLogger.log("failed to create permissions JDBCTable", ex);
            }
            //then clerkgroups
            try{
                statement.execute("CREATE TABLE "+ClerkGroup.TABLENAME+"("+ClerkGroup.GROUP_COLNAME+" text REFERENCES  "+Clerk.TABLENAME+" ("+Clerk.NAME_COLNAME+"),"
                														+ ClerkGroup.CLERK_COLNAME+" text REFERENCES  "+Clerk.TABLENAME+" ("+Clerk.NAME_COLNAME+"),"
                														+ ClerkGroup.OWNER_COLNAME+" boolean)");
                debugLogger.log(5, "Bx-cNJ: clerkgroupss Table created OK");
            }catch(SQLException ex) {
                debugLogger.log("failed to create clerkgroups Table", ex);
            }
            
            //then the chart of accounts      
            try {
              statement.execute("CREATE TABLE "+Chart.TABLENAME +"("+Chart.SYSNAME_COLNAME +" text PRIMARY KEY,"
            		  												+Chart.NAME_COLNAME +" text,"
            		  												+ Chart.FULLNAME_COLNAME +" text,"
            		  												+ Chart.OWNER_COLNAME+" text REFERENCES  "+Clerk.TABLENAME+" ("+Clerk.NAME_COLNAME+") ,"
            		  												+ Chart.LEDGER_COLNAME+" text REFERENCES "+Ledger.TABLENAME+"("+Ledger.FULLNAME_COLNAME+"),"
            		  												+ Chart.CURRENCY_COLNAME+" text,"
            		  												+ Chart.TYPE_COLNAME+" text,"
            		  												+ Chart.DESCRIPTION_COLNAME+" text)");
              debugLogger.log(5, "Bx-cNJ: chart Table created OK");
            } catch (SQLException ex) {
                debugLogger.log("failed to create chart Table", ex);
            }
            /*This is the wrong place to create the tasks table. we have given it the wrong schema anyway
          //now the tasks JDBCTable
            try {
                 
                //statement.execute("CREATE TABLE journal (transactionID integer PRIMARY KEY, debit text, credit text, amount numeric(20,2), currency text, date timestamp, clerk text REFERENCES clerks (name), note text)");
                //
                JDBCTable taskTable = JDBCTable.createTable(enterprise.getDatabaseName(), Task.TABLENAME, Task.TASKNAME_COLNAME, Table.TEXT_COLUMN);
                taskTable.addColumn(Task.TASKDESCRIPTION_COLNAME, Table.TEXT_COLUMN);
                taskTable.addColumn(Task.FREQUENCY_COLNAME, Table.INTEGER_COLUMN);
                taskTable.addColumn(Task.LAST_DATE_COLNAME, Table.TIMESTAMP_COLUMN);
                taskTable.addColumn(Task.NEXT_DATE_COLNAME, Table.TIMESTAMP_COLUMN);
                taskTable.addColumn(Task.OWNER_COLNAME, Table.TEXT_COLUMN);
                taskTable.addColumn(Task.DELEGATE_COLNAME, Table.TEXT_COLUMN);
                taskTable.addColumn(Task.ROLE_COLNAME, Table.TEXT_COLUMN);
                //if((journalTable.addSerialRow(Task.TID_COLNAME, Task.NOTE_COLNAME, "Row Zero"))!=0){
                	//throw new BooxException("Task Row Zero is not Zero!!!");
                //}
                debugLogger.log(5, "Bx-cNJ: tasks Table created OK");
                 
            } catch (Exception ex) {
                debugLogger.log("failed to create tasks Table", ex);
               
            }*/
            //finally the journal JDBCTable
            try {
                 
                //statement.execute("CREATE TABLE journal (transactionID integer PRIMARY KEY, debit text, credit text, amount numeric(20,2), currency text, date timestamp, clerk text REFERENCES clerks (name), note text)");
                //
                JDBCSerialTable journalTable = JDBCSerialTable.createTable(enterprise.getDatabaseName(), Journal.TABLENAME, Journal.TID_COLNAME);
                if(journalTable==null){logger.log("journal table not created");}
                journalTable.addColumn(Journal.DEBIT_COLNAME, Table.TEXT_COLUMN);
                journalTable.addColumn(Journal.CREDIT_COLNAME, Table.TEXT_COLUMN);
                journalTable.addColumn(Journal.AMOUNT_COLNAME, Table.DECIMAL_COLUMN);
                journalTable.addColumn(Journal.CURRENCY_COLNAME, Table.TEXT_COLUMN);
                journalTable.addColumn(Journal.DATE_COLNAME, Table.TIMESTAMP_COLUMN);
                journalTable.addColumn(Journal.CLERK_COLNAME, Table.TEXT_COLUMN);
                journalTable.addColumn(Journal.NOTE_COLNAME, Table.TEXT_COLUMN);
                journalTable.addColumn(Journal.STATUS_COLNAME, Table.BOOLEAN_COLUMN);
                //if((journalTable.addSerialRow(Journal.TID_COLNAME, Journal.NOTE_COLNAME, "Row Zero"))!=0){
                	//throw new BooxException("Journal Row Zero is not Zero!!!");
                //}
                debugLogger.log(5, "Bx-cNJ: journal Table created OK");
                 
            } catch (Exception ex) {
                debugLogger.log("failed to create journalTable", ex);
               
            }
            try {
                //This line is created so that the account tables can have a balance line with transaction id = 0;
                statement.execute("INSERT INTO "+Journal.TABLENAME+" VALUES(0, null, null, 0, null, null, null)");
            } catch (SQLException ex) {
                debugLogger.log("failed to initialise journal Table", ex);
               
            }
            
            
            connection.close();
            debugLogger.log(5, "Bx-cnJ: journal created OK");
            //return new Journal(enterprise,0,false);
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}

            debugLogger.log("Books failed to create new journal", e);
           // return null;
        }
    }

    /**
     * When first called for any existing set of accounts (i.e. on first running an application using Boox), this 
     * method creates both the supervisor - the Clerk with super-user powers - and the General Ledger, that is, the root 
     * ledger in the hierarchy of ledgers. It also creates a guest Clerk, who has no powers and whose password is the empty string.
     * 
     * If a supervisor has already been created, it will return a Clerk object,
     * provided the credentials are correct - that is, the name and password of the
     * supervisor first created.
     *
     * If the credentials don't match, it will throw a Credentials Exception
     * 
     * 
     * @param databaseName the name of the database.
     * @param name the supervisor username
     * @param password the supervisor's password
     * @return a clerk object, or null if there's an error.
     */
    public static Clerk createSupervisor(Enterprise enterprise, String name, String password) throws CredentialsException{
    	String generalLedgerName = Ledger.ROOT_LEDGER_NAME;
        logger.log("boox creating new supervisor "+name+" for enterprise "+ enterprise.getName());
    	Connection connection=null;
        try{
        	String databaseName = enterprise.getDatabaseName();
        	logger.log("CreateSupervisor: database name is "+databaseName);
        	connection = ConnectionSource.getConnection(databaseName);
            logger.log("got connection to "+databaseName);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from  "+Clerk.TABLENAME+"  WHERE role = 'supervisor'");
            if (!rs.next()){
                logger.log("no supervisor created - creating one");
            	statement.execute("INSERT INTO  "+Clerk.TABLENAME+"  ("+Clerk.NAME_COLNAME+","
            															+Clerk.PASSWORD_COLNAME+","
            															+Clerk.LEDGER_COLNAME+","
            															+Clerk.SUPERVISOR_COLNAME+","
            															+Clerk.ROLE_COLNAME+","
            															+Clerk.ACCOUNTS_COLNAME+"," 
            															+Clerk.LEDGERS_COLNAME+","
            															+Clerk.CLERKS_COLNAME+")" +
            								           		" VALUES (\'"+name+"\',\'"+hash(password)+"\',\'"+generalLedgerName+"\',\'"+name+"\',\'supervisor\',true,true,true)");
                debugLogger.log(5, "Boox - Supervisor added to db");
                
                statement.execute("INSERT INTO "+Ledger.TABLENAME+"("
                												+Ledger.NAME_COLNAME+","
                												+Ledger.FULLNAME_COLNAME+","
                												+Ledger.PARENT_COLNAME+","
                												+Ledger.CURRENCY_COLNAME+","
                												+Ledger.OWNER_COLNAME+")" +
                											" VALUES(\'"+generalLedgerName+"\',\'"+generalLedgerName+"\',\'none\',\'all\',\'"+name+"\')");
                //statement.execute("ALTER JDBCTable clerks SET ledger REFERENCES ledgers(name)");
                statement.execute("INSERT INTO "+Permission.TABLENAME+"(clerk, ledger, createAccounts, credit, debit, balance, read, audit) VALUES(\'"+name+"\',\'"+generalLedgerName+"\', true, true, true, true, true, true)");
                connection.close();
                Ledger generalLedger = new Ledger (enterprise, generalLedgerName);
                Clerk supervisor = new Clerk(enterprise, name, password);
                ClerkGroup.createClerkGroup(enterprise, ClerkGroup.ALL_GROUPNAME, supervisor);
                Clerk.createClerk(enterprise, GUEST_NAME, "", supervisor, generalLedger, false, false, false, false, false, false, false, false, false );
                
                //all.addMember(supervisor, guest);
                return supervisor;
            }else{
                connection.close();
                debugLogger.log(5, "Boox - didn't create supervisor, returning the existing one");
                return new Clerk(enterprise, name, password);//this bit throws the Credentials Exception!
            }
        } catch(CredentialsException ce){
        	logger.log("create supervisor ", ce);
            try{connection.close();}catch(Exception p){}

            throw ce;
        } catch(Exception e){
            try{connection.close();}catch(Exception p){}

            debugLogger.log("error creating supervisor", e);
            return null;
        }
    }
  
   
  
    /**
     * Opens a ledger.
     * @param name
     * @param clerk
     * @return
     * @throws BooxException
     */
    public static Ledger openLedger(Enterprise enterprise, String name, Clerk clerk) throws BooxException, PermissionsException{
        return new Ledger(enterprise, name);
    }

  
   

    
  
    
    /**
     *     runs a check to ensure that the system is in balance.
     *     note that this is currency-agnostic. 
     * 
     */
     public static final boolean balance(String databaseName){
         BigDecimal balance = BigDecimal.ZERO;
         Connection connection1=null;
         Connection connection2=null;
         try{
            connection1 = ConnectionSource.getConnection(databaseName);
            connection2 = ConnectionSource.getConnection(databaseName);
            
            Statement statement = connection1.createStatement();
            Statement statement2 = connection2.createStatement();
                
            ResultSet rs= statement.executeQuery("SELECT * FROM chart");
            while(rs.next()){
                String name = rs.getString("name");
                ResultSet account=statement2.executeQuery("SELECT amount FROM "+name+ "WHERE contra = \'balance\'");
                if(account.next()){
                    balance=balance.add(rs.getBigDecimal("amount"), Money.MC);
                     
                }else{
                    connection1.close();
                    connection2.close();
                    return false;
                }
            }
            connection1.close();
            connection2.close();
         }catch(Exception e){
              try{connection1.close();
              connection2.close();
              }catch(Exception p){}

             return false;
         }
         if (!balance.equals(BigDecimal.ZERO)){
            return false;
         } else{return true;}
     }
     /**
      * Hashes the given password for security
      * @param password
      * @return a hash of the given password
      */
   public static String hash(String password){
       return HashPass.hash(password);
   }
   
  
   /**
    * Tests for the existence of a Journal in the given database
    * @param databaseName
    * @return true if a Journal has been created.
    */
   
   public static boolean journalExists(String databaseName){
       Connection connection=null;
       try{
           connection=ConnectionSource.getConnection(databaseName);
           Statement statement = connection.createStatement();
           try {
                ResultSet rs = statement.executeQuery("SELECT * FROM "+Journal.TABLENAME+" WHERE "+Journal.TID_COLNAME+"=0");
                if (rs.next()){
                    debugLogger.log(5, "Boox-JournalExists: TRUE");
                    connection.close();
                    return true;
                }else{
                    debugLogger.log(5, "Boox-JournalExists: FALSE");
                    
                    connection.close();
                    return false;
                }
           }catch(PSQLException psqe){
               debugLogger.log(3, "Books-JournalExists? error:");
           
               connection.close();
               return false;
             
           }
       }catch(Exception e){
           debugLogger.log("Books-JournalExists? error: ", e);
           return false;
       }
   
   }
   /**
    *returns the full name of an account given its system (short) name.
    
   public static String getFullName(String accountName){
       return getFullName(accountName, DATABASE_NAME);
   }*/
   public static String getFullName(String accountName, String databaseName){
    Connection connection=null;
       try{
          connection=ConnectionSource.getConnection(databaseName);
       Statement statement=connection.createStatement();
           ResultSet rs=statement.executeQuery("SELECT * FROM "+Chart.TABLENAME+" WHERE name = \'"+accountName+"\'");
           if (rs.next()){
               String fullName=rs.getString(Chart.FULLNAME_COLNAME);
               connection.close();
               return fullName;
           }else{
               connection.close();
               debugLogger.log("Books-getFullName - couldn't find account named "+accountName);
               return null;
           }
       }catch(Exception e){
           try{connection.close();}catch(Exception ex){}
           debugLogger.log("Books-getFullName - error finding  account named "+accountName, e);
            return null;   
      
       }
   }
   /**
    * 
    * @param databaseName
    * @param ledgerName
    * @return the String representing the currency of the ledger
    */
   public static String getLedgerCurrency(String databaseName, String ledgerName){
   Connection connection=null;
       try{
          connection=ConnectionSource.getConnection(databaseName);
       Statement statement=connection.createStatement();
           ResultSet rs=statement.executeQuery("SELECT "+Ledger.CURRENCY_COLNAME+" FROM "+Ledger.TABLENAME+" WHERE name = \'"+ledgerName+"\'");
           if (rs.next()){
               String currency=rs.getString("currency");
               connection.close();
               return currency;
           }else{
               connection.close();
               debugLogger.log("Books-getLedgerCurrency - couldn't find ledger named "+ledgerName);
               return null;
           }
       }catch(Exception e){
           try{connection.close();}catch(Exception ex){}
           debugLogger.log("Books-getLedgerCurrency - error finding  ledger named "+ledgerName, e);
            return null;   
      
       }
   }
  
   private static String readProperty (String propertyName){
       String propertiesFileName="/etc/platosys/boox.xml";
    
       
       try{
            File propertiesFile = new File(propertiesFileName);
            java.util.Properties properties = new java.util.Properties();
            properties.loadFromXML(new FileInputStream(propertiesFile));
            return properties.getProperty(propertyName);
        }catch(Exception e){
            debugLogger.log("problem reading property", e);
            return null;
        }
    }
   
   /**
    * This returns a Map of the available Modules, indexed by the module name, reading from the boox.xml config
    * file which lists them all. It is deprecated in favour of the identical method in the Module class.
    * @return
    */
   @Deprecated
   public static Map<String, Module> getModules(){
	   try{ 
		   logger.log("Bx-getting the modules");
		   Map<String, Module> modules = new HashMap<String, Module>();
		   Document moduleDoc = DocMan.build(Module.MODULE_FILE);
		   Element rtel = moduleDoc.getRootElement();
		   List<Element> modelements = rtel.getChildren(Module.ELEMENT_NAME, ns);
		   Iterator<Element> it = modelements.iterator();
		   while (it.hasNext()){
			   Module module = new Module(it.next());
			   modules.put(module.getName(), module);
		   }
		   return modules;
	   }catch(Exception e){
		   logger.log("Boox - problem parsing the modules file", e);
		   return null;
	   }
   }
   /**
    * This returns a Map of the available Segments, indexed by the segment name, reading from the boox.xml config
    * file which lists them all. A segment is a group of modules.
    * @return
    */
   public static Map<String, Segment> getSegments(){
	   try{ 
		   Map<String, Segment> segments = new HashMap<String, Segment>();
		   Map<String, Module> modules= getModules();
		   Document segmentDoc = DocMan.build(Module.MODULE_FILE);
		   Element rtel = segmentDoc.getRootElement();
		   List<Element> modelements = rtel.getChildren(Segment.ELEMENT_NAME, ns);
		   Iterator<Element> it = modelements.iterator();
		   while (it.hasNext()){
			   Segment segment = new Segment(it.next());
			   for (Entry<String, Module> mit:modules.entrySet()){
				   Module module= mit.getValue();
				   if (module.getSegment().equals(segment.getName())){
					   segment.addModule(module);
				   }
			   };
			   
			   segments.put(segment.getName(), segment);
		   }
		   return segments;
	   }catch(Exception e){
		   logger.log("Boox - problem parsing the modules file", e);
		   return null;
	   }
   }
   /**
    * This is one of a group of setup/configuration methods.
    * @param enterprise
    * @param clerk
    * @param tasksElement
    */
   public static void createTasks(Enterprise enterprise, Clerk clerk, Element tasksElement) throws PermissionsException {
	   List<Element> taskElements= tasksElement.getChildren(TASK_ELEMENT_NAME, ns);
	   Map<Ledger, Permission> permissions=new HashMap<Ledger,Permission>();
       for(Element taskElement: taskElements){
           String name = "";
           String description = "";
           String frequency = "";
           String rolename ="";
           String formClassName="";
           
           try{
        	   name = taskElement.getAttributeValue(Task.NAME_ATTNAME);
               description=taskElement.getChildText(Task.TASKDESCRIPTION_ELNAME, ns);
        	   frequency = taskElement.getAttributeValue(Task.FREQ_ATTNAME);
        	   rolename= taskElement.getAttributeValue(Task.ROLE_ATTNAME);
        	   formClassName=taskElement.getAttributeValue(Task.FORM_ATTNAME);
           }catch(Throwable t){
        	   
           }
           String sysname = ShortHash.hash(Task.SYSNAME_PREFIX+name+description);
           
           int frint=20;
           
           switch (frequency){
	           case "once": frint=Task.ONCE; break;
	           case "daily": frint=Task.DAILY; break;
	           case "weekly": frint=Task.WEEKLY;break;
	           case "fortnightly": frint=Task.FORTNIGHTLY;break;
	           case "fourweekly": frint=Task.FOURWEEKLY;break;
	           case "monthly": frint=Task.MONTHLY;break;
	           case "periodend": frint=Task.PERIOD_END;break;
	           case "quarterly": frint=Task.QUARTERLY;break;
	           case "annual": frint=Task.ANNUAL;break;
	           case "irregular": frint=Task.IRREGULAR;break;
	           default: frint=Task.IRREGULAR; 
           }
           Role role=Role.getRole(rolename);
           
           List<Element> ledgerElements=taskElement.getChildren(Boox.LEDGER_ELEMENT_NAME, ns);
           
           for(Element ledgerElement: ledgerElements){
	        	String permissionName="";
	        	String ledgerName="";
	        	boolean cascades=false;
	        	try{
	        		ledgerName=ledgerElement.getAttributeValue(NAME_ATTRIBUTE_NAME);
	        		permissionName=ledgerElement.getAttributeValue(PERMISSION_ATTRIBUTE_NAME);
	        		cascades=(ledgerElement.getAttributeValue(CASCADES_ATTRIBUTE_NAME).equals("true"));
	        	}catch(Throwable t){
	        		
	        	}
	        	Ledger ledger=Ledger.getLedger(enterprise, ledgerName);
	        	Permission permission;
	        	if (cascades){
	        		permission=CascadingPermission.getPermission(permissionName);
	        	}else{
	        		permission=Permission.getPermission(permissionName);
	        	}
	        	if (clerk.hasPermission(enterprise, ledger, permission)){
	        		permissions.put(ledger, permission);
	        	}else{
	        		String error = "BX: creating task - permissions error: owning clerk "+clerk.getName()+" does not have "+permission.getName()+" permission on ledger "+ledger.getFullName();
	        		logger.log(error);
	        		throw new PermissionsException(error);
	        	}
           }
           Task.createTask(enterprise, clerk, name, description, frint, role, formClassName, permissions);
       }   
   }

   ///
   /// These methods are used to set up or modify a Boox system given a Boox xml file, an
   /// org.jdom.Document, or an org.jdom.Element.
   ///


   /**
    * Boox provides a simple way to set up an accounting installation with a full
    * chart of accounts, using an XML file which maps the hierarchy of Ledgers.
    * The distribution jar contains a sample boox.xml file which can easily be
    * customised for your application.
    *
    * @param clerk the supervisor setting up the hierarchy of accounts
    * @param file the location of the boox.xml file
    * @throws BooxException
    * @throws PermissionsException
    */
   public static void createLedgersAndAccounts(Enterprise enterprise, Clerk clerk, File file) throws BooxException, PermissionsException{
      logger.log("creating ledgers and accounts for "+enterprise.getName()+" from template file "+file.getAbsolutePath());
	   Document document;
	try {
		if (file.exists()&&file.canRead()){
			document = DocMan.build(file);
			if (document==null){throw new BooxException("Boox: DocMan returned a null document at "+file.getAbsolutePath());}
		}else{
			throw new BooxException("template file at "+file.getAbsolutePath()+" does not exist or is unreadable");
		}
	} catch (Exception e) {
		logger.log("failed to build xml accts doc from file",e );
		throw new BooxException("failed to build xml accts doc from file",e );
	}
       try {
		createLedgersAndAccounts(enterprise, clerk, document);
	} catch (BooxXMLException e) {
		// TODO Auto-generated catch block
		logger.log("Boox XML exception thrown - xml error in file "+file.getAbsolutePath(), e);
		//throw new BooxException("XML Exception", e);
	}
   }
   /**
    * This takes an org.jdom.Document argument instead of a java.io.File, but
    * does the same thing.
    *
    * @param clerk
    * @param document
    * @throws BooxException
    * @throws PermissionsException
 * @throws BooxXMLException 
    */
   public static void createLedgersAndAccounts(Enterprise enterprise, Clerk clerk, Document document) throws BooxException, PermissionsException, BooxXMLException{
       Element rootElement = document.getRootElement();
       Element ledgersElement = rootElement.getChild(LEDGERS_ELEMENT_NAME, ns);
       if (ledgersElement==null){
    	   throw new BooxXMLException("ledgers schema docs faulty, no ledgers element");
       }
        if (ledgersElement.getNamespace()!=ns){
            throw new BooxXMLException("wrong namespace in schema documents");
        }
       
        logger.log("creating ledgers and accounts for "+enterprise.getName()+" from template document "+document.toString());
  	  createLedgersAndAccounts(enterprise, clerk, ledgersElement, openLedger(enterprise, Ledger.ROOT_LEDGER_NAME, clerk) );
  	  
  	  Element tasksElement = rootElement.getChild(TASKS_ELEMENT_NAME, ns);
  	  if(tasksElement!=null){
  		  createTasks(enterprise, clerk, tasksElement);
  	  }//no requirement for a tasks element.
   }
  
   /**
    * This method takes an org.jdom.Element argument and creates a sub-hierarchy
    * of Ledgers in the given Ledger.
    * @param databaseName
    * @param clerk
    * @param element
    * @param ledger
    * @throws BooxException
    * @throws PermissionsException
    */
   public static void createLedgersAndAccounts(Enterprise enterprise, Clerk clerk, Element element, Ledger ledger) throws BooxException, PermissionsException{
        createAccounts(enterprise, clerk, element, ledger);
        debugLogger.log(5, "creating sub-ledgers in ledger "+ledger.getName());
        setLedgerPermissions(enterprise, clerk, element, ledger);
        List<Element> ledgerElements= element.getChildren("Ledger", ns);//use constant!
        Iterator<Element> lit = ledgerElements.iterator();
        while(lit.hasNext()){
            Element ledgerElement = (Element) lit.next();
            String name = ledgerElement.getAttributeValue("name");
            boolean isprivate = false;
            isprivate = Boolean.valueOf(ledgerElement.getAttributeValue("isPrivate"));//use constant
            Currency currency=ledger.getCurrency();
            try{
                if (ledgerElement.getAttributeValue("currency")!=null){
                    currency=Currency.getCurrency(ledgerElement.getAttributeValue("currency"));//use constant
                }
            }catch(Exception e){
                debugLogger.log("error reading currency attribute for ledger "+name, e);
            }
            Ledger newLedger = Ledger.createLedger(enterprise, name, ledger, currency, clerk, isprivate);
            debugLogger.log( "BX_CL Created ledger:"+newLedger.getName()+" , owner "+newLedger.getOwner().getName()+", fullname "+newLedger.getFullName());
            createLedgersAndAccounts(enterprise, clerk, ledgerElement, newLedger);
        }
   }
   private static void createAccounts(Enterprise enterprise, Clerk clerk, Element element, Ledger ledger) throws BooxException, PermissionsException {
        List<Element> accountElements = element.getChildren("Account", ns);
        Iterator<Element> ait = accountElements.iterator();
        while(ait.hasNext()){
            Element accountElement = (Element) ait.next();
            String name = accountElement.getAttributeValue("name");
           // String fullName=accountElement.getAttributeValue("fullName");
            String description=accountElement.getAttributeValue("description");
            debugLogger.log(5, "about to create "+name+" account");
            Account.createAccount(enterprise, name,   clerk, ledger,ledger.getCurrency(), description);
        }
   }
   private static void setLedgerPermissions(Enterprise enterprise, Clerk clerk, Element element, Ledger ledger) throws BooxException, PermissionsException {
        List<Element> permissionElements = element.getChildren("Permission", ns);
        Iterator<Element> pit = permissionElements.iterator();
        while(pit.hasNext()){
            Element permissionElement = (Element) pit.next();
            Permission permission  = Permission.getPermission(permissionElement.getAttributeValue("value"));
            String pclerk=permissionElement.getAttributeValue("clerk");
            Permission.setPermission(enterprise, pclerk, ledger, permission, true);
       }
   }
   public static void setApplicationName(String applicationName){
       APPLICATION_NAME=applicationName;
   }
  
  
}
