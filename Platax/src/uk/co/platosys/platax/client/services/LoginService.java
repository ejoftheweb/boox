package uk.co.platosys.platax.client.services;

import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.exceptions.LoginException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("loginService")
public interface LoginService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static LoginServiceAsync instance;
		public static LoginServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(LoginService.class);
			}
			return instance;
		}
	}
	public PXUser login (String email, String password);//throws LoginException;
	public boolean logout();
}
