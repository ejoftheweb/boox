package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

import uk.co.platosys.platax.client.forms.fields.IsFieldValue;

/**
 * Class to wrap a geographical address
 * @author edward
 *
 */
public class GWTAddress implements Serializable, IsFieldValue<GWTAddress> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String postCode;
	String building;
	String noInBuilding;
	
	public GWTAddress(){}
}
