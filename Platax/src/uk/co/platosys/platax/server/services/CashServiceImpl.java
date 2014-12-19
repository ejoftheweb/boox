package uk.co.platosys.platax.server.services;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.boox.cash.Cash;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.platax.client.services.CashService;
import uk.co.platosys.platax.server.core.Booxlet;
import uk.co.platosys.platax.server.core.PlataxServer;
import uk.co.platosys.platax.shared.boox.GWTCash;
import uk.co.platosys.platax.shared.boox.GWTSelectable;
import uk.co.platosys.platax.shared.exceptions.PlataxException;

public class CashServiceImpl extends Booxlet implements CashService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ArrayList<GWTSelectable> getCashRegisters(String enterpriseSysname) {
		try{
			Enterprise enterprise=getEnterprise(enterpriseSysname);
			Clerk clerk=getClerk(enterprise);
			List<Cash> cashes=Cash.getCashes(enterprise, clerk);
			ArrayList<GWTSelectable> registers=new ArrayList<GWTSelectable>();
			for(Cash cash:cashes){
				registers.add(convert(cash));
			}
			return registers;
		}catch(Exception e){
			logger.log("CashSI: problem getting registers", e);
			return null;
		}
	}

	@Override
	public ArrayList<GWTSelectable> getCashiers(String enterpriseName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GWTCash getRegister(String cashRegisterID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean cashUp(GWTCash cashRegister, String enterpriseSysname) throws PlataxException {
		// TODO Auto-generated method stub
		return null;
	}
   
	public GWTCash convert(Cash cash){
		GWTCash gCash=new GWTCash();
		gCash.setName(cash.getName());
		gCash.setSysname(cash.getSysname());
		gCash.setFloatbal(PlataxServer.convert(cash.getFloatbal()));
		gCash.setRunningTotal(PlataxServer.convert(cash.getGt()));
		return gCash;
	}

	@Override
	public GWTCash addCashRegister(GWTCash register, String enterpriseSysname) throws PlataxException {
		try{
			Enterprise enterprise=getEnterprise(enterpriseSysname);
			Cash cash=Cash.addCashmc(enterprise, getClerk(enterprise), Ledger.getLedger(enterprise, Cash.CASH_LEDGER_NAME), register.getName());
			cash.setGt(PlataxServer.convert(register.getRunningTotal()));
			cash.setFloatbal(PlataxServer.convert(register.getFloatbal()));
			cash.setReportNo(register.getSeqno());
			cash.setModel(register.getModel());
			register.setSysname(cash.getSysname());
			return register;
		}catch(Exception x){
			logger.log("CshSI: problem adding register", x);
			return null;
		}
	}
	@Override
	public GWTCash addCRDept(GWTCash register, int deptNo, String deptName, int deptTaxBand){
		return null;
	}
}
