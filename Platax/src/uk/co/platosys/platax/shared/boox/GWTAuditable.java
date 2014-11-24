package uk.co.platosys.platax.shared.boox;

import java.util.Date;
import java.util.List;

import com.google.gwt.view.client.ProvidesKey;
/**
 * GWT client-side equivalent of the Boox Auditable interface
 * @author edward
 *
 */


public interface GWTAuditable  extends ProvidesKey<GWTAuditable>{
    /**
     * the basic audit method
     * @return a List of <GWTAuditElements>
     */
	public List<GWTAuditElement> audit();
	public GWTAuditLine getBalanceLine();
	
	
    public String getName();
    public GWTMoney getBalance();
    public Date getDate();
    
}
