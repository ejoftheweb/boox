package uk.co.platosys.platax.client.services;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTItem;
import uk.co.platosys.platax.shared.boox.GWTTask;
import uk.co.platosys.pws.values.GWTMoney;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("taskService")
public interface TaskService extends RemoteService {
	public List<GWTTask> listTasks(PXUser puser);
}
