package uk.co.platosys.boox.compliance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.Permission;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.Table;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.ShortHash;
/**
 *  This class defines the Boox task management system.
 *  
 *  <h2>Tasks Defined in XML</h2>
 *  Tasks are defined in module xml configuration files, in the <Tasks> element. 
 *  Every Task has its own <Task> element with the following attributes:
 *  name: the name of the task, freq its frequency (which can take one of the following
 *  ten string values: "once", "daily", "weekly", "fortnightly", "period", "fourweekly", "monthly", "quarterly", "annual", "irregular", or
 *  a positive integer which is the number of "frequnit"s, default day_s, between occurrences) and optionally, "frequnit" if frequency
 *  is expressed as an integer and can be "second", "minute", "hour", "days"(default), "week", "years".  
 *  It then has any number of Ledger elements, which contain the ledger name and an associated Permission attribute which provides the 
 *  permissions necessary to complete the Task in question. 
 *  
 *  <h2>Tasks Defined in SQL</h2>
 *  Tasks are listed in three sql tables. The main tasks table has the task sysname as a key, and it is referred to by two other tables: permissions and delegates.
 *  
 *  
 *  <h2>Task Completion</h2>
 *  When a task is completed, a TaskCompleted object is returned. The Task class then makes the necessary changes to the task record. The task is recorded
 *  as completed, and if it is a recurring one the next task is created in SQL
 *  
 * There is never more than one active instance of a task in the SQL table.
 *  
 *  <h2>Delegation<h2>
 *  Responsibility for completing a task always rests with the task owner. However, the task owner can delegate a task to one or more delegates. The 
 *  delegated task will then appear in the delegate's  task list and the delegate will receive reminders as well as the task owner. 
 *  
 * @author edward
 *
 */
public  class Task {
	//Strings related to the task definition elements in the module file.
	public static final String TASKS_ELNAME="Tasks";
	public static final String TASK_ELNAME="Task";
	public static final String NAME_ATTNAME="name";
	public static final String FREQ_ATTNAME="freq";
	public static final String ONCE_FREQ="once";
	public static final String DAILY_FREQ="daily";
	public static final String WEEKLY_FREQ="weekly";
	public static final String WEEKLY2_FREQ="fortnightly";
	public static final String WEEKLY4_FREQ="fourweekly";
	public static final String PERIOD_FREQ="period";
	public static final String MONTHLY_FREQ="monthly";
	public static final String QUARTERLY_FREQ="quarterly";
	public static final String ANNUAL_FREQ="annual";
	public static final String IRREG_FREQ="irregular";
	public static final String FORM_ATTNAME="form";
	public static final String LEDGER_ELNAME="Ledger";
	public static final String PERM_ATTNAME="permission";
	
	
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
	
	//Strings related to the tasks table specification
	public static final String TABLENAME="bx_tasks";
	public static final String SYSNAME_PREFIX="tsk";
	public static final String SYSNAME_COLNAME="sysname";
	public static final String TASKNAME_COLNAME="Task";
	public static final String TASKDESCRIPTION_COLNAME="Description";
	public static final String TASKDESCRIPTION_ELNAME="Description";
	public static final String FREQUENCY_COLNAME="Frequency";
	public static final String DUE_DATE_COLNAME="due_date";
	public static final String DONE_DATE_COLNAME="done_date";
	public static final String DONE_BY_COLNAME="done_by";
	public static final String ROLE_COLNAME="role";
	public static final String ROLE_ATTNAME="role";
	   
	public static final String OWNER_COLNAME="owner";
	public static final String FORM_COLNAME="form_class";//this column records the java classname of a form used to complete  this task.
	
	//Strings related to the tasks-permissions table specification.
	public static final String TPTABLENAME="bx_tasks_permissions";
	public static final String PERMISSION_COLNAME="permission";
	public static final String LEDGER_COLNAME="ledger";
	
