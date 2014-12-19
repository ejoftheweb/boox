package uk.co.platosys.platax.client.services;

import java.util.ArrayList;

import uk.co.platosys.platax.shared.boox.GWTCash;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTEmployee;
import uk.co.platosys.platax.shared.boox.GWTRole;
import uk.co.platosys.platax.shared.exceptions.PlataxException;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


public interface StaffServiceAsync {
	public void getEmployees(String enterpriseName, GWTRole role, AsyncCallback<ArrayList<GWTEmployee>> callback);
	public void addEmployee(GWTEmployee employee, String enterpriseName,  AsyncCallback<GWTEmployee> callback);
	public void getEmployee(String sysname, String enterpriseName, AsyncCallback<GWTEmployee> callback);
}
