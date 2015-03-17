package uk.co.platosys.platax.client.services;

import uk.co.platosys.pws.values.PWSAddress;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AddressServiceAsync {
public void recordAddress(PWSAddress address, AsyncCallback<PWSAddress> callback);
public void getAddress(String addressID,AsyncCallback<PWSAddress> callback );
}
