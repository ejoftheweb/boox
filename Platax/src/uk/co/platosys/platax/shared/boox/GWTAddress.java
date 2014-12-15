package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

/**
 * Class to wrap a geographical address
 * @author edward
 *
 */
public class GWTAddress implements Serializable {
	String postCode;
	String building;
	String noInBuilding;
	
	public GWTAddress(){}
}
