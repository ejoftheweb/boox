package uk.co.platosys.platax.server.services;

import java.util.Iterator;
import java.util.List;

import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.AuditElement;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.platax.server.core.Booxlet;
import uk.co.platosys.platax.shared.boox.GWTAccount;

public class AccountServiceImpl extends Booxlet{

	public AccountServiceImpl() {
		// TODO Auto-generated constructor stub
	}
    protected static GWTAccount convert(Account account, Enterprise enterprise, Clerk clerk) throws PermissionsException, Exception{
		 GWTAccount gAccount = new GWTAccount();
		 gAccount.setName(account.getName());
		 gAccount.setFullname(account.getFullName());
		 List<AuditElement> lines = account.audit(enterprise, clerk);//this  will return only the information the clerk is entitled to see. 
		 Iterator<AuditElement> lit= lines.iterator();{
			 while (lit.hasNext()){
				 gAccount.addLine(AuditServiceImpl.convert(lit.next(), enterprise, clerk));
			 }
		 }
		return gAccount;
    	
    }
}