	//Strings related to the delegates table specification.
	public static final String DELEGATES_TABLENAME="bx_tasks_permissions";
	public static final String DELEGATE_COLNAME="delegate";
	public static final String RANK_COLNAME="rank";
	
	static Logger logger = Logger.getLogger("boox");
	
	//Fields
	private Table table;
	private String sysname;
	private String name;
	private String description;
	private Enterprise enterprise;
	private ISODate dueDate;
	private ISODate doneDate;
	private Role role;
	private Clerk owner;
	private int frequency;
	private List<Clerk> delegates=new ArrayList<Clerk>();
	private String formClassName;
	private String permissionsTable;
	private Map<Ledger, Permission> necessaryPermissions=new HashMap<Ledger,Permission>();
	
	
	
	private Task(Enterprise enterprise, Clerk clerk, String sysname) {
		this.enterprise=enterprise;
		try{
			Row row =table.getRow(sysname);
			this.name=row.getString(TASKNAME_COLNAME);
			this.description=row.getString(TASKDESCRIPTION_COLNAME);
			this.owner=Clerk.getClerk(enterprise, row.getString(OWNER_COLNAME));
			this.role=Role.getRole(row.getString(ROLE_COLNAME));
			this.frequency=row.getInt(FREQUENCY_COLNAME);
			this.formClassName=row.getString(FORM_COLNAME);
			this.dueDate=new ISODate(row.getDate(DUE_DATE_COLNAME).getTime());
		}catch(Exception x){
			//TODO
		}
	}
	/**
	 */
    public  boolean complete(Enterprise enterprise, TaskCompleted taskCompleted) throws TaskException {
    	if (!(taskCompleted.getSysname().equals(this.sysname))){throw new TaskException ("tasks don't match");}
    	if (taskCompleted.isSuccessful()){
    		Table taskTable = getTasksTable(enterprise);
	    	try{
	    		taskTable.amend(taskCompleted.getSysname(), DONE_DATE_COLNAME, taskCompleted.getDate());
	    		taskTable.amend(taskCompleted.getSysname(), DONE_BY_COLNAME, taskCompleted.getClerkSysname());
	    		//TODO: add the next iteration of this task to the tasks table.
	    		Date nextDate=new ISODate();
	    		int frequency = (int) taskTable.readLong(taskCompleted.getSysname(), FREQUENCY_COLNAME);
	            switch (frequency){
	 	           case Task.ONCE: return true;
	 	           case Task.DAILY: nextDate=dueDate.getDayAhead(); break;
	 	           case Task.WEEKLY:nextDate=dueDate.getWeekAhead();break;
	 	           case Task.FORTNIGHTLY:nextDate=dueDate.getFortnightAhead();break;
	 	           case Task.FOURWEEKLY:nextDate=dueDate.getFourWeeksAhead();break;
	 	           case Task.MONTHLY:nextDate=dueDate.getMonthAhead();break;
	 	           case Task.PERIOD_END:nextDate=dueDate.getDayAhead();break;//todo
	 	           case Task.QUARTERLY:nextDate=dueDate.getDayAhead();break;//todo
	 	           case Task.ANNUAL:nextDate=dueDate.getYearAhead();break;
	 	           case Task.IRREGULAR:break;//todo
	 	           default: 
	            }
	    		return createNextTask(nextDate);
	    	}catch(Exception ex){
	    		return false;
	    	}
    	}else{
    		return false;
    	}
    }
	public Map<Ledger, Permission> getNecessaryPermissions(	Enterprise enterprise, Clerk clerk) {
		 return necessaryPermissions;
	}
	/**
	 * Adds a delegate for this task
	 * @param enterprise
	 * @param owner
	 * @param delegate
	 * @param rank
	 * @return
	 */
	public boolean addDelegate (Enterprise enterprise, Clerk owner, Clerk delegate, int rank) {
		//TODO first grant the necessary permissions for this delegate
		//Adds the delegate to the delegates table,
		Table delTable=getDelegatesTable(enterprise);
		String[] vals={sysname, delegate.getName()};
		String[] cols={SYSNAME_COLNAME, DELEGATE_COLNAME};
		try {
			delTable.addRow(cols, vals);
		} catch (PlatosysDBException e) {
			return false;
		}//now  the necessary permissions:
		return true;
	}
	public boolean removeDelegate (Enterprise enterprise, Clerk owner, Clerk delegate) {
		// TODO Auto-generated method stub
		return false;
	}
	public String getName() {
		return name;
	}

