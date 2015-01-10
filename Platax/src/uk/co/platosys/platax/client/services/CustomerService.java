package uk.co.platosys.platax.client.services;

import java.util.ArrayList;

import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.exceptions.PlataxException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("customerService")
public interface CustomerService extends RemoteService {
	public GWTCustomer addCustomer(String enterpriseName, String name, String customerID);
	public GWTCustomer addCustomer(String enterpriseName, String name, boolean trade) throws PlataxException;
	public GWTCustomer editCustomer(GWTCustomer customer);
	public GWTCustomer checkName(String customerName);
	public ArrayList<GWTCustomer> listCustomers(String enterpriseID, int selection );
}
