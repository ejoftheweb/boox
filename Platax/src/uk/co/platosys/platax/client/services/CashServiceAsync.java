package uk.co.platosys.platax.client.services;

import java.util.ArrayList;

import uk.co.platosys.platax.shared.boox.GWTCash;
import uk.co.platosys.platax.shared.boox.GWTSelectable;
import uk.co.platosys.platax.shared.exceptions.PlataxException;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface CashServiceAsync {
	public void getCashRegisters(String enterpriseSysname, AsyncCallback<ArrayList<GWTSelectable>> callback);
	public void getCashiers(String enterpriseSysname, AsyncCallback<ArrayList<GWTSelectable>> callback);
	public void getRegister(String cashRegisterID, AsyncCallback<GWTCash> callback);
	public void cashUp(GWTCash cashRegister, String enterpriseSysname, AsyncCallback<Boolean> callback) throws PlataxException;
	public void addCashRegister(GWTCash register, String enterpriseSysname, AsyncCallback<GWTCash> callback)throws PlataxException;
	public void addCRDept(GWTCash register, int deptNo, String deptName,int deptTaxBand, AsyncCallback<GWTCash> callback);
}
