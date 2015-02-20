package uk.co.platosys.platax.client.services;

import java.util.List;

import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTTask;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("taskService")
public interface TaskService extends RemoteService {
	public List<GWTTask> listTasks(PXUser puser);
}
