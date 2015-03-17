package uk.co.platosys.platax.server.services;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.boox.cash.Cash;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.Transaction;
import uk.co.platosys.boox.money.Bank;
import uk.co.platosys.platax.client.services.BankService;
import uk.co.platosys.platax.client.services.CashService;
import uk.co.platosys.platax.server.core.Booxlet;
import uk.co.platosys.platax.server.core.PlataxServer;
import uk.co.platosys.platax.shared.boox.GWTBank;
import uk.co.platosys.platax.shared.boox.GWTCash;
import uk.co.platosys.platax.shared.boox.GWTHasAccount;
import uk.co.platosys.platax.shared.boox.GWTSelectable;
import uk.co.platosys.platax.shared.exceptions.PlataxException;
import uk.co.platosys.pws.values.GWTMoney;

public class BankServiceImpl extends Booxlet implements BankService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public GWTBank convert(Bank cash){
		GWTBank gBank=new GWTBank();
		gBank.setName(cash.getName());
		gBank.setSysname(cash.getSysname());
		gBank.setBalance(PlataxServer.convert(cash.getBalance()));
		return gBank;
	}

	@Override
	public List<GWTBank> getBanks(String enterpriseSysname) {
		try {
			List<GWTBank> gBanks=new ArrayList<GWTBank>();
			Enterprise enterprise=getEnterprise(enterpriseSysname);
			Clerk clerk= getClerk(enterprise);
			List<Bank> banks=Bank.getBanks(enterprise, clerk);
			for(Bank bank:banks){
				gBanks.add(convert(bank));
			}
			return gBanks;
		} catch (PlataxException e) {
			 logger.log("BSIgetBanks(); error", e);
			 return null;
		}
	}

	@Override
	public GWTBank getBank(String enterpriseSysname, String cashRegisterID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GWTBank addBankAccount(GWTBank gbank, String enterpriseName)throws PlataxException {
		Enterprise enterprise=getEnterprise(enterpriseName);
		Clerk clerk= getClerk(enterprise);
		if (checkName(enterpriseName, gbank.getName())){
			try{
				Bank bank = Bank.createBank(enterprise, clerk, gbank.getName(), gbank.getSortcode(), gbank.getAccno(), gbank.getIban());
				bank.setAddressID(gbank.getAddress().getxAddressID());
				bank.setLowlimit(PlataxServer.convert(gbank.getLowlimit()));
				return convert(bank);
			}catch(Exception x){
				logger.log("BSI.addBank()error: ",x);
			}
		}
		return null;
		
	}
	
    public boolean checkName(String enterpriseName, String bankName){
    	try{
	    	Enterprise enterprise=getEnterprise(enterpriseName);
			Clerk clerk= getClerk(enterprise);
	    	return Bank.checkName(enterprise,clerk,bankName);
    	}catch(Exception x){
    		return false;
    	}
    }

	@Override
	public Long pay(String enterpriseSysname, String debit, String credit, GWTMoney amount, String note) {
		try{
			Enterprise enterprise=getEnterprise(enterpriseSysname);
			Clerk clerk= getClerk(enterprise);
			Transaction transaction = new Transaction(enterprise,
													  clerk,
													  PlataxServer.convert(amount),
													  credit,
													  debit,
													  note,
													  0);
			return transaction.post();
		}catch(Exception x){
			return null;
		}
	}
	
	
	}
