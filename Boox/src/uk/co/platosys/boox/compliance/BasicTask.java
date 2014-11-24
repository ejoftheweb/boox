package uk.co.platosys.boox.compliance;

import java.util.Date;
import java.util.List;
import java.util.Map;

import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.Permission;
import uk.co.platosys.db.Table;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.ShortHash;

public class BasicTask implements Task {
static Logger logger = Logger.getLogger("boox");
	private BasicTask(Enterprise enterprise, Clerk clerk, String sysname) {
		 
	}

	@Override
	public Map<Ledger, Permission> getNecessaryPermissions(	Enterprise enterprise, Clerk clerk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void grantNecessaryPermissions(Enterprise enterprise,Clerk supervisor, Clerk grantee) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clerk getOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Clerk> getDelegates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getNextDueDate() {
		// TODO Auto-generated method stub
		return null;
	}
  public static final Task createTask(Enterprise enterprise, Clerk owner, String taskname, String taskdescription, int frequency, Role role, Map<Ledger, Permission> permissions){
	  String sysname= ShortHash.hash(taskname+taskdescription);
	  sysname=SYSNAME_PREFIX+sysname;
	  try{
		  //we create the tasks table if it doesn't exist and put the task in it.
		  JDBCTable tasksTable;
		  JDBCTable tpTable;
		  if(! JDBCTable.tableExists(enterprise.getDatabaseName(), Task.TABLENAME)){
			  tasksTable=JDBCTable.createTable(enterprise.getDatabaseName(), Task.TABLENAME, Task.SYSNAME_COLNAME, Table.TEXT_COLUMN);
              tasksTable.addColumn(Task.TASKNAME_COLNAME, Table.TEXT_COLUMN);
              tasksTable.addColumn(Task.TASKDESCRIPTION_COLNAME, Table.TEXT_COLUMN);
              tasksTable.addColumn(Task.OWNER_COLNAME, Table.TEXT_COLUMN);
              tasksTable.addColumn(Task.ROLE_COLNAME, Table.TEXT_COLUMN);
              tasksTable.addColumn(Task.FREQUENCY_COLNAME, Table.INTEGER_COLUMN);
              tasksTable.addColumn(Task.LAST_DATE_COLNAME, Table.DATE_COLUMN);
              tasksTable.addColumn(Task.NEXT_DATE_COLNAME, Table.DATE_COLUMN);
              tasksTable.addColumn(Task.FORM_COLNAME, Table.TEXT_COLUMN);
		  }else{
			  tasksTable=JDBCTable.getTable(enterprise.getDatabaseName(), TABLENAME, Task.SYSNAME_COLNAME);
		  }
		  
		  String[] cols = {Task.SYSNAME_COLNAME, 
				  		  Task.TASKNAME_COLNAME, 
				  		  Task.TASKDESCRIPTION_COLNAME, 
				  		  Task.OWNER_COLNAME, 
				  		  Task.ROLE_COLNAME};
		  String[] vals= {sysname, taskname, taskdescription, owner.getName(), role.getName()};		  		 
		  tasksTable.addRow(cols, vals);
		  tasksTable.amend(sysname, Task.FREQUENCY_COLNAME, frequency);
		  ///now we need to create or open the task-permissions table:
		  if(!JDBCTable.tableExists(enterprise.getDatabaseName(), Task.TPTABLENAME)){
			  tpTable=JDBCTable.createForeignKeyTable(enterprise.getDatabaseName(), Task.TPTABLENAME,Task.SYSNAME_COLNAME,  Task.TABLENAME);
			  tpTable.addColumn(PERMISSION_COLNAME, Table.TEXT_COLUMN);
			  tpTable.addColumn(LEDGER_COLNAME, Table.TEXT_COLUMN);
		  }else{
			  tpTable=JDBCTable.getTable(enterprise.getDatabaseName(), Task.TPTABLENAME);
		  }
		  //and now to populate it:
		  String[] ncols={Task.SYSNAME_COLNAME, Task.LEDGER_COLNAME, Task.PERMISSION_COLNAME};
		  for(Ledger ledger: permissions.keySet()){
			  String[] nvals={sysname, ledger.getFullName(), permissions.get(ledger).getName()};
			  tpTable.addRow(ncols, nvals);
		  }
	  }catch(Exception x){
		  logger.log("problem with creating a task", x);
	  }
	  return new BasicTask(enterprise, owner, sysname);
  }
  public static final Task getTask(Enterprise enterprise, Clerk clerk, String sysname){
	  return new  BasicTask(enterprise, clerk, sysname);
  }

@Override
public void setFormClassName(String formClassName) {
	// TODO Auto-generated method stub
	
}

@Override
public String getFormClassName() {
	// TODO Auto-generated method stub
	return null;
}
}
