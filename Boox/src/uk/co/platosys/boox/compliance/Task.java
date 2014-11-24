package uk.co.platosys.boox.compliance;

import java.util.Date;
import java.util.List;
import java.util.Map;

import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.Permission;

public interface Task {
	public static final int ONCE = 1;
	public static final int DAILY = 2;
	public static final int WEEKLY = 4;
	public static final int FORTNIGHTLY = 6;
	public static final int PERIOD_END = 8;
	public static final int FOURWEEKLY = 10;
	public static final int MONTHLY = 12;
	public static final int QUARTERLY = 14;
	public static final int ANNUAL = 16;
	public static final int IRREGULAR = 20;
	public static final String TABLENAME="bx_tasks";
	public static final String TPTABLENAME="bx_tasks_permissions";
	public static final String SYSNAME_PREFIX="tsk";
	public static final String SYSNAME_COLNAME="sysname";
	public static final String TASKNAME_COLNAME="Task";
	public static final String TASKDESCRIPTION_COLNAME="Description";
	public static final String FREQUENCY_COLNAME="Frequency";
	public static final String LAST_DATE_COLNAME="last_date";
	public static final String NEXT_DATE_COLNAME="next_date";
	public static final String ROLE_COLNAME="role";
	public static final String OWNER_COLNAME="owner";
	public static final String DELEGATE_COLNAME="delegate";
	public static final String FORM_COLNAME="form_class";//this column records the java classname of a form used to complete
	//this task.
	public static final String PERMISSION_COLNAME="permission";
	public static final String LEDGER_COLNAME="ledger";
	
	public Map<Ledger, Permission> getNecessaryPermissions(Enterprise enterprise, Clerk clerk);
	public void grantNecessaryPermissions(Enterprise enterprise, Clerk supervisor, Clerk grantee);
	public String getName();
	public Clerk getOwner();
	public List<Clerk> getDelegates();
	public Date getNextDueDate();
	public void setFormClassName(String formClassName);
	/**
	 * This allows the Task framework to record the classname of a form used to complete the task. Boox is agnostic 
	 * about the higher-level framework which will vary by implementation.  If using (say) GWT for a web-application, 
	 * this would be the classname of a GWT form to be used on the client side. Your app can then use Class.forName(classname) to
	 * instantiate the right form for each Task.
	 * @return
	 */
	public String getFormClassName();
}
