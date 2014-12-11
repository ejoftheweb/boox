package uk.co.platosys.boox.compliance;

import java.io.Serializable;
import java.util.Date;


/**
 * Class returned by instances of TaskForm. 
 * 
 * @author edward
 *
 */

public class TaskCompleted implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String clerkSysname;
	Date date;
	String sysname;
	boolean successful;
	public TaskCompleted(){}
	public TaskCompleted (String clerkSysname, Date date, String sysname, boolean successful){
		this.clerkSysname=clerkSysname;
		this.date=date;
		this.sysname=sysname;
		this.successful=successful;
	}
}