	public Clerk getOwner() {
		return owner;
	}

	public List<Clerk> getDelegates() {
		return delegates;
	}

	public Date getDueDate() {
		return dueDate;
	}
   
	private boolean createNextTask(Date dueDate){
		return (createTask(enterprise,  owner, this.name,  description,frequency, role,  dueDate,  formClassName,  necessaryPermissions)!=null);
		
	}
	/** This is used to create tasks from the data in the xml task definition files. It is called by the Boox.createTasks(Element element) method.
     * Tasks created by this method are given a due date of the end of the current month.
     * @param enterprise
     * @param owner
     * @param taskname
     * @param taskdescription
     * @param frequency
     * @param role
     * @param permissions
     * @return*/
	public static final Task createTask(Enterprise enterprise, Clerk owner, String taskname, String taskdescription, int frequency, Role role, String formClassName, Map<Ledger, Permission> permissions){
		Date dueDate = ISODate.getEndOfMonth(new ISODate());//Due date defaults to end of current month.
		return createTask( enterprise,  owner, taskname,  taskdescription,frequency, role,  dueDate,  formClassName,  permissions);
	}	
	
	
		private static final Task createTask(Enterprise enterprise, 
				Clerk owner,
				String taskname, 
				String taskdescription, 
				int frequency, 
				Role role, 
				Date dueDate, 
				String formClassName,
				Map<Ledger, Permission> permissions){
	  String sysname= ShortHash.hash(taskname+taskdescription);
	  sysname=SYSNAME_PREFIX+sysname;
	  try{
		  //the tasks table
		  Table tasksTable=getTasksTable(enterprise);
		  String[] cols = {Task.SYSNAME_COLNAME, 
				  		  Task.TASKNAME_COLNAME, 
				  		  Task.TASKDESCRIPTION_COLNAME, 
				  		  Task.OWNER_COLNAME, 
				  		  Task.ROLE_COLNAME,
				  		  Task.FORM_COLNAME
				  		  };
		  String[] vals= {sysname, taskname, taskdescription, owner.getName(), role.getName(), formClassName};		  		 
		  tasksTable.addRow(cols, vals);
		  tasksTable.amend(sysname, Task.FREQUENCY_COLNAME, frequency);//integer parameter
		  tasksTable.amend(sysname, Task.DUE_DATE_COLNAME, dueDate);//Due date defaults to end of current month.
			  //the permissions are stored in a separate table.
		  Table tpTable=getTasksPermissionsTable(enterprise);
		  String[] ncols={Task.SYSNAME_COLNAME, Task.LEDGER_COLNAME, Task.PERMISSION_COLNAME};
		  for(Ledger ledger: permissions.keySet()){
			  String[] nvals={sysname, ledger.getFullName(), permissions.get(ledger).getName()};
			  tpTable.addRow(ncols, nvals);
		  }
	  }catch(Exception x){
		  logger.log("problem with creating a task", x);
	  }
	  return new Task(enterprise, owner, sysname);
    }
	public static final Task getTask(Enterprise enterprise, Clerk clerk, String sysname){
		return new  Task(enterprise, clerk, sysname);
	}


	/**	 * This allows the Task framework to record the classname of a form used to complete the task. Boox is agnostic 
	 * about the higher-level framework which will vary by implementation.  If using (say) GWT for a web-application, 
	 * this would be the classname of a GWT form to be used on the client side. Your app can then use Class.forName(classname) to
	 * instantiate the right form for each Task.
	 * @return
	 */
  	public void setFormClassName(String formClassName) {
  		this.formClassName=formClassName;
	}


