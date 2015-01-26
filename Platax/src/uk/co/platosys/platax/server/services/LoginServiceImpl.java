package uk.co.platosys.platax.server.services;

import uk.co.platosys.platax.client.services.LoginService;
import uk.co.platosys.platax.server.core.Booxlet;
import uk.co.platosys.platax.server.core.PlataxUser;
import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.exceptions.LoginException;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.xservlets.Xservlet;
import uk.co.platosys.xuser.XuserCredentialsException;


public class LoginServiceImpl extends Booxlet implements LoginService {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -738242988901516841L;
	private static final String USER_ATTNAME= "PlataxUser";

	public PXUser login(String email, String password)  {
		char[] pwd = password.toCharArray();
		password="";
		try {
			PlataxUser puser = PlataxUser.getPlataxUser(email, pwd, getThreadLocalRequest());
			getSession().setAttribute(USER_ATTNAME, puser);
			return puser.getPXUser();
		} catch(XuserCredentialsException xce){
			 logger.log("credentials exception");
			 return new PXUser();
		}catch(Exception x){
			
			logger.log("login exception for email"+email, x);
			return null;
		}
		 
	}

	@Override
	public boolean logout() {
		try{
			PlataxUser puser = (PlataxUser) getSession().getAttribute(USER_ATTNAME);
			puser.setAuthenticated(false);
			getSession().invalidate();
			//logger.log(puser.getUsername() +" logged out at "+new ISODate());
			return true;
		}catch(Exception e){
			logger.log("logout exception", e);
		}
		return false;
	} 
}
