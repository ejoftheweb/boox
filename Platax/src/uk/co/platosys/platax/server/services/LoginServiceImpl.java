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
	

	public PXUser login(String email, String password)  {
		char[] pwd = password.toCharArray();
		password="";
		try {
			logger.log("login request received");
			PlataxUser puser = PlataxUser.getPlataxUser(email, pwd, getThreadLocalRequest());
			logger.log("LISI has got user "+puser.getUsername());
			getSession().setAttribute("PlataxUser", puser);
			logger.log("LISI logged "+puser.getUsername()+" in session");
			logger.log("LISI puser has xuserid "+puser.getXuserID());
			PXUser pxuser = puser.getPXUser();
			logger.log("LISI - PXuser is "+ pxuser.getUsername());
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
			PlataxUser puser = (PlataxUser) getSession().getAttribute("PlataxUser");
			puser.setAuthenticated(false);
			getSession().invalidate();
			logger.log(puser.getUsername() +" logged out at "+new ISODate());
			return true;
		}catch(Exception e){
			logger.log("logout exception", e);
		}
		return false;
	} 
}
