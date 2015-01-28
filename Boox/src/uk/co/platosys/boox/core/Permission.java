/*
  To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.boox.core;

import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.core.permissions.CascadingPermission;
import uk.co.platosys.db.jdbc.JDBCTable;

/**
 * The Permission object handles permissions. A permission attaches to a clerk and
 * a ledger, and can cascade, or not. The default is not, that means that a clerk who
 * has a permission on a ledger does not have that permission on its children, unless
 * the permission is a cascading permission.
 *
 * @author edward
 */
public class Permission {
	public static final String TABLENAME="bx_permissions";
    private String permissionName;
    protected boolean cascading=false;
    static String CLERK_COLNAME="clerk";
    static String LEDGER_COLNAME="ledger";
    static String DEBIT_COLNAME="debit";
    static String CREDIT_COLNAME="credit";
    static String READ_COLNAME="read";
    static String POST_COLNAME="post";
    static String ACCOUNTS_COLNAME="createaccounts";
    static String AUDIT_COLNAME="audit";
    static String BALANCE_COLNAME="balance";
    static String GET_BUDGET_COLNAME="get_budget";
    static String SET_BUDGET_COLNAME="set_budget";
    static String ALL_COLNAME="tout";
    static String CASCADES_COLNAME="cascades";
    
    public static Permission DEBIT=new Permission("debit");
    public static Permission CREDIT=new Permission("credit");
    public static Permission POST=new Permission("post");
    public static Permission READ=new Permission("read");
    public static Permission ACCOUNTS=new Permission("createaccounts");
    public static Permission AUDIT=new Permission("audit");
    public static Permission BALANCE=new Permission("balance");
    public static Permission GET_BUDGET=new Permission("get_budget");
    public static Permission SET_BUDGET=new Permission("set_budget");
    public static Permission ALL=new Permission("all");
    public static CascadingPermission CASCADING_DEBIT=new CascadingPermission("debit");
    public static CascadingPermission CASCADING_CREDIT=new CascadingPermission("credit");
    public static CascadingPermission CASCADING_POST=new CascadingPermission("post");
    public static CascadingPermission CASCADING_READ=new CascadingPermission("read");
    public static CascadingPermission CASCADING_ACCOUNTS=new CascadingPermission("createaccounts");
    public static CascadingPermission CASCADING_AUDIT=new CascadingPermission("audit");
    public static CascadingPermission CASCADING_BALANCE=new CascadingPermission("balance");
    public static CascadingPermission CASCADING_ALL=new CascadingPermission("all");
    public static CascadingPermission CASCADING_GET_BUDGET=new CascadingPermission("get_budget");
    public static CascadingPermission CASCADING_SET_BUDGET=new CascadingPermission("set_budget");
    protected Permission(String permissionName){
        if(permissionName.equals("all")){
            this.permissionName="tout";//"all" is a sql reserved word
        }else{
            this.permissionName=permissionName;
        }
    }

    public String getName(){

        return permissionName;
    }
    public boolean isCascading(){
        return cascading;
    }
   
    public static Permission getPermission(String permissionName){
        return new Permission(permissionName);
    }

    public boolean equals(Permission permission){
      if((permission.getName().equals(permissionName))&&(permission.isCascading()==cascading)){
          return true;
      }else{
          return false;
      }
    }
    /**
     * Sets a permission for a clerk to use a particular ledger.
     * By default, ledgers are created with full permissions for their owner, and no others.  
     * @param supervisor
     * @param clerk
     * @param ledger
     * @param permission
     * @param setting
     * @return true if successful, otherwise false.
     * @throws uk.co.platosys.boox.core.exceptions.PermissionsException
     * @throws uk.co.platosys.boox.core.exceptions.BooxException
     */
  
