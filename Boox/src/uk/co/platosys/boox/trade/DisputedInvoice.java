/**
 * 
 */
package uk.co.platosys.boox.trade;

import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;

/**
 * This extension of Invoice is for use when an invoice is disputed by a customer. 
 * It provides records and storage of correspondence relating to the dispute.
 * @author edward
 *
 */
public class DisputedInvoice extends Invoice {

	/**
	 * 
	 */
	public DisputedInvoice(Enterprise enterprise, Clerk clerk, String sysname) {
		super(enterprise,  sysname, clerk);
	}

}
