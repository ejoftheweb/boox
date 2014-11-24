package uk.co.platosys.boox.compliance;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.boox.core.Clerk;

/**
 * This class contains constants pertaining to roles, in particular the  string  values for the 
 * role attribute in the Task element in xml module definition documents.
 * 
 * Roles, however, are flexible and easily created. The standard definitions help keep them from proliferating. 
 * 
 * A role must be created by a Clerk, who is thus responsible for carrying out any tasks associated with it.
 * However the role can be delegated. Normally all roles created on setup are assigned to the system supervisor.
 * 
 * [Roles aren't pre-defined, they emerge: a Task is associated with a role by the role attribute in the task definition element.
 * NO, THEY NEED TO BE PREDEFINED IN A CONFIG FILE.
 *  ]
 * 
 * 
 * 
 * @author edward
 *
 */


public class Role {
	
	/* TODO
	 * These roles need to be read from a system config file
	 * 
	 */
   public static final String VAT="vat";
   public static final String CONTROLLER="controller";
   public static final String BOUGHT_LEDGER="purchases";
   public static final String SOLD_LEDGER="sales";
   public static final String GENERAL="general";
   public static final String AUDITOR="auditor";
   public static final String STOCKTAKER="stocktaker";
   public static final String MANAGER="manager";
   public static final String FRIEND="friend";
   public static final String DIRECTOR="director";
   public static final String STAKEHOLDER="stakeholder";
   public static final String SHAREHOLDER="shareholder";
   public static final String GUARANTOR="guarantor";
   public static final String MENTOR="mentor";
   public static final Role VAT_ROLE=new Role(VAT, VAT);
   public static final Role CONTROLLER_ROLE=new Role(CONTROLLER, CONTROLLER);
   public static final Role BOUGHT_LEDGER_ROLE=new Role(BOUGHT_LEDGER, BOUGHT_LEDGER);
   public static final Role SOLD_LEDGER_ROLE=new Role(SOLD_LEDGER,SOLD_LEDGER);
   public static final Role GENERAL_ROLE=new Role(GENERAL,GENERAL);
   public static final Role AUDITOR_ROLE=new Role(AUDITOR,AUDITOR);
   public static final Role STOCKTAKER_ROLE=new Role(STOCKTAKER,STOCKTAKER);
   public static final Role MANAGER_ROLE=new Role(MANAGER,MANAGER);
   public static final Role FRIEND_ROLE=new Role(FRIEND,FRIEND);
   public static final Role DIRECTOR_ROLE=new Role(DIRECTOR,DIRECTOR);
   public static final Role STAKEHOLDER_ROLE=new Role(STAKEHOLDER,STAKEHOLDER);
   public static final Role SHAREHOLDER_ROLE=new Role(SHAREHOLDER,SHAREHOLDER);
   public static final Role GUARANTOR_ROLE=new Role(GUARANTOR,GUARANTOR);
   public static final Role MENTOR_ROLE=new Role(MENTOR,MENTOR);
    
   
   private Clerk clerk;//the clerk responsible for this role.
   private String name;
   private String localisedName;
	public Role(String rolename, String localisedName) {
		this.name=rolename;
		this.localisedName = localisedName;
	}
	
	
	/**
	 * Returns the Clerk currently holding this role
	 * @return
	 */
    public Clerk getClerk(){
    	return clerk;
    }
    /**
     * Delegates the role from one Clerk to another
     * @param delegator
     * @param delegatee
     * @return
     */
    public boolean delegate(Clerk delegator, Clerk delegatee){
    	//TODO
    	return false;
    }
    /**
     * Returns the role associated with this rolename. If it doesn't exist, it is created with the supplied Clerk as its owner.
     * if the Clerk param is null and the role exists it is returned, if it doesn't exist it will return null. 
     * if the Clerk param is notnull and the role exists, the role is returned and the clerk param is ignored.
     * @param rolename
     * @return
     */
    public static Role getRole(Clerk clerk, String rolename){
    	return new Role(rolename, rolename);
    }
    /**
     * Returns an ArrayList of roles for which this Clerk is responsible.
     * @param clerk
     * @return
     */
    public static List<Role> getRoles(Clerk clerk){
    	//TODO
    	return new ArrayList<Role>();
    }
    /**
     * Returns an ArrayList of all the roles
     * @return
     */
    public static List<Role> getRoles(){
    	ArrayList<Role> roles=new ArrayList<Role>();
    	roles.add(AUDITOR_ROLE);
    	roles.add(VAT_ROLE);
    	roles.add(MENTOR_ROLE);
    	roles.add(CONTROLLER_ROLE);
    	roles.add(DIRECTOR_ROLE);
    	roles.add(SHAREHOLDER_ROLE);
    	roles.add(STAKEHOLDER_ROLE);
    	return roles;
    }
    public String getName(){
    	return name;
    }
    public static Role getRole(String rolename){
    	return new Role(rolename, rolename);
    }


	public String getLocalisedName() {
		return localisedName;
	}


	public void setLocalisedName(String localisedName) {
		this.localisedName = localisedName;
	}


	
}
