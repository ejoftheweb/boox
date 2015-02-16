package uk.co.platosys.platax.client.reports;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.components.PTab;
import uk.co.platosys.platax.client.forms.AbstractForm;
import uk.co.platosys.platax.client.services.LedgerService;
import uk.co.platosys.platax.client.services.LedgerServiceAsync;
import uk.co.platosys.platax.shared.boox.GWTAuditLine;
import uk.co.platosys.pws.values.GWTMoney;


public class LedgerReport extends AbstractForm {
	private LedgerServiceAsync rlsa = GWT.create(LedgerService.class);
	private FlexTable table;
	public LedgerReport(Platax platax, String  enterpriseID, String ledgerName){
		super( platax, ledgerName);
		 
		table = new FlexTable();
		table.setText(0,1, ledgerName);
		table.setText(0,2, "Credits");
		table.setText(0,3, "Debits");
		table.setText(1,1, "Balance");
	    refresh(enterpriseID, ledgerName);
		
	}
	  private void refresh(String enterpriseID, String ledgerName) {
		    // Initialize the service proxy.
		    if (rlsa == null) {
		      rlsa = GWT.create(LedgerService.class);
		    }

		    // Set up the callback object.
		    AsyncCallback<GWTMoney> callback = new AsyncCallback<GWTMoney>() {
		      public void onFailure(Throwable caught) {
		        // TODO: Do something with errors.
		      }

		      public void onSuccess(GWTMoney balance) {
		    	  if (balance.credit()){
		    		  table.setText(1, 2, balance.toUnsignedString());
		    	  }else {
		    		  table.setText(1,3, balance.toUnsignedString());
		    	  }
		      }
		    };
            rlsa.readBalance(enterpriseID, ledgerName, callback);
            
		    // Set up the callback object.
		    AsyncCallback<ArrayList<GWTAuditLine>> callback1 = new AsyncCallback<ArrayList<GWTAuditLine>>() {
		      public void onFailure(Throwable caught) {
		        // TODO: Do something with errors.
		      }

		      public void onSuccess(ArrayList<GWTAuditLine> result) {
		        Iterator<GWTAuditLine> it = result.iterator();
		        int i=1;
		        while (it.hasNext()){
		        	GWTAuditLine line = it.next();
		        	i++;
		        	table.setText(i, 0, line.getDate().toString());
		        	table.setText(i,1, line.getName());
		        	if (line.getBalance().credit()){
		        		table.setText(i, 2, line.getBalance().toUnsignedString());
		        	}else{
		        		table.setText(i, 3, line.getBalance().toUnsignedString());
		        	}
		        }
		      }
		    };
		    // Make the call to the stock price service.
		    rlsa.readBalance(enterpriseID, ledgerName, callback);
		    rlsa.audit(enterpriseID, ledgerName, callback1);
		  }
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}}
	
