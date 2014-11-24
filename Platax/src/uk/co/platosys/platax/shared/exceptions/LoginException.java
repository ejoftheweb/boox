package uk.co.platosys.platax.shared.exceptions;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LoginException extends Exception implements Serializable, IsSerializable {
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
