package uk.co.platosys.platax.client.services;

import uk.co.platosys.pws.values.PWSAddress;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("addressService")
public interface AddressService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static AddressServiceAsync instance;
		public static AddressServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(AddressService.class);
			}
			return instance;
		}
	}
	/**
	 * this method returns an identifier for the address;
	 * @param vals
	 * @return
	 */
	public PWSAddress recordAddress(PWSAddress address);
	/**
	 * this method returns the address itself, formatted for
	 * one or multiple lines.
	 * @param xaddressid
	 * @param oneline
	 * @return
	 */
	public PWSAddress getAddress(String xaddressid);
}
