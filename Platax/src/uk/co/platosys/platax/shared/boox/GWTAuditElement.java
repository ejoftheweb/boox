package uk.co.platosys.platax.shared.boox;

import java.util.Date;

import com.google.gwt.view.client.ProvidesKey;



public interface GWTAuditElement extends ProvidesKey<GWTAuditable> {
    public GWTAuditLine getBalanceLine();
    public String getName();
    public GWTMoney getBalance();
    public Date getDate();
    
}
