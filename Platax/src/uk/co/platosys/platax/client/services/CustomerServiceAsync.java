package uk.co.platosys.platax.client.services;

import java.util.ArrayList;

import uk.co.platosys.platax.shared.boox.GWTCustomer;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CustomerServiceAsync {
 void addCustomer(String enterpriseName, String customerName, String customerID, AsyncCallback<GWTCustomer> callback);
 void addCustomer(String enterpriseName, String customerName, boolean isPrivate,  AsyncCallback<GWTCustomer> callback);
 void editCustomer(GWTCustomer customer, AsyncCallback<GWTCustomer> callback);
 void checkName(String customerName, AsyncCallback<GWTCustomer> callback);
 void listCustomers(String enterpriseID, int selection, AsyncCallback<ArrayList<GWTCustomer>> callback);
}