/*
 ** Clerk.java
 *
 *  Copyright (C) 2008  Edward Barrow
 * 
     
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
 
 * Clerk.java
 *
 * Created on 24 March 2007, 14:02
 *
 * 
 */

  

package uk.co.platosys.boox.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.CredentialsException;
import uk.co.platosys.db.jdbc.ConnectionSource;
import uk.co.platosys.util.HashPass;
import uk.co.platosys.util.Logger;

/**
 * The Clerk object - as the name suggests - wraps the persona of a user (an accounts clerk, would you believe) of the 
 * books accounting system. 
 * 
 * Clerks have varying privileges in relation to Ledgers, handled by the permissions
 * system.
 * 
 * Clerk objects can be authenticated or unauthenticated. Authenticated clerks can
 * exercise their permissions; authentication is done by a login module. Non-authenticated
 * Clerk objects are much more limited; they are essentially just pointers.
 *
 * Clerks are normally referred to by their names. 
 * 
 * There are also clerkgroups. The effect of clerkgroups is to provide a set of aliases.
 * In the database, "band" is a synonym for "clerkgroup". 
 * The clerkgroup "all" is a universal group to which all clerks are added on 
 * registration.
 * 
 * @author edward
 */
public  class Clerk {
	static final String TABLENAME="bx_clerks";
    static final String NAME_COLNAME="name";
    static final String PASSWORD_COLNAME="password";
    static final String LEDGER_COLNAME="ledger";
    static final String SUPERVISOR_COLNAME="supervisor";
    static final String EMAIL_COLNAME="email";
    static final String ROLE_COLNAME="role";
    static final String ACCOUNTS_COLNAME="accounts";
    static final String LEDGERS_COLNAME="ledgers";
    static final String CLERKS_COLNAME="clerks";
    private String name;
    private boolean createAccounts=false;
    private boolean createLedgers=false;
    private boolean createClerks=false;
    private static Logger logger = Logger.getLogger("boox");
    private boolean authenticated = false;
    private Set<ClerkGroup> groups=new HashSet<ClerkGroup>();
    private Set<String> names=new HashSet<String>();
    private String databaseName;
    private static Map<String, Clerk> clerks=new HashMap<String, Clerk>();
    /**
     * This constructor is deprecated.
     * Creates a new instance of Clerk
     */
    @Deprecated
    public Clerk(String name, Ledger ledger,  boolean createAccounts, boolean createLedgers, boolean createClerks ) {
        this.name=name;
        this.createAccounts=createAccounts;
        this.createLedgers=createLedgers;
        this.createClerks=createClerks;
    }
       /**
     *
     * Creates a new instance of Clerk
     */
    private Clerk(Enterprise enterprise, String name,   boolean createAccounts, boolean createLedgers, boolean createClerks ) {
        this.name=name;
        this.createAccounts=createAccounts;
        this.createLedgers=createLedgers;
        this.createClerks=createClerks;
        this.databaseName=enterprise.getDatabaseName();
        Connection connection=null;
        try{
            connection = ConnectionSource.getConnection(databaseName);
            Statement statement = connection.createStatement();
            ResultSet rs=statement.executeQuery("SELECT "+ClerkGroup.GROUP_COLNAME+" FROM "+ClerkGroup.TABLENAME+" WHERE "+ClerkGroup.CLERK_COLNAME+" = \'"+name+"\'");
            if(rs.next()){
                ClerkGroup group = new ClerkGroup(rs.getString(ClerkGroup.GROUP_COLNAME));
                groups.add(group);
                names.add(group.getName());
            }
            connection.close();
        }catch(Exception x ){
            logger.log("Clerk constructor problem populating groups/names info",x);
            try{connection.close();}catch(Exception d){}
        }
    }
    
