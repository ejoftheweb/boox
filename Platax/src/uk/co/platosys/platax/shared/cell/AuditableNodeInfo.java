package uk.co.platosys.platax.shared.cell;

import uk.co.platosys.platax.shared.boox.GWTAuditLine;
import uk.co.platosys.platax.shared.boox.GWTAuditable;
import uk.co.platosys.platax.shared.boox.GWTLedger;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public class AuditableNodeInfo implements TreeViewModel.NodeInfo<GWTAuditable>{
	//the Cell class used to render GWTAuditable s
	final SingleSelectionModel<GWTAuditable> selmod = new SingleSelectionModel<GWTAuditable>(); //note model has no key provider.
	private Cell<GWTAuditable> cell=new AuditCell();
    private ProvidesKey<GWTAuditable> keyProvider ;
	public AuditableNodeInfo(GWTAuditable value) {
		keyProvider=value;
	}

	@Override
	public Cell<GWTAuditable> getCell() {
		return cell;
	}

	@Override
	public ProvidesKey<GWTAuditable> getProvidesKey() {
		return keyProvider;
	}

	@Override
	public SelectionModel<? super GWTAuditable> getSelectionModel() {
		return selmod;
	}

	@Override
	public ValueUpdater<GWTAuditable> getValueUpdater() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void unsetDataDisplay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDataDisplay(HasData<GWTAuditable> display) {
		// TODO Auto-generated method stub
		
	}


}
