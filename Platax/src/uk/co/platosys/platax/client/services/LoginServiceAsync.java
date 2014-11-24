package uk.co.platosys.platax.client.services;

import uk.co.platosys.platax.shared.PXUser;
import uk.co.platosys.platax.shared.exceptions.LoginException;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
   void login (String email, String password, AsyncCallback<PXUser> callback);// throws LoginException;
   void logout (AsyncCallback<Boolean> callback);
}