    /**
     * This constructor instantiates an authenticated Clerk object.
     * @param databaseName
     * @param name
     * @param password
     * @throws uk.co.platosys.boox.core.exceptions.CredentialsException
     * @throws uk.co.platosys.boox.core.exceptions.BooxException
     */
   public Clerk(Enterprise enterprise, String name, String password) throws CredentialsException,BooxException  {
	   this.databaseName=enterprise.getDatabaseName();
        Connection connection;
        Statement statement;
        ResultSet rs;
        try{
            connection = ConnectionSource.getConnection(databaseName);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM "+TABLENAME+" WHERE "+NAME_COLNAME+" = \'"+name+"\'");
        }catch(Exception e){
            throw new BooxException("database problem creating clerk", e);
        }
        try{
            //populate from the clerks table;
            if (rs.next()){
               String hashedPassword = rs.getString(PASSWORD_COLNAME);
               if (HashPass.check(password, hashedPassword)){
                   this.name=name;
                   names.add(name);
                   Ledger.getLedger(enterprise, rs.getString(LEDGER_COLNAME));
                   this.createAccounts=rs.getBoolean(ACCOUNTS_COLNAME);
                   this.createLedgers=rs.getBoolean(LEDGERS_COLNAME);
                   this.createClerks=rs.getBoolean(CLERKS_COLNAME);
                   this.authenticated=true;
                   names.add(ClerkGroup.ALL.getName());
                   groups.add(ClerkGroup.ALL);
               }else{
                   logger.log ("password check failed for clerk "+name);
                   throw new CredentialsException("password check failed");
               }
            }else{
                logger.log("clerk "+name+" not found in database");
                throw new CredentialsException("clerk not found");
            }
            //populate from the groups table;
            rs=statement.executeQuery("SELECT "+ClerkGroup.GROUP_COLNAME+" FROM "+ClerkGroup.TABLENAME+" WHERE "+ClerkGroup.CLERK_COLNAME+" = \'"+name+"\'");
            if(rs.next()){
                ClerkGroup group = new ClerkGroup(rs.getString(ClerkGroup.GROUP_COLNAME));
                groups.add(group);
                names.add(group.getName());
            }
            clerks.put(name, this);
        }catch(SQLException sqx){
            try{connection.close();}catch(Exception x){}
            throw new BooxException("problem with result set creating clerk", sqx);
        }catch(CredentialsException ce){
            try{connection.close();}catch(Exception x){}
            throw ce;
        }finally{
            try{connection.close();}catch(Exception x){}
        }
    }
    public Clerk(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }

  
    public boolean canCreateClerks(){
        return createClerks;
    }
    public boolean canCreateLedgers(){
        return createLedgers;
    }
    public boolean canCreateAccounts(){
        return createAccounts;
    }
    
    /**
     * Returns true if the clerk object is both authenticated and
     * has the appropriate permissions. Use the hasPermission(String permission) method
     * to discover information about an unauthenticated Clerk object.
     * @param ledger
     * @return true if both authenticated and has permission, otherwise false
     */
    public boolean canCreateAccounts(Enterprise enterprise, Ledger ledger){
        if(authenticated){
            return hasPermission (enterprise, ledger, Permission.ACCOUNTS);
        }return false;
    }
     /**
     * Returns true if the clerk object is both authenticated and
     * has the appropriate permissions. Use the hasPermission(String permission) method
     * to discover information about an unauthenticated Clerk object.
      * canDebit means this Clerk is authorised to post debit transactions to accounts
      * in the given ledger.
     * @param ledger
     * @return true if both authenticated and has permission, otherwise false
     */
    public boolean canDebit(Enterprise enterprise, Ledger ledger){
    	//logger.log("checking debit permission in enterprise "+enterprise.getName()+" ledger "+ledger.getName());
        if (authenticated){
            return hasPermission(enterprise, ledger, Permission.DEBIT);
        }else{
            logger.log("Clerk "+name+" is not authenticated");
            return false;
        }
    }
     /**
     * Returns true if the clerk object is both authenticated and
     * has the appropriate permissions. Use the hasPermission(String permission) method
     * to discover information about an unauthenticated Clerk object.
      * 
      * canCredit means this clerk is authorised to post credit transactions to accounts
      * in the given ledger.
      * 
     * @param ledger
     * @return true if both authenticated and has permission, otherwise false
     */
    public boolean canCredit(Enterprise enterprise, Ledger ledger){
        if (authenticated){
            return hasPermission(enterprise, ledger, Permission.CREDIT);
        }return false;
    }
     /**
     * Returns true if the clerk object is both authenticated and
     * has the appropriate permissions. Use the hasPermission(String permission) method
     * to discover information about an unauthenticated Clerk object.
      * 
      * canBalance means the clerk is allowed to read the ledger balance (but not the detail
      * of individual accounts)
     * @param ledger
     * @return true if both authenticated and has permission, otherwise false
     */
    public boolean canBalance(Enterprise enterprise, Ledger ledger){
        if (authenticated){
            return hasPermission(enterprise, ledger, Permission.BALANCE);
        }return false;
    }
     /**
     * Returns true if the clerk object is both authenticated and
     * has the appropriate permissions. Use the hasPermission(String permission) method
     * to discover information about an unauthenticated Clerk object.
      * canRead implies canBalance
      * 
      * canRead means that this clerk can read the individual account balances within 
      * this ledger, but not the details of Transactions on each account.
     * @param ledger
     * @return true if both authenticated and has permission, otherwise false
     */
    public boolean canRead(Enterprise enterprise, Ledger ledger){
        if (authenticated){
        	logger.log("Clerk:canRead checking started");
            return hasPermission(enterprise, ledger, Permission.READ);
        }else{
        	logger.log("Clerk:canRead - clerk"+name+" is not authenticated");
        	return false;
        }
    }
     /**
     * Returns true if the clerk object is both authenticated and
     * has the appropriate permissions. Use the hasPermission(String permission) method
     * to discover information about an unauthenticated Clerk object.
      * canAudit implies canRead
      * 
      * canAudit means that this clerk can read the details of individual Transactions
      * in the ledger concerned.
     * @param ledger
     * @return true if both authenticated and has permission, otherwise false
     */
    public boolean canAudit (Enterprise enterprise, Ledger ledger){
        if (authenticated){
            return hasPermission(enterprise, ledger, Permission.AUDIT);
        }return false;
    }
    
