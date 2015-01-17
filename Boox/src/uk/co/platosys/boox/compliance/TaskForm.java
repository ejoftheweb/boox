package uk.co.platosys.boox.compliance;

import java.io.Serializable;

/**
 * Interface that must be implemented by forms used to complete Tasks.
 * On successful task completion,  forms should return a TaskCompleted object
 * 
 * 
 *  TaskForm extends Serializable so that forms can be implemented in GWT client code.
 *  
 * The point being that:
 * TaskList generates a dynamic menu, from which the user then links to the form applicable to 
 * the task in question. On successful completion of that Task, a TaskCompleted object is returned
 * to the TaskManager, which records the success in the TaskList. 
 * 
 * 
 * @author edward
 *
 */
public interface TaskForm extends Serializable {
	public void setTaskSysname(String taskSysname);
	public TaskCompleted getResult();
}
