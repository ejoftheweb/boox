package uk.co.platosys.platax.server.services;

import uk.co.platosys.platax.client.services.UserService;
import uk.co.platosys.platax.server.core.PlataxUser;
import uk.co.platosys.platax.shared.Constants;
import uk.co.platosys.platax.shared.FieldVerifier;
import uk.co.platosys.util.Logger;
import uk.co.platosys.xservlets.Xservlet;
import uk.co.platosys.xuser.XuserException;
import uk.co.platosys.xuser.XuserExistsException;


public class UserServiceImpl extends Xservlet implements
		UserService {
/**
	 * 
	 */
	private static final long serialVersionUID = 5953214743330925557L;
Logger logger = Logger.getLogger("platax");
	

	@Override
	public String registerUser(String email, String username, String name, String password,
			String confirm, boolean accept, boolean investor) {
		logger.log("RUSI registration request received");
		String result="";
		boolean ok=true;
		if (!(FieldVerifier.isValidEmail(email))){
			result=result+" invalid email\n";
			ok=false;
		}
		if (!(FieldVerifier.isValidUsername(username))){
			result=result+" invalid username\n";
			ok=false;
		}
		if (!(FieldVerifier.isValidPassword(password))){
			result=result+" invalid username\n";
			ok=false;
		}
		if (!(FieldVerifier.confirms(password, confirm))){
			result=result+" passwords don't match\n";
			ok=false;
		}
		if(!ok){
			return result;
		}else{
			
		    char[] pword = password.toCharArray();
			try{
				logger.log("RUSI - inputs checked, now calling PU.register");
				PlataxUser.register(email, username, name, pword, investor);
				return Constants.OK;
			}catch (XuserException e) {
				logger.log("error",e);
				result=result+e.getMessage()+"\n";
				StackTraceElement[] st = e.getStackTrace();
				for (int i=0; i< st.length; i++){
					result=result+st[i].toString()+"\n";
				}
				
				return result;
			} catch (XuserExistsException e) {
				ok=false;
				result=result+"user already exists";
				return Constants.USER_EXISTS;
			}
			
		}
		
	}
   
}
