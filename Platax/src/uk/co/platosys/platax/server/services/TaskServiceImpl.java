package uk.co.platosys.platax.server.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import uk.co.platosys.boox.compliance.Task;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.platax.client.services.TaskService;
import uk.co.platosys.platax.server.core.PlataxUser;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTTask;
import uk.co.platosys.platax.shared.exceptions.PlataxException;
import uk.co.platosys.xservlets.Xservlet;

public class TaskServiceImpl extends Xservlet implements TaskService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public List<GWTTask> listTasks(PXUser puser)  {
		logger.log("TSI getting tasks for user "+puser.getUsername());
		PlataxUser pxuser =  (PlataxUser) getSession().getAttribute("PlataxUser");
		List<GWTTask> gtasks=new ArrayList<GWTTask>();
		List<Task> tasks=new ArrayList<Task>();
		Collection<Enterprise> enterprises = pxuser.getEnterprises();
		logger.log("TSI user " + pxuser.getUsername() +" has "+ enterprises.size()+" enterprises");
		for(Enterprise enterprise:enterprises){
			Clerk clerk;
			try {
				clerk = pxuser.getClerk(enterprise);
				GWTEnterprise gEnterprise=EnterpriseServiceImpl.convert(enterprise, clerk);
				tasks=(Task.getTasks(enterprise, clerk));
				for (Task task:tasks){
					gtasks.add(convert(task, gEnterprise));
				}
			} catch (PlataxException e) {
				e.printStackTrace();
			}
		}
		logger.log("TSI we have "+tasks.size()+ "Tasks");
		
		logger.log("TSI we have "+gtasks.size()+"gtasks");
		try{
			Collections.sort(gtasks);
		}catch(Exception t){
			logger.log("TSI sort error", t);
		}
		return gtasks;
	}
    private GWTTask convert(Task task, GWTEnterprise enterprise){
    	 GWTTask gTask = new GWTTask();
    	 gTask.setName(task.getName());
    	 gTask.setDescription(task.getName());
    	 if (task.getDueDate()==null){
    		 gTask.setDueDate(new Date());
    	 }else{
    		  gTask.setDueDate(new Date(task.getDueDate().getTime()));
    	 }
    	 gTask.setEnterprise(enterprise);
    	 gTask.setMenuAction(task.getFormClassName());
    	return gTask;
    }
}