    public static boolean setPermission(Enterprise enterprise, Clerk groupOwner, ClerkGroup clerkgroup, Ledger ledger, Permission permission, boolean setting) throws PermissionsException, BooxException {
         //is the groupOwner owner f this clerkgroup?
        if(!(clerkgroup.ownedBy(groupOwner))){
            throw new PermissionsException("Clerk "+groupOwner.getName()+" cannot set permissions for group "+clerkgroup.getName()+", as does not own it");
        }
        //does the group owner  have this permission on this ledger?
        if(!groupOwner.hasPermission(enterprise, ledger, permission)){
            throw new PermissionsException("Group Owner "+groupOwner.getName()+" does not have "+permission.getName()+" on ledger "+ledger.getName());
        }
       
        return setPermission(enterprise, clerkgroup.getName(), ledger, permission, setting);
   }
    public  static boolean setPermission(Enterprise enterprise, String  clerkname, Ledger ledger, Permission permission, boolean setting) throws  BooxException {
        
        try{     
     	      String databaseName = enterprise.getDatabaseName();
                JDBCTable permissionsTable = new JDBCTable(databaseName,Permission.TABLENAME);
                String[] columns = {"clerk", "ledger"};
                String[] values={clerkname, ledger.getName()};
                if (permission.equals(Permission.ALL)){
                    permissionsTable.amendWhere(columns, values, DEBIT_COLNAME, setting);
                    permissionsTable.amendWhere(columns, values, CREDIT_COLNAME, setting);
                    permissionsTable.amendWhere(columns, values, ACCOUNTS_COLNAME, setting);
                    permissionsTable.amendWhere(columns, values, BALANCE_COLNAME, setting);
                    permissionsTable.amendWhere(columns, values, READ_COLNAME, setting);
                    permissionsTable.amendWhere(columns, values, AUDIT_COLNAME, setting);
                    permissionsTable.amendWhere(columns, values, ALL_COLNAME, setting);
                }else if(permission.equals(Permission.AUDIT)){
                    permissionsTable.amendWhere(columns, values, BALANCE_COLNAME, setting);
                    permissionsTable.amendWhere(columns, values, READ_COLNAME, setting);
                    permissionsTable.amendWhere(columns, values, AUDIT_COLNAME, setting);
                }else if(permission.equals(Permission.READ)){
                    permissionsTable.amendWhere(columns, values,  BALANCE_COLNAME, setting);
                    permissionsTable.amendWhere(columns, values, READ_COLNAME, setting);
                }else{
                     permissionsTable.amendWhere(columns, values, permission.getName(), setting);
                }

                 permissionsTable.amendWhere(columns, values, CASCADES_COLNAME, permission.isCascading());

            return true;
        }catch(Exception e){
            return false;
        }
   }
    /**
     * this method sets a ledger's credit permission to "all". It is often desirable to
     * allow anyone to post a credit to a ledger but restrict debit postings to its owner.
     * (I don't mind if you put money in my account, I just don't want you taking it out
     * without my permission....)
     *
     * @param ledgerOwner
     * @param ledger
     * @return
     * @throws PermissionsException
     */
    public static boolean setAllCreditPermission (Enterprise enterprise, Clerk ledgerOwner, Ledger ledger) throws PermissionsException, BooxException{
        if(!ledgerOwner.isAuthenticated()){throw new PermissionsException("Owner must be authenticated to set credit permission");}
        if(!ledger.getOwner().equals(ledgerOwner)){throw new PermissionsException("clerk "+ledgerOwner.getName()+" does not own ledger "+ledger.getName());}
        return setPermission(enterprise, ClerkGroup.ALL.getName(), ledger, Permission.CREDIT, true);
    }
    /**
    * this method sets a Ledger's audit permission to "all".
    * @param ledgerOwner
    * @param ledger
    * @return
    * @throws PermissionsException
    */
   public static boolean setOpenAuditPermission (Enterprise enterprise, Clerk ledgerOwner, Ledger ledger) throws PermissionsException, BooxException{
       if(!ledgerOwner.isAuthenticated()){throw new PermissionsException("Owner must be authenticated to set open permission");}
       if(!ledger.getOwner().equals(ledgerOwner)){throw new PermissionsException("clerk "+ledgerOwner.getName()+" does not own ledger "+ledger.getName());}
       return ((setPermission(enterprise, ClerkGroup.ALL.getName(), ledger, Permission.READ, true))&&
               (setPermission(enterprise, ClerkGroup.ALL.getName(), ledger, Permission.BALANCE, true))&&
               (setPermission(enterprise, ClerkGroup.ALL.getName(), ledger, Permission.AUDIT, true)));
   }
   /**
   * this method sets a Ledger's read permission to "all".
   * @param ledgerOwner
   * @param ledger
   * @return
   * @throws PermissionsException
   */
  public static boolean setOpenReadPermission (Enterprise enterprise, Clerk ledgerOwner, Ledger ledger) throws PermissionsException, BooxException{
      if(!ledgerOwner.isAuthenticated()){throw new PermissionsException("Owner must be authenticated to set open permission");}
      if(!ledger.getOwner().equals(ledgerOwner)){throw new PermissionsException("clerk "+ledgerOwner.getName()+" does not own ledger "+ledger.getName());}
      return ((setPermission(enterprise, ClerkGroup.ALL.getName(), ledger, Permission.READ, true))&&
              (setPermission(enterprise, ClerkGroup.ALL.getName(), ledger, Permission.BALANCE, true)));
  }
  /**
   * Sets a permission for a clerk to use a particular ledger.
   * By default, ledgers are created with full permissions for their owner, and no others.  
   * @param supervisor
   * @param clerk
   * @param ledger
   * @param permission
   * @param setting
   * @return true if successful, otherwise false.
   * @throws uk.co.platosys.boox.core.exceptions.PermissionsException
   * @throws uk.co.platosys.boox.core.exceptions.BooxException
   */
  public static boolean setPermission(Enterprise enterprise, Clerk supervisor, Clerk clerk, Ledger ledger, Permission permission, boolean setting) throws PermissionsException, BooxException {
     //This method is semi-broken.
      //
      //It must check the permissions of the granting clerk (supervisor).
      //
      //does the supervisor have this permission ?
     
      if(!supervisor.hasPermission(enterprise, ledger, permission)){
        throw new PermissionsException("Clerk "+supervisor.getName()+" cannot grant "+permission.getName()+" on ledger "+ledger.getName());
      }
      /*
      //is the supervisor a supervisor of this clerk?
      if(!(supervisor.reports(clerk, false))){
          throw new PermissionsException("Clerk "+supervisor.getName()+" cannot set permissions for "+clerk.getName()+", as does not report");
      }*/
     return setPermission(enterprise, clerk.getName(), ledger, permission, setting);
 }
}
