package uk.co.platosys.platax.client.services;

import uk.co.platosys.platax.shared.PXAddress;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AddressServiceAsync {
public void recordAddress(String[] fieldValues, AsyncCallback<PXAddress> callback);
public void getAddress(String addressID,AsyncCallback<PXAddress> callback );
}
