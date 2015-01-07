package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.platosys.pws.values.GWTMoney;

import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.TreeViewModel;
/**
 * GWTLedger implements TreeViewModel directly. Boox ledgers are tree-like.
 * 
 * 
 * @author edward
 *
 */


public class GWTLedger extends AbstractDataProvider<GWTAuditable> implements TreeViewModel, GWTAuditable, Serializable {
	ArrayList <GWTAuditable> children = new ArrayList<GWTAuditable>();
	ArrayList <GWTLedger> childLedgers= new ArrayList<GWTLedger>();
	ArrayList <GWTAccount> accounts = new ArrayList<GWTAccount>();
    String parentName;
    GWTMoney balance=new GWTMoney();
    String name;
    	private final ListDataProvider<GWTAuditable> ledgerDataProvider=new ListDataProvider<GWTAuditable>(){
    		
    	};
	  private final DefaultSelectionEventManager<GWTAuditLine> selectionManager = DefaultSelectionEventManager.createCheckboxManager();
	  private final MultiSelectionModel<GWTAuditable> selectionModel  = new MultiSelectionModel<GWTAuditable>(ledgerDataProvider);
    
    
	public GWTLedger() {
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {
              StringBuilder sb = new StringBuilder();
              boolean first = true;
              List<GWTAuditable> selected = new ArrayList<GWTAuditable>(
                  selectionModel.getSelectedSet());
              //Collections.sort(selected);
              for (GWTAuditable value : selected) {
                if (first) {
                  first = false;
                } else {
                  sb.append(", ");
                }
                sb.append(value.getName());
              }
              //selectedLabel.setText(sb.toString());
            }
          }); 
	}

	
	public GWTAuditLine getBalanceLine(){
		return new GWTAuditLine(balance, name, new Date());
	}
    public void addLedger(GWTLedger ledger) throws Exception{
    	children.add(ledger);
    	childLedgers.add(ledger);
    	balance = balance.add(ledger.getBalance());
    }
    public void addAccount(GWTAccount account) throws Exception{
    	children.add(account);
    	accounts.add(account);
    	balance=balance.add(account.getBalance());
    }
	public GWTMoney getBalance() {
		return balance;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
    public ArrayList<GWTAuditable> getChildren(){
    	return children;
    }
    public ArrayList<GWTLedger> getChildLedgers(){
    	return childLedgers;
    }

    /**
     * method specified to return the children of this value. 
     */
	@Override
	public <GWTAuditable> NodeInfo<?> getNodeInfo(GWTAuditable value) {
		/*/if(value instanceof GWTLedger){
			return new LedgerNodeInfo((GWTLedger)value);
		}else if (value instanceof GWTAccount){
			return new AccountNodeInfo((GWTAccount) value);
		}else{/*/
			return null;
	
	}

	@Override
	public boolean isLeaf(Object value) {
		if (value instanceof GWTAuditLine){
			return true;
		}else{
			return false;
		}
	}


	


	@Override
	public Date getDate() {
		//will do for now but we need to get a more meaningful thing here. 
		return new Date();
	}



	@Override
	public List<GWTAuditElement> audit() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected void onRangeChanged(HasData<GWTAuditable> display) {
		// TODO Auto-generated method stub
		
	}
}
