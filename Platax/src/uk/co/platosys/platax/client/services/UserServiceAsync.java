package uk.co.platosys.platax.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {

	void registerUser(String email, String username, String password,
			String confirm,boolean accept, boolean investor, AsyncCallback<String> callback);

}
