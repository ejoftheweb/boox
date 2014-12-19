package uk.co.platosys.platax.server.services;

import uk.co.platosys.platax.client.services.AddressService;
import uk.co.platosys.xservlets.Xservlet;
import uk.co.platosys.xuser.Xaddress;
import uk.co.platosys.xuser.XuserException;
import uk.co.platosys.platax.server.core.Booxlet;
import uk.co.platosys.platax.shared.*;

public class AddressServiceImpl extends Booxlet implements AddressService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8268342512681720881L;
	/**
	 * 
	 */

	@Override
	public PXAddress recordAddress(String[] vals) {
		try {
			Xaddress xaddress= new Xaddress(vals);
			PXAddress pxAddress =  new PXAddress(xaddress.getXaddressID(),xaddress.getFieldValues());
		   	return pxAddress;
		 } catch (XuserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public PXAddress getAddress(String xaddressid){
		Xaddress xaddress;
		try {
			xaddress = new Xaddress(xaddressid);
			PXAddress pxAddress =  new PXAddress(xaddress.getXaddressID(),xaddress.getFieldValues());
		   	return pxAddress;
		} catch (XuserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
}
