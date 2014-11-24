package uk.co.platosys.platax.shared;

public class LoginException extends Exception {
	public LoginException(){
		super();
	}
   public LoginException (String msg){
	   super(msg);
   }
   public LoginException (String msg, Throwable cause){
	   super(msg, cause);
   }
}