    public boolean hasPermission(Enterprise enterprise, Ledger ledger, Permission permission){
        logger.log("Clerk-HP checking for permission "+permission.getName()+ " on ledger "+ledger.getFullName());
        
        //Permission checking Algorithm:
        //First, check to see if the permission is there on the ledger directly.
        //if not, check each parent recursively for an equivalent cascading permission.
        
        try{
        	//first check for a direct permission
        	//logger.log("Clerk-HP checking for direct permission "+permission.getName()+ " on ledger "+ledger.getFullName());
            if(hasPermission(enterprise, ledger, permission, false)){
                  return true;
            }else{

                while(ledger.hasParent(enterprise)){
                	//Recursively checking for cascading permission on parents
                	//logger.log("Clerk-HP recursively checking for cascading permission "+permission.getName()+ " on ledger "+ledger.getFullName());
                    
                    if(hasPermission(enterprise, ledger.getParent(enterprise),permission,  true)){
                        return true;
                    }
                    ledger=ledger.getParent(enterprise);
                }
                return false;
            
            }

        }catch(Exception exc){
            logger.log("permission checking error, px", exc);
            return false;
        }
    }

     private boolean hasPermission(Enterprise enterprise, Ledger ledger, Permission permission,  boolean cascades){
    	Connection connection;
        String databaseName = enterprise.getDatabaseName();
        try{
            connection=ConnectionSource.getConnection(databaseName);
            Statement statement = connection.createStatement();
            String sqlstring =("SELECT * FROM "+Permission.TABLENAME+" WHERE("+Permission.LEDGER_COLNAME+" = '"+ledger.getFullName()+"')");
          //  logger.log(sqlstring);
            ResultSet rs = statement.executeQuery(sqlstring);
            while(rs.next()){
               for(String aname:names){
                   if (rs.getString(Permission.CLERK_COLNAME).equals(aname)){
                       if(cascades){
                            if((rs.getBoolean(permission.getName()))&&(rs.getBoolean(Permission.CASCADES_COLNAME))){
                           //logger.log("CHPx Clerk "+name+" has cascade permission "+permission.getName()+" on ledger "+ledger.getFullName()+" in enterprise "+enterprise.getName());
                                rs.close();
                                statement.close();
                                connection.close();
                            	return true;
                            }else if((rs.getBoolean(Permission.ALL_COLNAME)&&(rs.getBoolean(Permission.CASCADES_COLNAME)))){
                         	//logger.log("CHPx Clerk "+name+" has all permissions inc "+permission.getName()+" on ledger "+ledger.getFullName()+" in enterprise "+enterprise.getName());
                            	rs.close();
                                statement.close();
                                connection.close();
                                return true;
                            }
                        }else{
                            if(rs.getBoolean(permission.getName())){
                            	logger.log("CHPx Clerk "+name+" has direct permission "+permission.getName()+" on ledger "+ledger.getFullName()+" in enterprise "+enterprise.getName());
                            	rs.close();
                                statement.close();
                                connection.close();
                            	return true;
                            }else if(rs.getBoolean(Permission.ALL_COLNAME)){
                            	logger.log("CHPx Clerk "+name+" has all permissions inc "+permission.getName()+" on ledger "+ledger.getFullName()+" in enterprise "+enterprise.getName());
                            	rs.close();
                                statement.close();
                                connection.close();
                            	return true;
                            }
                        }
                   }//
                }//done cycling names
             }
            logger.log("FALSE: "+name+" permission "+permission.getName()+" on ledger "+ledger.getName()+" in enterprise "+enterprise.getName());
            rs.close();
            statement.close();
            connection.close();
            return false;

        }catch(Exception x){
            logger.log("Clerk permissions checking error", x);
        }
       
        
        return false;
    }
    /**
     *
     * @param clerk
     * @param directly
     * @return true if the given clerk reports to this clerk (directly or indirectly,
     * according to the directly argument).
     */
    public boolean reports(Clerk clerk, boolean directly){
        List<String> clerkNames = getClerkNames(databaseName, this);
        Iterator<String> it = clerkNames.iterator();
        while(it.hasNext()){
            String cname=(it.next());
            if (cname.equals(clerk.getName())){
                return true;
            }
            if(!directly){
                Clerk clerka = new Clerk(cname);
                return clerka.reports(clerk, directly);
            }
        }
        return false;
    }
    public boolean isAuthenticated(){
        return authenticated;
    }
    public void disAuthenticate(){
        this.authenticated=false;
    }
   
