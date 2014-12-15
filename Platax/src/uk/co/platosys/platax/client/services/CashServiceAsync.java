package uk.co.platosys.platax.client.services;

import java.util.ArrayList;

import uk.co.platosys.platax.shared.boox.GWTCash;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.exceptions.PlataxException;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


public interface CashServiceAsync {
	public void getCashRegisters(String enterpriseName, AsyncCallback<ArrayList<String>> callback);
	public void getCashiers(String enterpriseName, AsyncCallback<ArrayList<String>> callback);
	public void getRegister(String cashRegisterID, AsyncCallback<GWTCash> callback);
	public void cashUp(GWTCash cashRegister, AsyncCallback<Boolean> callback) throws PlataxException;
}
