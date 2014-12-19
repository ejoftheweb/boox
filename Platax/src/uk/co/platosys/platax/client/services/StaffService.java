package uk.co.platosys.platax.client.services;

import java.util.ArrayList;

import uk.co.platosys.platax.shared.boox.GWTCash;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEmployee;
import uk.co.platosys.platax.shared.boox.GWTRole;
import uk.co.platosys.platax.shared.exceptions.PlataxException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("staffService")
public interface StaffService extends RemoteService {
	/** Returns a list of employees in the given role
	 * @param enterpriseName
	 * @return */
	public ArrayList<GWTEmployee>getEmployees(String enterpriseName, GWTRole role);
	public GWTEmployee addEmployee(GWTEmployee employee, String enterpriseName);
	public GWTEmployee getEmployee(String sysname, String enterpriseName);
	
}
