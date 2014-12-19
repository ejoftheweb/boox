package uk.co.platosys.platax.client.services;

import java.util.ArrayList;

import uk.co.platosys.platax.shared.boox.GWTCash;
import uk.co.platosys.platax.shared.boox.GWTCustomer;
import uk.co.platosys.platax.shared.boox.GWTSelectable;
import uk.co.platosys.platax.shared.exceptions.PlataxException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("cashService")
public interface CashService extends RemoteService {
	public ArrayList<GWTSelectable>getCashRegisters(String enterpriseName);
	public ArrayList<GWTSelectable> getCashiers(String enterpriseName);
	public GWTCash getRegister(String cashRegisterID);
	public Boolean cashUp(GWTCash cashRegister, String enterpriseName) throws PlataxException;
	public GWTCash addCashRegister(GWTCash register, String enterpriseName) throws PlataxException;
	GWTCash addCRDept(GWTCash register, int deptNo, String deptName,int deptTaxBand);
	
}