    public boolean equals(Clerk clerk){
        return ((clerk.getName()).equals(name));
    }
    public String getDatabaseName(){
        return databaseName;
    }
    public static Clerk getClerk(Enterprise enterprise, String sysname){
    	if(clerks.containsKey(sysname)){
    		return clerks.get(sysname);
    	}else{
    		return new Clerk(enterprise, sysname, false, false, false);
    	}
    }
    
    
    /**
     * This returns a List of the names of those Clerks who report to the given supervisor.
     * It just returns their names, nothing more.
     * @param supervisor
     * @return a List of the names of those Clerks reporting to supervisor
     */

    public static List<String> getClerkNames(String databaseName, Clerk supervisor){
        List<String> clerks = new ArrayList<String>();
        
        Connection connection;
        try{
            connection = ConnectionSource.getConnection(databaseName);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT "+Clerk.NAME_COLNAME+" FROM  "+Clerk.TABLENAME+"  WHERE "+Clerk.SUPERVISOR_COLNAME+" = \'"+supervisor.getName()+"\'");
            while(rs.next()){
                String name = rs.getString(Clerk.NAME_COLNAME);
                clerks.add(name);
            }
            connection.close();
        }catch(Exception e){
         logger.log("Boox had a problem getting the list of clerk names", e);
        }
        return clerks;
    }
    /**
     * This returns a Set of those Clerks who report DIRECTLY or INDIRECTLY to the given supervisor.
     * The Clerks in the returned Set are not authenticated so can't be used to post or read anything.
     * If the supervisor is authenticated at the time the method is called, the  set contains
     * *validated* clerks (but only the direct reports. The indirect reports are not validate).
     * A validated clerk can create accounts and ledgers (but not post
     * or read anything to them, or create clerks).
     * @param supervisor
     * @return a Set of Clerks reporting DIRECTLY or INDIRECTLY to supervisor
     */
    public static Set<Clerk> getClerks(Enterprise enterprise, Clerk supervisor){
        Set<Clerk> clerks = new HashSet<Clerk>();
        
        Connection connection=null;
        try{
            connection = ConnectionSource.getConnection(enterprise.getDatabaseName());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM  "+Clerk.TABLENAME+"  WHERE "+Clerk.SUPERVISOR_COLNAME+" = \'"+supervisor.getName()+"\'");
            while(rs.next()){
                String name = rs.getString(Clerk.NAME_COLNAME);
                if(supervisor.isAuthenticated()){
                     Ledger ledger = new Ledger(enterprise, rs.getString(Clerk.LEDGER_COLNAME));
                     boolean createAccounts=rs.getBoolean(Clerk.ACCOUNTS_COLNAME);
                     boolean createLedgers=rs.getBoolean(Clerk.LEDGER_COLNAME);
                     Clerk clerk = new Clerk(name, ledger, createAccounts, createLedgers, false);
                     Set<Clerk> subs=getClerks(enterprise, clerk);
                     Iterator<Clerk> it = subs.iterator();
                     while(it.hasNext()){
                         clerks.add(it.next());
                     }
                     clerks.add(clerk);
                }else{
                    Clerk clerk = new Clerk(name);
                    Set<Clerk> subs=getClerks(enterprise, clerk);
                     Iterator<Clerk> it = subs.iterator();
                     while(it.hasNext()){
                         clerks.add(it.next());
                     }
                     clerks.add(clerk);
                }
            }
            connection.close();
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}

         logger.log("Clerk-gk had a problem getting the list of clerks", e);
        }
        return clerks;
        
    }
    /**
     *  Used to create new Clerks.
     * Creates a Clerk object and returns it; if the Clerk has already been created, initialises it but throws a CredentialsException if the supplied credentials don't match.
     * @param databaseName
     * @param name
     * @param password
     * @param supervisor
     * @param ledger
     * @param createAccounts
     * @param createLedgers
     * @param createClerks
     * @return an initialised Clerk object, or null in the case of an error.
     * @throws uk.co.platosys.boox.core.exceptions.CredentialsException
     */ 
    public static Clerk createClerk(Enterprise enterprise, String name, String password,  Clerk supervisor, Ledger ledger, boolean createAccounts, boolean createLedgers, boolean createClerks, boolean canCredit, boolean canDebit, boolean canBalance, boolean canRead, boolean canAudit, boolean cascades) throws CredentialsException{
        boolean createaccounts=createAccounts;
        boolean createledgers=createLedgers;
        boolean createclerks=createClerks;
        boolean tout=(createAccounts && canCredit && canDebit && canBalance && canRead && canAudit);
         if (!supervisor.canCreateClerks()){
             throw new CredentialsException("Clerk "+supervisor.getName()+ " is not authorised to create new Clerks");
         }
         if (!supervisor.canCreateAccounts()){createaccounts=false;}
         if (!supervisor.canCreateLedgers()){createledgers=false;}
         String SQLString="";
         Connection connection=null;
         try{
            String ledgerName = ledger.getName();
            connection = ConnectionSource.getConnection(enterprise.getDatabaseName());
            Statement statement = connection.createStatement();
            SQLString=("SELECT * FROM  "+Clerk.TABLENAME+"  WHERE (name = \'"+name+"\')");
            ResultSet rs = statement.executeQuery(SQLString);
            if(rs.next()){
                //Clerk name exists - so we try to return the matching Clerk object
                connection.close();//tidy up!
                return new Clerk(enterprise, name, password);//which will throw a CredentialsException if the wrong password has been supplied.
            }else{
                //this Clerk name does not exist so we create a new one.
                SQLString=("INSERT INTO  "+Clerk.TABLENAME+"  (name,password, ledger, supervisor, accounts, ledgers, clerks) VALUES (\'"+name+"\',\'"+HashPass.hash(password)+"\',\'"+ledgerName+"\',\'"+supervisor.getName()+"\',"+Boolean.toString(createaccounts)+","+Boolean.toString(createledgers)+","+Boolean.toString(createclerks)+")");
                statement.execute(SQLString);
                SQLString=("INSERT INTO "+Permission.TABLENAME+"(clerk, ledger, createAccounts, credit, debit, balance, read, audit, tout, cascades) VALUES (\'"+name+"\',\'"+ledgerName+"\',"+Boolean.toString(createaccounts)+","+Boolean.toString(canCredit)+","+Boolean.toString(canDebit)+","+Boolean.toString(canBalance)+","+Boolean.toString(canRead)+","+Boolean.toString(canAudit)+","+Boolean.toString(tout)+","+Boolean.toString(cascades)+")");
                statement.execute(SQLString);
                //SQLString=("INSERT INTO clerkgroups(clerk,band) VALUES(\'"+name+"\',\'all\')");
                //statement.execute(SQLString);
                logger.log(5, "Boox - created clerk "+ name);
                connection.close();
                return new Clerk(enterprise, name, password);
            }
        }catch(CredentialsException ce){
            try{connection.close();}catch(Exception p){}

            throw ce;
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}
            logger.log ("Boox-create clerk, error \n"+SQLString, e);
            return null;
        }
    }

}
