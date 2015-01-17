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
	public String getClerkSysname() {
		return clerkSysname;
	}
	public void setClerkSysname(String clerkSysname) {
		this.clerkSysname = clerkSysname;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getSysname() {
		return sysname;
	}
	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
	public boolean isSuccessful() {
		return successful;
	}
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
}
