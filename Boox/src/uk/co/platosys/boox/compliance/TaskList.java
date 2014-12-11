package uk.co.platosys.boox.compliance;

import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;

public class TaskList {

	public TaskList(Enterprise enterprise, Clerk clerk) {
		// TODO Auto-generated constructor stub
	}
   public static TaskList getTaskList(Enterprise enterprise, Clerk clerk){
	   return new TaskList(enterprise, clerk);
   }
}
