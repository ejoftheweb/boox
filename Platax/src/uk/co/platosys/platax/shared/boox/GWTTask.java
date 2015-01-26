package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;
import java.util.Date;

public class GWTTask implements Serializable, Comparable<GWTTask> {
 
	private String name;
	private String description;
	private Date dueDate;
	private String menuAction;
	private GWTEnterprise enterprise;
	
	public GWTTask (){}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getMenuAction() {
		return menuAction;
	}

	public void setMenuAction(String menuAction) {
		this.menuAction = menuAction;
	}

	@Override
	public int compareTo(GWTTask task) {
	if (task.getDueDate().before(dueDate)){
		return 1;
	}else if (task.getDueDate().after(dueDate)){
		return -1;
	}else{
		return 0;
	}
	}

	public GWTEnterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(GWTEnterprise enterprise) {
		this.enterprise = enterprise;
	}

	
 
}