  	public String getFormClassName() {
		return formClassName;
  	}
  	
  	/** Returns the tasks table
  	 * @param enterprise
  	 * @return  	 */
  	private static Table getTasksTable(Enterprise enterprise){
  		try{
  			  JDBCTable tasksTable;
			  if(! JDBCTable.tableExists(enterprise.getDatabaseName(), Task.TABLENAME)){
				  tasksTable=JDBCTable.createTable(enterprise.getDatabaseName(), Task.TABLENAME, Task.SYSNAME_COLNAME, Table.TEXT_COLUMN);
	            tasksTable.addColumn(Task.TASKNAME_COLNAME, Table.TEXT_COLUMN);
	            tasksTable.addColumn(Task.TASKDESCRIPTION_COLNAME, Table.TEXT_COLUMN);
	            tasksTable.addColumn(Task.OWNER_COLNAME, Table.TEXT_COLUMN);
	            tasksTable.addColumn(Task.ROLE_COLNAME, Table.TEXT_COLUMN);
	            tasksTable.addColumn(Task.FREQUENCY_COLNAME, Table.INTEGER_COLUMN);
	            tasksTable.addColumn(Task.DUE_DATE_COLNAME, Table.DATE_COLUMN);
	            tasksTable.addColumn(Task.DONE_DATE_COLNAME, Table.DATE_COLUMN);
	            tasksTable.addColumn(Task.DONE_BY_COLNAME, Table.TEXT_COLUMN);
		        tasksTable.addColumn(Task.FORM_COLNAME, Table.TEXT_COLUMN);
			  }else{
				  tasksTable=JDBCTable.getTable(enterprise.getDatabaseName(), TABLENAME, Task.SYSNAME_COLNAME);
			  }
			  return tasksTable;
  		}catch(Exception ex){
  			logger.log("problem getting the tasks table)", ex);
  			return null;
  		}
  	}
  	
  	/**returns the tasks/permissions table
  	 * @param enterprise
  	 * @return  	 */
  	private static Table getTasksPermissionsTable(Enterprise enterprise){
	  	try{	 
	  		 JDBCTable tpTable;
			 if(!JDBCTable.tableExists(enterprise.getDatabaseName(), Task.TPTABLENAME)){
				  tpTable=JDBCTable.createForeignKeyTable(enterprise.getDatabaseName(), Task.TPTABLENAME,Task.SYSNAME_COLNAME,  Task.TABLENAME);
				  tpTable.addColumn(PERMISSION_COLNAME, Table.TEXT_COLUMN);
				  tpTable.addColumn(LEDGER_COLNAME, Table.TEXT_COLUMN);
			  }else{
				  tpTable=JDBCTable.getTable(enterprise.getDatabaseName(), Task.TPTABLENAME);
			  }
			 return tpTable;
	  	}catch(Exception ex){
  			logger.log("problem getting the tasks-permissions table)", ex);
  			return null;
  		}
  	}
 	/**returns the delegates table
  	 * @param enterprise
  	 * @return  	 */
  	private static Table getDelegatesTable(Enterprise enterprise){
	  	try{	 
	  		 JDBCTable delTable;
			 if(!JDBCTable.tableExists(enterprise.getDatabaseName(), Task.DELEGATES_TABLENAME)){
				 delTable=JDBCTable.createForeignKeyTable(enterprise.getDatabaseName(), Task.DELEGATES_TABLENAME,Task.SYSNAME_COLNAME,  Task.TABLENAME);
				 delTable.addColumn(DELEGATE_COLNAME, Table.TEXT_COLUMN);
				 delTable.addColumn(RANK_COLNAME, Table.INTEGER_COLUMN);
			  }else{
				  delTable=JDBCTable.getTable(enterprise.getDatabaseName(), Task.TPTABLENAME);
			  }
			 return delTable;
	  	}catch(Exception ex){
  			logger.log("problem getting the tasks-permissions table)", ex);
  			return null;
  		}
  	}
}
