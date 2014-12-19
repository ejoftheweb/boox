package uk.co.platosys.platax.server.core;

import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.platax.shared.exceptions.PlataxException;
import uk.co.platosys.xservlets.Xservlet;

public abstract class Booxlet extends Xservlet {
	public static final String SESSION_ATTNAME="PlataxUser";
	
	public Enterprise getEnterprise(String enterpriseSysname) throws PlataxException{
		PlataxUser pxuser =  (PlataxUser) getSession().getAttribute(PlataxUser.SESSION_ATTNAME);
		logger.log("Booxlet user in session is "+pxuser.getUsername());
		return pxuser.getEnterprise(enterpriseSysname);
		
	}
	public Clerk getClerk(Enterprise enterprise) throws PlataxException{
		PlataxUser pxuser =  (PlataxUser) getSession().getAttribute(PlataxUser.SESSION_ATTNAME);
		return pxuser.getClerk(enterprise);
	}
	
	/*
	logger.log("ACSL calling add customer service "+enterpriseID+" for "+name);
	PlataxUser pxuser =  (PlataxUser) getSession().getAttribute("PlataxUser");
	logger.log("ACSL user in session is "+pxuser.getUsername());
	Enterprise enterprise = pxuser.getEnterprise(enterpriseID);
	logger.log("ACSL ent in session is "+enterprise.getName());
	Clerk clerk = pxuser.getClerk(enterprise);*/
	
}
