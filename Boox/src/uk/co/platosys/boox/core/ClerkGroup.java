 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.db.jdbc.ConnectionSource;
import uk.co.platosys.util.HashPass;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.RandomString;

/**
 * The ClerkGroup object lets you group Clerks together for 
 * the purposes of managing permissions.
 * clerk/txt   - group/txt - owner/bool
 * This is quite useful.
 *
 * @author edward
 */
public class ClerkGroup {
	static final String TABLENAME="bx_clerkgroups";
	static final String ALL_GROUPNAME="all";
	static final String GROUP_COLNAME="band";
	static final String CLERK_COLNAME="clerk";
	static final String OWNER_COLNAME="owner";
	private static Logger logger = Logger.getLogger("boox");
   private String name;
   private String databaseName;
   public static ClerkGroup ALL=new ClerkGroup("all");
    public ClerkGroup(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public boolean isMember(Clerk clerk){
       Connection connection=null;
       try{
           connection = ConnectionSource.getConnection(clerk.getDatabaseName());
           Statement statement=connection.createStatement();
           ResultSet rs = statement.executeQuery("SELECT clerk FROM clerkgroups WHERE band = \'"+name+"\'");
           while(rs.next()){
               if (rs.getString("clerk").equals(clerk.getName())){
                   connection.close();
                  return true;
               }
           }
           connection.close();
           return false;
       }catch(Exception e){
           try{connection.close();}catch(Exception x){}
       }
        
        return false;
    }
    public boolean ownedBy(Clerk clerk){
        Connection connection=null;
       try{
           connection = ConnectionSource.getConnection(clerk.getDatabaseName());
           Statement statement=connection.createStatement();
           ResultSet rs = statement.executeQuery("SELECT * FROM clerkgroups WHERE band = \'"+name+"\'");
           while(rs.next()){
               if (rs.getString("clerk").equals(clerk.getName())){
                   if (rs.getBoolean("owner")){
                        connection.close();
                        return true;
                   }else{
                       connection.close();
                       return false;
                   }
               }
           }
           connection.close();
           return false;
       }catch(Exception e){
           try{connection.close();}catch(Exception x){}
       }
        
        return true;
    }
    /**
     * Adds a new member to a clerkgroup.
     * @param owner
     * @param newMember
     * @return true if successful; false if newMember is already a member, or
     * an error occurs.
     * @throws uk.co.platosys.boox.core.exceptions.PermissionsException if owner
     * is not authenticated or does not own this clerkgroup.
     */

    public boolean addMember(Clerk owner, Clerk newMember) throws PermissionsException{
        if(!owner.isAuthenticated()){
            throw new PermissionsException("Clerk "+ owner.getName() + " is not authenticated");
        }
        if(!ownedBy(owner)){
            throw new PermissionsException("Clerk "+ owner.getName() + " does not own clerkgroup "+name+", so cannot add members");
        }
        Connection connection=null;
        try {
            connection =ConnectionSource.getConnection(owner.getDatabaseName());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT clerk FROM clerkgroups WHERE band = \'"+name+"\'");
            while (rs.next()){
                if (rs.getString("clerk").equals(newMember.getName())){
                    connection.close();
                    return false;
                }
            }
            statement.execute("INSERT INTO clerkgroups (clerk, band) VALUES (\'"+newMember.getName()+"\',\'"+name+"\')");
            connection.close();
            return true;
        }catch(Exception e){
            try{connection.close();}catch(Exception x){}
            return false;
        }
        
    }
    public boolean groupExists(String databaseName){
            Connection connection=null;
       try{
           connection = ConnectionSource.getConnection(databaseName);
           Statement statement=connection.createStatement();
           ResultSet rs = statement.executeQuery("SELECT * FROM clerkgroups WHERE band = \'"+name+"\'");
           if(rs.next()){
              connection.close();
              return true;
           }else{
              connection.close();
              return false;
            
           }
       }catch(Exception e){
           try{connection.close();}catch(Exception x){}
       }
        
        return true;
    }
    /**
     * Creates a new ClerkGroup. Clerks and ClerkGroups occupy the same namespace; 
     * this method will throw a BooxException if you attempt to create a ClerkGroup with
     * the same name as an existing clerk.
     * It will return a ClerkGroup owned by the clerk owner, as its only member.
     * You must then call the "add member" method on that group to extend it.
     * @param databaseName
     * @param name
     * @param owner
     * @return
     * @throws uk.co.platosys.boox.core.exceptions.PermissionsException
     * @throws uk.co.platosys.boox.core.exceptions.BooxException
     */
    public static ClerkGroup createClerkGroup(Enterprise enterprise, String name,  Clerk owner) throws PermissionsException, BooxException{
        if (!owner.canCreateClerks()){
             throw new PermissionsException("Clerk "+owner.getName()+ " is not authorised to create new ClerksGroups");
         }
         String SQLString="";
         Connection connection=null;
         try{
            connection = ConnectionSource.getConnection(enterprise.getDatabaseName());
            Statement statement = connection.createStatement();
            SQLString=("SELECT * FROM  "+Clerk.TABLENAME+"  WHERE (name = \'"+name+"\')");
            ResultSet rs = statement.executeQuery(SQLString);
            if(rs.next()){
                //Clerk name exists, so we throw an exception.
                connection.close();//tidy up!
                throw new BooxException("Cannot create clerk group named the same as an existing clerk");
            }else{
                //this Clerk name does not exist so we create a new one.
                String password =  RandomString.getRandomString(6);
                SQLString=("INSERT INTO  "+Clerk.TABLENAME+"  ("
                											+Clerk.NAME_COLNAME+","
                											+Clerk.PASSWORD_COLNAME+","
                											+Clerk.LEDGER_COLNAME+","
                											+Clerk.SUPERVISOR_COLNAME+","
                											+Clerk.ACCOUNTS_COLNAME+","
                											+Clerk.LEDGERS_COLNAME+","
                											+Clerk.CLERKS_COLNAME+")" +
                													" VALUES (\'"+name+"\',\'"+HashPass.hash(password)+"\',\'none\',\'"+owner.getName()+"\',"+Boolean.toString(false)+","+Boolean.toString(false)+","+Boolean.toString(false)+")");
                statement.execute(SQLString);
                SQLString=("INSERT INTO "+TABLENAME+" ("+GROUP_COLNAME+","+CLERK_COLNAME+","+OWNER_COLNAME+") VALUES(\'"+name+"\',\'"+owner.getName()+"\', true)");
                statement.execute(SQLString);
                logger.log(5, "Boox - created ClerkGroup "+ name);
                connection.close();
                return new ClerkGroup(name);//, ledger, createaccounts, createledgers, createclerks);
            }
        
        }catch(Exception e){
            try{connection.close();}catch(Exception p){}

           logger.log ("Boox-create ClerkGroup" +
                    ", error \n"+SQLString, e);
            return null;
        }
    }
}
